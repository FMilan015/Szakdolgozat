package org.szakdolgozat.szakdolgozatbackend.authentication;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.szakdolgozat.szakdolgozatbackend.authentication.auth.AuthenticationRequest;
import org.szakdolgozat.szakdolgozatbackend.authentication.auth.AuthenticationResponse;
import org.szakdolgozat.szakdolgozatbackend.authentication.auth.RegisterRequest;
import org.szakdolgozat.szakdolgozatbackend.authentication.config.JwtService;
import org.szakdolgozat.szakdolgozatbackend.model.Role;
import org.szakdolgozat.szakdolgozatbackend.model.User;
import org.szakdolgozat.szakdolgozatbackend.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    public AuthenticationResponse register(RegisterRequest request) {
        logger.debug("Registering user with username: {}", request.getUsername());
        var user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        logger.info("User registered successfully: {}", request.getUsername());
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        logger.debug("Authenticating user with username: {}", request.getUsername());
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        var user = repository.findByUsername(request.getUsername());
        var jwtToken = jwtService.generateToken(user);
        logger.info("User authenticated successfully: {}", request.getUsername());
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .perm(user.getRole())
                .userID(user.getUserID())
                .build();
    }
}
