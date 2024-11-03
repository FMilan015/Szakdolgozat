package org.szakdolgozat.szakdolgozatbackend.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;
import org.szakdolgozat.szakdolgozatbackend.controller.UserController;
import org.szakdolgozat.szakdolgozatbackend.model.Role;
import org.szakdolgozat.szakdolgozatbackend.model.User;
import org.szakdolgozat.szakdolgozatbackend.service.UserService;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

class UserControllerTest {

    @Mock
    private UserService userService;
    @Mock
    private Principal principal;
    @InjectMocks
    private UserController userController;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAll() {
        User user1 = new User(1L, "user1", "pass1");
        User user2 = new User(2L, "user2", "pass2");
        when(userService.getAll()).thenReturn(Arrays.asList(user1, user2));

        List<User> users = userController.getAll();

        assertEquals(2, users.size());
        verify(userService, times(1)).getAll();
    }

    @Test
    void testGetUserProfile() {
        when(principal.getName()).thenReturn("user1");
        User user = new User(1L, "user1", "pass1");
        user.setEmail("user1@example.com");
        when(userService.getUsersByUsername("user1")).thenReturn(user);

        ResponseEntity<?> response = userController.getUserProfile(principal);

        assertTrue(response.getStatusCode().is2xxSuccessful());
        Map<String, Object> profile = (Map<String, Object>) response.getBody();
        assertEquals("user1", profile.get("username"));
        assertEquals("user1@example.com", profile.get("email"));
        verify(userService, times(1)).getUsersByUsername("user1");
    }

    @Test
    void testGetById() {
        User user = new User(1L, "user", "pass");
        ResponseEntity<User> responseEntity = ResponseEntity.ok(user);
        when(userService.getById(1L)).thenReturn(responseEntity);

        ResponseEntity<User> response = userController.getById(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("user", response.getBody().getUsername());
        verify(userService, times(1)).getById(1L);
    }

    @Test
    void testGetUsersByUsername() {
        User user = new User(1L, "user", "pass");
        when(userService.getUsersByUsername("user")).thenReturn(user);

        User result = userController.getUsersByUsername("user");

        assertEquals("user", result.getUsername());
        verify(userService, times(1)).getUsersByUsername("user");
    }

    @Test
    void testGetAdmin() {
        User admin = new User(1L, "admin", "pass");
        admin.setRole(Role.ADMIN);
        when(userService.getAdmin()).thenReturn(Arrays.asList(admin));

        List<User> admins = userController.getAdmin();

        assertEquals(1, admins.size());
        assertEquals(Role.ADMIN, admins.get(0).getRole());
        verify(userService, times(1)).getAdmin();
    }

    @Test
    void testGetUserByEmail() {
        User user = new User(1L, "user", "pass");
        user.setEmail("user@example.com");
        when(userService.getUserByEmail("user@example.com")).thenReturn(user);

        User result = userController.getUserByEmail("user@example.com");

        assertEquals("user@example.com", result.getEmail());
        verify(userService, times(1)).getUserByEmail("user@example.com");
    }

    @Test
    void testCreate() {
        User user = new User(null, "newuser", "pass");
        User savedUser = new User(1L, "newuser", "pass");
        when(userService.create(user)).thenReturn(savedUser);

        User result = userController.create(user);

        assertNotNull(result.getUserID());
        assertEquals("newuser", result.getUsername());
        verify(userService, times(1)).create(user);
    }

    @Test
    void testUpdate() {
        User user = new User(1L, "user", "pass");
        ResponseEntity<User> responseEntity = ResponseEntity.ok(user);
        when(userService.update(user)).thenReturn(responseEntity);

        ResponseEntity<User> response = userController.update(user);

        assertEquals(200, response.getStatusCodeValue());
        verify(userService, times(1)).update(user);
    }
}
