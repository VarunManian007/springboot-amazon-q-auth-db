package com.example;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import com.example.controller.UserController;
import com.example.entity.User;
import com.example.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetUserById_ExistingUser_ReturnsUser() {
        Long userId = 1L;
        User mockUser = new User(userId, "John Doe", "john@example.com", "xyz", "xyz");
        when(userService.getUserById(userId)).thenReturn(mockUser);

        ResponseEntity<User> response = userController.getUserById(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockUser, response.getBody());
        verify(userService, times(1)).getUserById(userId);
    }

    @Test
    public void testGetUserById_NonExistingUser_ReturnsNotFound() {
        Long userId = 999L;
        when(userService.getUserById(userId)).thenReturn(null);

        ResponseEntity<User> response = userController.getUserById(userId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(userService, times(1)).getUserById(userId);
    }


    @Test
    public void testGetAllUsers_ReturnsListOfUsers() {
        List<User> mockUsers = Arrays.asList(
                new User(1L, "John Doe", "john@example.com","xyz", "xyz"),
                new User(2L, "Jane Doe", "jane@example.com","xyz", "xyz")
        );
        when(userService.getAllUsers()).thenReturn(mockUsers);

        ResponseEntity<List<User>> response = userController.getAllUsers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockUsers, response.getBody());
        assertEquals(2, response.getBody().size());
        verify(userService, times(1)).getAllUsers();
    }

    @Test
    public void testCreateUser_ValidUser_ReturnsCreatedUser() {
        User newUser = new User(null, "Alice Smith", "alice@example.com","xyz", "xyz");
        User createdUser = new User(3L, "Alice Smith", "alice@example.com","xyz", "xyz");
        when(userService.createUser(newUser)).thenReturn(createdUser);

        ResponseEntity<User> response = userController.createUser(newUser);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(createdUser, response.getBody());
        verify(userService, times(1)).createUser(newUser);
    }

//    @Test
//    public void testUpdateUser_ExistingUser_ReturnsUpdatedUser() {
//        Long userId = 1L;
//        User updatedUser = new User(userId, "John Updated", "john.updated@example.com","xyz", "xyz");
//        when(userService.updateUser(eq(userId), any(User.class))).thenReturn(updatedUser);
//
//        ResponseEntity<User> response = userController.updateUser(userId, updatedUser);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(updatedUser, response.getBody());
//        verify(userService, times(1)).updateUser(eq(userId), any(User.class));
//    }
//
//    @Test
//    public void testDeleteUser_ExistingUser_ReturnsNoContent() {
//        Long userId = 1L;
//        doNothing().when(userService).deleteUser(userId);
//
//        ResponseEntity<Void> response = userController.deleteUser(userId);
//
//        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
//        verify(userService, times(1)).deleteUser(userId);
//    }
}
