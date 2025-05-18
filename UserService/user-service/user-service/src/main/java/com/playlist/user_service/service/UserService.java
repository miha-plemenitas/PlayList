package com.playlist.user_service.service;

import com.playlist.user_service.model.User;
import com.playlist.user_service.repository.UserRepository;
import io.micrometer.core.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Timed(value = "user.register", description = "Time to register a user")
    public User registerUser(User user) {
        logger.info("Registering new user: {}", user.getEmail());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        logger.info("User registered successfully: {}", savedUser.getId());
        return savedUser;
    }

    @Timed(value = "user.find.by.email", description = "Time to find user by email")
    public Optional<User> findByEmail(String email) {
        logger.info("Searching for user by email: {}", email);
        return userRepository.findByEmail(email);
    }

    @Timed(value = "user.password.validation", description = "Time to validate password")
    public boolean isValidPassword(String rawPassword, String encodedPassword) {
        boolean isValid = passwordEncoder.matches(rawPassword, encodedPassword);
        logger.info("Password validation attempted for user, result: {}", isValid ? "Valid" : "Invalid");
        return isValid;
    }

    @Timed(value = "user.get.by.id", description = "Time to fetch user by ID")
    public Optional<User> getUserById(String userId) {
        logger.info("Fetching user profile with ID: {}", userId);
        return userRepository.findById(userId);
    }

    @Timed(value = "user.update", description = "Time to update a user")
    public User updateUser(String userId, User updatedUser) {
        logger.info("Updating user profile with ID: {}", userId);
        return userRepository.findById(userId)
                .map(user -> {
                    user.setUsername(updatedUser.getUsername());
                    user.setEmail(updatedUser.getEmail());

                    if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
                        user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
                    }

                    User updated = userRepository.save(user);
                    logger.info("User updated successfully: {}", userId);
                    return updated;
                }).orElseThrow(() -> {
                    logger.error("User with ID {} not found", userId);
                    return new RuntimeException("User not found!");
                });
    }

    @Timed(value = "user.delete", description = "Time to delete a user")
    public String deleteUser(String userId) {
        if (userRepository.existsById(userId)) {
            logger.info("Deleting user with ID: {}", userId);
            userRepository.deleteById(userId);
            logger.info("User deleted successfully: {}", userId);
            return "User deleted successfully!";
        }
        logger.warn("User with ID {} not found", userId);
        return "User not found!";
    }

    @Override
    @Timed(value = "user.auth.load", description = "Time to load user by username")
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        logger.info("Authenticating user with email: {}", email);
        return userRepository.findByEmail(email)
                .map(user -> {
                    logger.info("User found: {}", email);
                    return org.springframework.security.core.userdetails.User
                            .withUsername(user.getEmail())
                            .password(user.getPassword())
                            .roles("USER")
                            .build();
                })
                .orElseThrow(() -> {
                    logger.error("User with email {} not found", email);
                    return new UsernameNotFoundException("User not found");
                });
    }
}
