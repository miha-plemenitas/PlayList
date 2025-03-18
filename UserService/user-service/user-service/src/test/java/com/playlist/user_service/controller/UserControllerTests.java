package com.playlist.user_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.playlist.user_service.model.User;
import com.playlist.user_service.service.UserService;
import com.playlist.user_service.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerTests {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private UserController userController;

    private User user;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        user = new User();
        user.setId("1");
        user.setUsername("testUser");
        user.setEmail("test@example.com");
        user.setPassword("password123");
    }

    @Test
    public void testRegisterUser() throws Exception {
        when(userService.registerUser(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"));

        verify(userService, times(1)).registerUser(any(User.class));
    }

    @Test
    public void testLoginUser() throws Exception {
        when(userService.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(userService.isValidPassword(anyString(), anyString())).thenReturn(true);
        when(jwtUtil.generateToken(anyString())).thenReturn("mock-jwt-token");

        mockMvc.perform(post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"test@example.com\", \"password\": \"password123\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("mock-jwt-token"));

        verify(userService, times(1)).findByEmail("test@example.com");
        verify(userService, times(1)).isValidPassword(anyString(), anyString());
    }

    @Test
    public void testGetUserProfile() throws Exception {
        when(userService.getUserById("1")).thenReturn(Optional.of(user));

        mockMvc.perform(get("/users/profile/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"));

        verify(userService, times(1)).getUserById("1");
    }
}
