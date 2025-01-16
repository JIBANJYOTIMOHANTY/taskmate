package com.taskmate.Taskmate.services;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.taskmate.Taskmate.entity.User;
import com.taskmate.Taskmate.repository.UserRepository;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Implement logic to fetch user details from your database based on the provided username
        Optional<User> user = userRepository.findByUsername(username);
        return user.map(CustomUserDetailsService::new).orElseThrow(() -> new UsernameNotFoundException("Username not fount with this name : " + username));
    }
    
}
