package gestion.proyectos.gestionproyectos.Service;

import gestion.proyectos.gestionproyectos.Entity.RefreshToken;
import gestion.proyectos.gestionproyectos.Entity.User;
import gestion.proyectos.gestionproyectos.Repository.RefreshTokenRepository;
import gestion.proyectos.gestionproyectos.Repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RefreshTokenServiceTest {

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private RefreshTokenService refreshTokenService;

    private User testUser;
    private RefreshToken testRefreshToken;
    private final Long refreshTokenDurationMs = 86400000L; // 24 horas en milisegundos

    @BeforeEach
    void setUp() {
        // Configurar el valor de refreshTokenDurationMs usando ReflectionTestUtils
        ReflectionTestUtils.setField(refreshTokenService, "refreshTokenDurationMs", refreshTokenDurationMs);

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
        testRefreshToken = RefreshToken.builder()
                .token(UUID.randomUUID().toString())
                .user(testUser)
                .expiryDate(Instant.now().plusMillis(refreshTokenDurationMs))
                .build();
    }

    @Test
    void whenCreateRefreshToken_thenReturnNewToken() {
        // Given
        when(userRepository.findById(testUser.getIdUsuario())).thenReturn(Optional.of(testUser));
        when(refreshTokenRepository.save(any(RefreshToken.class))).thenReturn(testRefreshToken);
        doNothing().when(refreshTokenRepository).deleteByUserId(testUser.getIdUsuario());

        // When
        RefreshToken createdToken = refreshTokenService.createRefreshToken(testUser.getIdUsuario());

        // Then
        assertNotNull(createdToken);
        assertEquals(testUser, createdToken.getUser());
        assertNotNull(createdToken.getToken());
        assertNotNull(createdToken.getExpiryDate());
        assertTrue(createdToken.getExpiryDate().isAfter(Instant.now()));

        verify(userRepository).findById(testUser.getIdUsuario());
        verify(refreshTokenRepository).deleteByUserId(testUser.getIdUsuario());
        verify(refreshTokenRepository).save(any(RefreshToken.class));
    }

    @Test
    void whenCreateRefreshTokenWithInvalidUser_thenThrowException() {
        // Given
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        // Then
        assertThrows(RuntimeException.class, () -> {
            refreshTokenService.createRefreshToken(99L);
        });

        verify(userRepository).findById(99L);
        verify(refreshTokenRepository, never()).save(any(RefreshToken.class));
    }

    @Test
    void whenFindByToken_thenReturnToken() {
        // Given
        String tokenValue = "test-token";
        when(refreshTokenRepository.findByToken(tokenValue)).thenReturn(Optional.of(testRefreshToken));

        // When
        Optional<RefreshToken> found = refreshTokenService.findByToken(tokenValue);

        // Then
        assertTrue(found.isPresent());
        assertEquals(testRefreshToken, found.get());
        verify(refreshTokenRepository).findByToken(tokenValue);
    }

    @Test
    void whenVerifyNonExpiredToken_thenReturnToken() {
        // Given
        RefreshToken validToken = RefreshToken.builder()
                .token(UUID.randomUUID().toString())
                .user(testUser)
                .expiryDate(Instant.now().plusMillis(refreshTokenDurationMs))
                .build();

        // When
        RefreshToken verifiedToken = refreshTokenService.verifyExpiration(validToken);

        // Then
        assertNotNull(verifiedToken);
        assertEquals(validToken, verifiedToken);
        verify(refreshTokenRepository, never()).delete(any(RefreshToken.class));
    }

    @Test
    void whenVerifyExpiredToken_thenThrowException() {
        // Given
        RefreshToken expiredToken = RefreshToken.builder()
                .token(UUID.randomUUID().toString())
                .user(testUser)
                .expiryDate(Instant.now().minusMillis(refreshTokenDurationMs))
                .build();

        // Then
        assertThrows(RuntimeException.class, () -> {
            refreshTokenService.verifyExpiration(expiredToken);
        });

        verify(refreshTokenRepository).delete(expiredToken);
    }

    @Test
    void whenDeleteByUserId_thenCallRepository() {
        // Given
        Long userId = 1L;
        doNothing().when(refreshTokenRepository).deleteByUserId(userId);

        // When
        refreshTokenService.deleteByUserId(userId);

        // Then
        verify(refreshTokenRepository).deleteByUserId(userId);
    }

    @Test
    void whenDeleteByUser_thenCallRepository() {
        // Given
        doNothing().when(refreshTokenRepository).deleteByUser(testUser);

        // When
        refreshTokenService.deleteByUser(testUser);

        // Then
        verify(refreshTokenRepository).deleteByUser(testUser);
    }
}