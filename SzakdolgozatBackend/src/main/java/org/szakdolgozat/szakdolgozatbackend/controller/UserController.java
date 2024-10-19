package org.szakdolgozat.szakdolgozatbackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.szakdolgozat.szakdolgozatbackend.model.User;
import org.szakdolgozat.szakdolgozatbackend.repository.UserRepository;
import org.szakdolgozat.szakdolgozatbackend.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> getAll() {
        return userService.getAll();
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
