package org.szakdolgozat.szakdolgozatbackend.authentication;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.szakdolgozat.szakdolgozatbackend.authentication.auth.AuthenticationRequest;
import org.szakdolgozat.szakdolgozatbackend.authentication.auth.AuthenticationResponse;
import org.szakdolgozat.szakdolgozatbackend.authentication.auth.RegisterRequest;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);
    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ) {
        logger.info("Register attempt for username: {}", request.getUsername());
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        logger.info("Authentication attempt for username: {}", request.getUsername());
        return ResponseEntity.ok(service.authenticate(request));
    }
}
