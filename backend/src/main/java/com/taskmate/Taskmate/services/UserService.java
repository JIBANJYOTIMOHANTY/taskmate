package com.taskmate.Taskmate.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.taskmate.Taskmate.entity.User;
import com.taskmate.Taskmate.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    @Lazy
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    private Set<String> logoutToken = new HashSet<String>();

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Implement logic to fetch user details from your database based on the
        // provided username
        Optional<User> user = userRepository.findByUsername(username);
        return user.map(CustomUserDetailsService::new)
                .orElseThrow(() -> new UsernameNotFoundException("Username not fount with this name : " + username));
    }

    public void LogoutToken(String token) {
        logoutToken.add(token);
    }

    public boolean isLogoutTokenExpired(String token) {
        return logoutToken.contains(token);
    }

    public ResponseEntity<HttpStatus> addUser(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            System.out.println("hello");
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        } else {

            System.out.println("not");
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
    }

    public ResponseEntity<String> loginUser(@RequestBody User user){
        try{
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
            if(authentication.isAuthenticated()){
                String token = jwtService.generatetoken(user.getUsername());
                return new ResponseEntity<>(token, HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        }
        catch(Exception e){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    public ResponseEntity<List<User>> getAllUsers(@RequestHeader("Authorization") String authorization){
        String token = authorization.substring(7);
        String username = jwtService.extractUsername(token);

        UserDetails userDetails = loadUserByUsername(username);
        
        if(jwtService.validateToken(token, userDetails)){
            return new ResponseEntity<>(userRepository.findAll(), HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    public ResponseEntity<String> logOut(@RequestHeader("Authorization") String authorization){
        // String token = authorization.substring(7);
        String username = null;
        UserDetails userDetails = null;
        String authHeader = authorization;
        String token = null;

        if(authHeader != null && authHeader.startsWith("Bearer")){
            token = authHeader.substring(7);
            username = jwtService.extractUsername(token);
            userDetails = loadUserByUsername(username);
        }
        
        if(jwtService.validateToken(token, userDetails)){
            LogoutToken(token);
            return new ResponseEntity<>("!!!You are loggedout successfully!!!",HttpStatus.OK);
        } else {

            return new ResponseEntity<>("!!!You are not loggedin!!!",HttpStatus.UNAUTHORIZED);
        }
        
    }

}
