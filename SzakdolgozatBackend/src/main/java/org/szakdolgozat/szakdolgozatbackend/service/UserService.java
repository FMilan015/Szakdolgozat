package org.szakdolgozat.szakdolgozatbackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.szakdolgozat.szakdolgozatbackend.model.User;
import org.szakdolgozat.szakdolgozatbackend.repository.UserRepository;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public ResponseEntity<User> getById(Long id) {
        User user = userRepository.findById(id);
        if (user == null)
            return ResponseEntity.notFound().build();
        else
            return ResponseEntity.ok(user);
    }

    public User getUsersByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public List<User> getAdmin() {
        return userRepository.findAdmin();
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User create(User user) {
        return userRepository.save(user);
    }

    public ResponseEntity<User> update(User user) {
        User u = userRepository.findById(user.getUserID());
        if (u == null)
            return ResponseEntity.notFound().build();
        u = userRepository.save(user);
        return ResponseEntity.ok(u);
    }

    public ResponseEntity<?> delete(Long id) {
        User user = userRepository.findById(id);
        if (user == null)
            return ResponseEntity.notFound().build();
        else {
            userRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
    }
}
