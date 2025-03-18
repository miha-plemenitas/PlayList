package com.playlist.user_service.controller;

import com.playlist.user_service.model.User;
import com.playlist.user_service.service.UserService;
import com.playlist.user_service.utils.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/users")
@Tag(name = "User API", description = "Endpoints for user management")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    @Operation(summary = "Register a new user")
    public User register(@RequestBody User user) {
        return userService.registerUser(user);
    }

    @PostMapping("/login")
    @Operation(summary = "Login a user and return JWT token")
    public String login(@RequestBody User loginRequest) {
        Optional<User> user = userService.findByEmail(loginRequest.getEmail());

        if (user.isPresent() && userService.isValidPassword(loginRequest.getPassword(), user.get().getPassword())) {
            return jwtUtil.generateToken(user.get().getEmail());
        }

        return "Invalid credentials!";
    }

    @GetMapping("/profile/{userId}")
    @Operation(summary = "Get user profile")
    public Optional<User> getUserProfile(@PathVariable String userId) {
        return userService.getUserById(userId);
    }

    @PutMapping("/profile/{userId}")
    @Operation(summary = "Update user profile")
    public User updateUserProfile(@PathVariable String userId, @RequestBody User updatedUser) {
        return userService.updateUser(userId, updatedUser);
    }

    @DeleteMapping("/profile/{userId}")
    @Operation(summary = "Delete user account")
    public String deleteUser(@PathVariable String userId) {
        return userService.deleteUser(userId);
    }
}
