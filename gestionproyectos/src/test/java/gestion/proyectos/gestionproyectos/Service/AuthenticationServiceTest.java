package gestion.proyectos.gestionproyectos.Service;

import gestion.proyectos.gestionproyectos.DTOs.AuthResponse;
import gestion.proyectos.gestionproyectos.DTOs.LoginRequest;
import gestion.proyectos.gestionproyectos.DTOs.RegisterRequest;
import gestion.proyectos.gestionproyectos.Entity.RefreshToken;
import gestion.proyectos.gestionproyectos.Entity.User;
import gestion.proyectos.gestionproyectos.Repository.UserRepository;
import gestion.proyectos.gestionproyectos.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private RefreshTokenService refreshTokenService;

    @InjectMocks
    private AuthenticationService authenticationService;

    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;
    private User testUser;
    private RefreshToken testRefreshToken;
    private Authentication mockAuthentication;

    @BeforeEach
    void setUp() {
        // Configurar RegisterRequest para pruebas
        registerRequest = new RegisterRequest();
        registerRequest.setNames("Test User");
        registerRequest.setSecondNames("Test LastName");
        registerRequest.setEmail("test@example.com");
        registerRequest.setPassword("password123");
        registerRequest.setPhoneNumber("123456789");

        // Configurar LoginRequest para pruebas
        loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password123");

        // Configurar User para pruebas
        testUser = User.builder()
                .idUsuario(1L)
                .names("Test User")
                .secondNames("Test LastName")
                .email("test@example.com")
                .password("encodedPassword")
                .phoneNumber("123456789")
                .build();

        // Configurar RefreshToken para pruebas
        testRefreshToken = new RefreshToken();
        testRefreshToken.setToken("refresh-token-test");
        testRefreshToken.setUser(testUser);

        // Configurar Authentication mock
        mockAuthentication = mock(Authentication.class);
    }

    @Test
    void whenRegister_thenReturnAuthResponse() {
        // Given
        String expectedToken = "test-jwt-token";
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(jwtService.generateToken(any(User.class))).thenReturn(expectedToken);
        when(refreshTokenService.createRefreshToken(any(Long.class))).thenReturn(testRefreshToken);

        // When
        AuthResponse response = authenticationService.register(registerRequest);

        // Then
        assertNotNull(response);
        assertEquals(expectedToken, response.getAccessToken());
        assertEquals(testRefreshToken.getToken(), response.getRefreshToken());
        assertEquals(testUser.getIdUsuario(), response.getUserId());
        assertEquals(testUser.getEmail(), response.getEmail());
        assertEquals(testUser.getNames(), response.getNames());
        assertEquals(testUser.getSecondNames(), response.getSecondNames());
        assertEquals(testUser.getPhoneNumber(), response.getPhoneNumber());

        verify(passwordEncoder).encode(registerRequest.getPassword());
        verify(userRepository).save(any(User.class));
        verify(jwtService).generateToken(any(User.class));
        verify(refreshTokenService).createRefreshToken(any(Long.class));
    }

    @Test
    void whenLogin_thenReturnAuthResponse() {
        // Given
        String expectedToken = "test-jwt-token";
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mockAuthentication);
        when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.of(testUser));
        when(jwtService.generateToken(testUser)).thenReturn(expectedToken);
        when(refreshTokenService.createRefreshToken(testUser.getIdUsuario())).thenReturn(testRefreshToken);

        // When
        AuthResponse response = authenticationService.login(loginRequest);

        // Then
        assertNotNull(response);
        assertEquals(expectedToken, response.getAccessToken());
        assertEquals(testRefreshToken.getToken(), response.getRefreshToken());
        assertEquals(testUser.getIdUsuario(), response.getUserId());
        assertEquals(testUser.getEmail(), response.getEmail());
        assertEquals(testUser.getNames(), response.getNames());
        assertEquals(testUser.getSecondNames(), response.getSecondNames());
        assertEquals(testUser.getPhoneNumber(), response.getPhoneNumber());

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository).findByEmail(loginRequest.getEmail());
        verify(jwtService).generateToken(testUser);
        verify(refreshTokenService).createRefreshToken(testUser.getIdUsuario());
    }

    @Test
    void whenLoginWithInvalidCredentials_thenThrowException() {
        // Given
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new RuntimeException("Invalid credentials"));

        // Then
        assertThrows(RuntimeException.class, () -> {
            authenticationService.login(loginRequest);
        });

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository, never()).findByEmail(any());
        verify(jwtService, never()).generateToken(any());
        verify(refreshTokenService, never()).createRefreshToken(any());
    }

    @Test
    void whenLoginWithNonExistentUser_thenThrowException() {
        // Given
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mockAuthentication);
        when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.empty());

        // Then
        assertThrows(Exception.class, () -> {
            authenticationService.login(loginRequest);
        });

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository).findByEmail(loginRequest.getEmail());
        verify(jwtService, never()).generateToken(any());
        verify(refreshTokenService, never()).createRefreshToken(any());
    }

    @Test
    void whenRegisterWithExistingEmail_thenSaveNewUser() {
        // Given
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(jwtService.generateToken(any(User.class))).thenReturn("test-jwt-token");
        when(refreshTokenService.createRefreshToken(any(Long.class))).thenReturn(testRefreshToken);

        // When
        AuthResponse response = authenticationService.register(registerRequest);

        // Then
        assertNotNull(response);
        assertEquals(testUser.getEmail(), response.getEmail());

        verify(passwordEncoder).encode(registerRequest.getPassword());
        verify(userRepository).save(any(User.class));
        verify(jwtService).generateToken(any(User.class));
        verify(refreshTokenService).createRefreshToken(any(Long.class));
    }
}