package org.szakdolgozat.szakdolgozatbackend.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.szakdolgozat.szakdolgozatbackend.model.User;

import java.util.List;

@Repository
public class UserRepository {
    private static final Logger logger = LoggerFactory.getLogger(UserRepository.class);

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public User save(User user) {
        logger.info("Saving user: {}", user.getUsername());
        return em.merge(user);
    }

    public List<User> findAll() {
        logger.info("Retrieving all users");
        return em.createQuery("SELECT u FROM User u", User.class).getResultList();
    }

    public User findByUsername(String username) {
        logger.info("Finding user by username: {}", username);
        return em.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                .setParameter("username", username)
                .getSingleResult();
    }

    public List<User> findAdmin() {
        logger.info("Finding all admins");
        return em.createQuery("SELECT u FROM User u WHERE u.role = 'ADMIN'", User.class).getResultList();
    }

    public User findByEmail(String email) {
        logger.info("Finding user by email: {}", email);
        return em.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class)
                .setParameter("email", email)
                .getSingleResult();
    }

    public User findById(Long id) {
        logger.info("Finding user by ID: {}", id);
        return em.find(User.class, id);
    }

    @Transactional
    public void deleteById(Long id) {
        logger.info("Deleting user by ID: {}", id);
        User user = findById(id);
        em.remove(user);
    }
}
