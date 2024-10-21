package org.szakdolgozat.szakdolgozatbackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.szakdolgozat.szakdolgozatbackend.authentication.auth.RegisterRequest;
import org.szakdolgozat.szakdolgozatbackend.model.User;
import org.szakdolgozat.szakdolgozatbackend.repository.UserRepository;
import org.szakdolgozat.szakdolgozatbackend.service.UserService;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> getAll() {
        return userService.getAll();
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile(Principal principal) {
        String username = principal.getName();

        User user = userService.getUsersByUsername(username);
        Map<String, Object> profileData = new HashMap<>();
        profileData.put("username", user.getUsername());
        profileData.put("email", user.getEmail());

        return ResponseEntity.ok(profileData);
    }

    @GetMapping("{id}")
    public ResponseEntity<User> getById(@PathVariable Long id) {
        return userService.getById(id);
    }

    @GetMapping("/search")
    public User getUsersByUsername(@RequestParam String username) {
        return userService.getUsersByUsername(username);
    }

    @GetMapping("/admin")
    public List<User> getAdmin() {
        return userService.getAdmin();
    }

    @GetMapping("/email")
    public User getUserByEmail(@RequestParam String email) {
        return userService.getUserByEmail(email);
    }

    @PostMapping
    public User create(@RequestBody User user) {
        return userService.create(user);
    }

    @PutMapping
    public ResponseEntity<User> update(@RequestBody User user) {
        return userService.update(user);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        return userService.delete(id);
    }
}
