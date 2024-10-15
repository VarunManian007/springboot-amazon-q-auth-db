package com.example;

import com.example.controller.UserController;
import com.example.dto.UserDto;
import com.example.exception.ResourceNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    public void testGetUserById_Success() throws Exception {
        Long id = 1L;
        UserDto expectedUser = new UserDto(id, "John Doe", "john.doe@example.com");

        mockMvc.perform(get("/api/users/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(expectedUser.getId()))
                .andExpect(jsonPath("$.name").value(expectedUser.getName()))
                .andExpect(jsonPath("$.email").value(expectedUser.getEmail()));
    }

    @Test
    public void testCreateUser() throws Exception {
        UserDto user = new UserDto(1L, "John Doe", "john.doe@example.com");

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"name\":\"John Doe\",\"email\":\"john.doe@example.com\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.name").value(user.getName()))
                .andExpect(jsonPath("$.email").value(user.getEmail()));
    }

    @Test
    public void testGetUserNames() throws Exception {
        mockMvc.perform(get("/api/users/names"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0]").value("alice"))
                .andExpect(jsonPath("$[1]").value("bob"))
                .andExpect(jsonPath("$[2]").value("charlie"));
    }

    @Test
    public void testGetIdByName_ValidId() throws Exception {
        int id = 1;

        mockMvc.perform(get("/api/users/getIdByName/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andExpect(content().string("Monday"));
    }

    @Test
    public void testGetIdByName_UnknownId() throws Exception {
        int id = 5;

        mockMvc.perform(get("/api/users/getIdByName/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andExpect(content().string("Unknown"));
    }
}
