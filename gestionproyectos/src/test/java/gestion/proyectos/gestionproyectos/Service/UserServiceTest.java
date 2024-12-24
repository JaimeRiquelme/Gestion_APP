package gestion.proyectos.gestionproyectos.Service;

import gestion.proyectos.gestionproyectos.Entity.User;
import gestion.proyectos.gestionproyectos.Repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User testUser1;
    private User testUser2;

    @BeforeEach
    void setUp() {
        // Configurar usuarios de prueba
        testUser1 = User.builder()
                .idUsuario(1L)
                .names("John")
                .secondNames("Doe")
                .email("john.doe@example.com")
                .password("password123")
                .phoneNumber("123456789")
                .build();

        testUser2 = User.builder()
                .idUsuario(2L)
                .names("Jane")
                .secondNames("Smith")
                .email("jane.smith@example.com")
                .password("password456")
                .phoneNumber("987654321")
                .build();
    }

    @Test
    @DisplayName("Guardar usuario nuevo")
    void whenSaveUser_thenReturnSavedUser() {
        // Arrange
        when(userRepository.save(any(User.class))).thenReturn(testUser1);

        // Act
        User savedUser = userService.create(testUser1);

        // Assert
        assertNotNull(savedUser);
        assertEquals(testUser1.getIdUsuario(), savedUser.getIdUsuario());
        assertEquals(testUser1.getNames(), savedUser.getNames());
        assertEquals(testUser1.getEmail(), savedUser.getEmail());
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("Obtener todos los usuarios")
    void whenGetAllUsers_thenReturnUserList() {
        // Arrange
        List<User> userList = Arrays.asList(testUser1, testUser2);
        when(userRepository.findAll()).thenReturn(userList);

        // Act
        List<User> result = userService.getAll();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(testUser1.getNames(), result.get(0).getNames());
        assertEquals(testUser2.getNames(), result.get(1).getNames());
        verify(userRepository).findAll();
    }

    @Test
    @DisplayName("Obtener usuario por ID existente")
    void whenGetUserById_thenReturnUser() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser1));

        // Act
        User found = userService.getById(1L);

        // Assert
        assertNotNull(found);
        assertEquals(testUser1.getIdUsuario(), found.getIdUsuario());
        assertEquals(testUser1.getNames(), found.getNames());
        verify(userRepository).findById(1L);
    }

    @Test
    @DisplayName("Obtener usuario por ID inexistente")
    void whenGetUserByInvalidId_thenReturnNull() {
        // Arrange
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        User result = userService.getById(999L);

        // Assert
        assertNull(result);
        verify(userRepository).findById(999L);
    }

    @Test
    @DisplayName("Actualizar usuario existente")
    void whenUpdateUser_thenReturnUpdatedUser() {
        // Arrange
        User userToUpdate = User.builder()
                .names("John Updated")
                .secondNames("Doe Updated")
                .email("john.updated@example.com")
                .password("newpassword123")
                .phoneNumber("1234567890")
                .build();
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser1));
        when(userRepository.save(any(User.class))).thenReturn(userToUpdate);

        // Act
        User updatedUser = userService.update(1L, userToUpdate);

        // Assert
        assertNotNull(updatedUser);
        assertEquals("John Updated", updatedUser.getNames());
        assertEquals("Doe Updated", updatedUser.getSecondNames());
        assertEquals("john.updated@example.com", updatedUser.getEmail());
        assertEquals("newpassword123", updatedUser.getPassword());
        assertEquals("1234567890", updatedUser.getPhoneNumber());
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("Actualizar usuario inexistente lanza excepción")
    void whenUpdateNonExistentUser_thenThrowException() {
        // Arrange
        User userToUpdate = User.builder()
                .names("John Updated")
                .secondNames("Doe Updated")
                .email("john.updated@example.com")
                .password("newpassword123")
                .phoneNumber("1234567890")
                .build();
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.update(999L, userToUpdate);
        });

        assertEquals("User not found with id 999", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Eliminar usuario existente")
    void whenDeleteUser_thenVerifyDeletion() {
        // Arrange
        Long userId = 1L;
        doNothing().when(userRepository).deleteById(userId);

        // Act
        userService.delete(userId);

        // Assert
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    @DisplayName("Eliminar usuario inexistente lanza excepción")
    void whenDeleteNonExistentUser_thenThrowException() {
        // Arrange
        Long userId = 999L;
        doThrow(new RuntimeException("User not found with id " + userId)).when(userRepository).deleteById(userId);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.delete(userId);
        });

        assertEquals("User not found with id 999", exception.getMessage());
        verify(userRepository, times(1)).deleteById(userId);
    }
}