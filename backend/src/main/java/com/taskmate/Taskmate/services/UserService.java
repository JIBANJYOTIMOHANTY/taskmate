package com.taskmate.Taskmate.services;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

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

    public void addLogoutToken(String token) {
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
        // try{
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
            if(authentication.isAuthenticated()){
                String token = jwtService.generatetoken(user.getUsername());
                System.out.println("hello");
                return new ResponseEntity<>(token, HttpStatus.OK);
            }
            else{
                System.out.println(user.getUsername());
                System.out.println(user.getPassword());
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        // }
        // catch(Exception e){
            // return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        // }
    }

}
