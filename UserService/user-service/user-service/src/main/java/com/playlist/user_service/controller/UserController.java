package com.playlist.user_service.controller;

import com.playlist.user_service.model.User;
import com.playlist.user_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public User register(@RequestBody User user) {
        return userService.registerUser(user);
    }

    @PostMapping("/login")
    public String login(@RequestBody User loginRequest) { // ✅ Accept JSON in the body
        return userService.loginUser(loginRequest.getEmail(), loginRequest.getPassword());
    }
}
