package org.szakdolgozat.szakdolgozatbackend.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.szakdolgozat.szakdolgozatbackend.authentication.AuthenticationController;
import org.szakdolgozat.szakdolgozatbackend.authentication.AuthenticationService;
import org.szakdolgozat.szakdolgozatbackend.authentication.auth.AuthenticationRequest;
import org.szakdolgozat.szakdolgozatbackend.authentication.auth.AuthenticationResponse;
import org.szakdolgozat.szakdolgozatbackend.authentication.auth.RegisterRequest;
import org.szakdolgozat.szakdolgozatbackend.model.Role;

class AuthenticationControllerTest {

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private AuthenticationController authenticationController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegister() {
        RegisterRequest request = RegisterRequest.builder()
                .username("testuser")
                .email("test@example.com")
                .password("password")
                .build();

        AuthenticationResponse response = AuthenticationResponse.builder()
                .token("jwtToken")
                .build();

        when(authenticationService.register(request)).thenReturn(response);

        ResponseEntity<AuthenticationResponse> result = authenticationController.register(request);

        assertNotNull(result);
        assertEquals(200, result.getStatusCodeValue());
        assertEquals("jwtToken", result.getBody().getToken());
        verify(authenticationService, times(1)).register(request);
    }

    @Test
    void testAuthenticate_Success() {
        AuthenticationRequest request = AuthenticationRequest.builder()
                .username("testuser")
                .password("password")
                .build();

        AuthenticationResponse response = AuthenticationResponse.builder()
                .token("jwtToken")
                .perm(Role.USER)
                .userID(1L)
                .build();

        when(authenticationService.authenticate(request)).thenReturn(response);

        ResponseEntity<AuthenticationResponse> result = authenticationController.authenticate(request);

        assertNotNull(result);
        assertEquals(200, result.getStatusCodeValue());
        assertEquals("jwtToken", result.getBody().getToken());
        assertEquals(Role.USER, result.getBody().getPerm());
        assertEquals(1L, result.getBody().getUserID());
        verify(authenticationService, times(1)).authenticate(request);
    }

    @Test
    void testAuthenticate_InvalidCredentials() {
        AuthenticationRequest request = AuthenticationRequest.builder()
                .username("testuser")
                .password("wrongpassword")
                .build();

        when(authenticationService.authenticate(request))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        BadCredentialsException exception = assertThrows(BadCredentialsException.class, () -> {
            authenticationController.authenticate(request);
        });

        assertEquals("Invalid credentials", exception.getMessage());
        verify(authenticationService, times(1)).authenticate(request);
    }
}

