package org.szakdolgozat.szakdolgozatbackend.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;



import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.szakdolgozat.szakdolgozatbackend.authentication.AuthenticationService;
import org.szakdolgozat.szakdolgozatbackend.authentication.auth.AuthenticationRequest;
import org.szakdolgozat.szakdolgozatbackend.authentication.auth.AuthenticationResponse;
import org.szakdolgozat.szakdolgozatbackend.authentication.auth.RegisterRequest;
import org.szakdolgozat.szakdolgozatbackend.authentication.config.JwtService;
import org.szakdolgozat.szakdolgozatbackend.model.Role;
import org.szakdolgozat.szakdolgozatbackend.model.User;
import org.szakdolgozat.szakdolgozatbackend.repository.UserRepository;

class AuthenticationServiceTest {

    @Mock
    private UserRepository repository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegister_Success() {
        RegisterRequest request = RegisterRequest.builder()
                .username("testuser")
                .email("test@example.com")
                .password("password")
                .build();

        User savedUser = User.builder()
                .userID(1L)
                .username("testuser")
                .email("test@example.com")
                .password("encodedPassword")
                .role(Role.USER)
                .build();

        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(repository.save(any(User.class))).thenReturn(savedUser);
        when(jwtService.generateToken(any(User.class))).thenReturn("jwtToken");

        AuthenticationResponse response = authenticationService.register(request);

        assertNotNull(response);
        assertEquals("jwtToken", response.getToken());
        verify(repository, times(1)).save(any(User.class));
        verify(jwtService, times(1)).generateToken(any(User.class));
    }

    @Test
    void testAuthenticate_Success() {
        AuthenticationRequest request = AuthenticationRequest.builder()
                .username("testuser")
                .password("password")
                .build();

        User user = User.builder()
                .userID(1L)
                .username("testuser")
                .email("test@example.com")
                .password("encodedPassword")
                .role(Role.USER)
                .build();

        when(repository.findByUsername("testuser")).thenReturn(user);
        when(jwtService.generateToken(user)).thenReturn("jwtToken");

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword(),
                user.getAuthorities()
        );
        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(authentication);

        AuthenticationResponse response = authenticationService.authenticate(request);

        assertNotNull(response);
        assertEquals("jwtToken", response.getToken());
        assertEquals(Role.USER, response.getPerm());
        assertEquals(1L, response.getUserID());
        verify(authenticationManager, times(1)).authenticate(any(Authentication.class));
        verify(jwtService, times(1)).generateToken(user);
    }

    @Test
    void testAuthenticate_InvalidCredentials() {
        AuthenticationRequest request = AuthenticationRequest.builder()
                .username("testuser")
                .password("wrongpassword")
                .build();

        doThrow(new BadCredentialsException("Invalid credentials"))
                .when(authenticationManager).authenticate(any(Authentication.class));

        BadCredentialsException exception = assertThrows(BadCredentialsException.class, () -> {
            authenticationService.authenticate(request);
        });

        assertEquals("Invalid credentials", exception.getMessage());
        verify(authenticationManager, times(1)).authenticate(any(Authentication.class));
        verify(jwtService, never()).generateToken(any(User.class));
    }
}

