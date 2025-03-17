package com.playlist.user_service.service;

import com.playlist.user_service.model.User;
import com.playlist.user_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerUser(User user) {
        System.out.println("Received User: " + user);

        // Hash the password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User savedUser = userRepository.save(user);
        System.out.println("Saved User: " + savedUser);

        return savedUser;
    }

    public String loginUser(String email, String rawPassword) {
        Optional<User> user = userRepository.findByEmail(email);
        
        if (user.isPresent() && passwordEncoder.matches(rawPassword, user.get().getPassword())) {
            System.out.println("Login Successful for user: " + email);
            return "Login successful!";
        }

        System.out.println("Login Failed for user: " + email);
        return "Invalid credentials!";
    }
}
