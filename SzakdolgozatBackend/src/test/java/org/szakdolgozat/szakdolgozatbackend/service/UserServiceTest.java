package org.szakdolgozat.szakdolgozatbackend.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;
import org.szakdolgozat.szakdolgozatbackend.model.Role;
import org.szakdolgozat.szakdolgozatbackend.model.User;
import org.szakdolgozat.szakdolgozatbackend.repository.UserRepository;

import java.util.Arrays;
import java.util.List;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAll() {
        User user1 = new User(1L, "user1", "pass1");
        User user2 = new User(2L, "user2", "pass2");
        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));
        
        List<User> users = userService.getAll();
        
        assertEquals(2, users.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testGetById_Found() {
        User user = new User(1L, "user1", "pass1");
        when(userRepository.findById(1L)).thenReturn(user);
        
        ResponseEntity<User> response = userService.getById(1L);
        
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals("user1", response.getBody().getUsername());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testGetById_NotFound() {
        when(userRepository.findById(1L)).thenReturn(null);
        
        ResponseEntity<User> response = userService.getById(1L);
        
        assertTrue(response.getStatusCode().is4xxClientError());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testGetUsersByUsername() {
        User user = new User(1L, "user1", "pass1");
        when(userRepository.findByUsername("user1")).thenReturn(user);
        
        User result = userService.getUsersByUsername("user1");
        
        assertEquals("user1", result.getUsername());
        verify(userRepository, times(1)).findByUsername("user1");
    }

    @Test
    void testGetAdmin() {
        User admin = new User(1L, "admin", "pass");
        admin.setRole(Role.ADMIN);
        when(userRepository.findAdmin()).thenReturn(Arrays.asList(admin));
        
        List<User> admins = userService.getAdmin();
        
        assertEquals(1, admins.size());
        assertEquals(Role.ADMIN, admins.get(0).getRole());
        verify(userRepository, times(1)).findAdmin();
    }

    @Test
    void testGetUserByEmail() {
        User user = new User(1L, "user", "pass");
        user.setEmail("user@example.com");
        when(userRepository.findByEmail("user@example.com")).thenReturn(user);
        
        User result = userService.getUserByEmail("user@example.com");
        
        assertEquals("user@example.com", result.getEmail());
        verify(userRepository, times(1)).findByEmail("user@example.com");
    }

    @Test
    void testCreate() {
        User user = new User(null, "newuser", "pass");
        User savedUser = new User(1L, "newuser", "pass");
        when(userRepository.save(user)).thenReturn(savedUser);
        
        User result = userService.create(user);
        
        assertNotNull(result.getUserID());
        assertEquals("newuser", result.getUsername());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testUpdate_Found() {
        User existingUser = new User(1L, "user1", "pass1");
        User updatedUser = new User(1L, "user1updated", "pass1updated");
        when(userRepository.findById(1L)).thenReturn(existingUser);
        when(userRepository.save(updatedUser)).thenReturn(updatedUser);
        
        ResponseEntity<User> response = userService.update(updatedUser);
        
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals("user1updated", response.getBody().getUsername());
        verify(userRepository, times(1)).save(updatedUser);
    }

    @Test
    void testUpdate_NotFound() {
        User updatedUser = new User(1L, "user1updated", "pass1updated");
        when(userRepository.findById(1L)).thenReturn(null);
        
        ResponseEntity<User> response = userService.update(updatedUser);
        
        assertTrue(response.getStatusCode().is4xxClientError());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testDelete_Found() {
        User user = new User(1L, "user", "pass");
        when(userRepository.findById(1L)).thenReturn(user);
        
        ResponseEntity<?> response = userService.delete(1L);
        
        assertTrue(response.getStatusCode().is2xxSuccessful());
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDelete_NotFound() {
        when(userRepository.findById(1L)).thenReturn(null);
        
        ResponseEntity<?> response = userService.delete(1L);
        
        assertTrue(response.getStatusCode().is4xxClientError());
        verify(userRepository, never()).deleteById(1L);
    }
}
