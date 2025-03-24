package com.playlist.user_service.repository;

import com.playlist.user_service.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

// Tests the repository's ability to save a User to the MongoDB database and retrieve it by email
@ExtendWith(SpringExtension.class)
@DataMongoTest
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testSaveAndFindByEmail() {
        User user = new User();
        user.setEmail("test.test@example.com");
        user.setUsername("testtestUser");
        user.setPassword("test123");

        userRepository.save(user);
        Optional<User> foundUser = userRepository.findByEmail("test.test@example.com");

        assertTrue(foundUser.isPresent());
        assertEquals("testtestUser", foundUser.get().getUsername());
    }
}
