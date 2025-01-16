package com.taskmate.Taskmate.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taskmate.Taskmate.entity.User;
import com.taskmate.Taskmate.services.UserService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/auth/api")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/addUser")
    public ResponseEntity<HttpStatus> createUser(@RequestBody User user){
        return userService.addUser(user);
        // return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/loginUser")
    public ResponseEntity<String> loginUser(@RequestBody User user){
        return userService.loginUser(user);
    }

    @GetMapping("/getUsers")
    public ResponseEntity<List<User>> getAllUsers(@RequestHeader("Authorization") String authorization){
        return userService.getAllUsers(authorization);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String authorization){
        return userService.logOut(authorization);
    }
}
