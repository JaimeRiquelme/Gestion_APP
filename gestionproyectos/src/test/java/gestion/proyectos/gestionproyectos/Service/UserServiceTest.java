package gestion.proyectos.gestionproyectos.Service;

import gestion.proyectos.gestionproyectos.Entity.User;
import gestion.proyectos.gestionproyectos.Repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
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

@ExtendWith(MockitoExtension.class) // Usar esto en lugar de @SpringBootTest
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
        testUser1 = new User();
        testUser1.setIdUsuario(1L);
        testUser1.setNames("John");
        testUser1.setSecondNames("Doe");
        testUser1.setEmail("john.doe@example.com");
        testUser1.setPassword("password123");
        testUser1.setPhoneNumber("123456789");

        testUser2 = new User();
        testUser2.setIdUsuario(2L);
        testUser2.setNames("Jane");
        testUser2.setSecondNames("Smith");
        testUser2.setEmail("jane.smith@example.com");
        testUser2.setPassword("password456");
        testUser2.setPhoneNumber("987654321");
    }

    @Test
    @DisplayName("Test: Guardar usuario nuevo")
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
        verify(userRepository).save(testUser1);
    }

    @Test
    @DisplayName("Test: Obtener todos los usuarios")
    void whenGetUsers_thenReturnUserList() {
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
    @DisplayName("Test: Obtener usuario por ID existente")
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
    @DisplayName("Test: Obtener usuario por ID inexistente")
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
    @DisplayName("Test: Actualizar usuario existente")
    void whenUpdateUser_thenReturnUpdatedUser() {
        // Arrange
        User userToUpdate = testUser1;
        userToUpdate.setNames("John Updated");
        when(userRepository.save(any(User.class))).thenReturn(userToUpdate);

        // Act
        User updated = userService.update(userToUpdate.getIdUsuario(), userToUpdate);

        // Assert
        assertNotNull(updated);
        assertEquals("John Updated", updated.getNames());
        assertEquals(testUser1.getIdUsuario(), updated.getIdUsuario());
        verify(userRepository).save(userToUpdate);
    }

    @Test
    @DisplayName("Test: Eliminar usuario")
    void whenDeleteUser_thenVerifyDeletion() {
        // Arrange
        Long userId = 1L;
        doNothing().when(userRepository).deleteById(userId);

        // Act
        userService.delete(userId);

        // Assert
        verify(userRepository, times(1)).deleteById(userId);
    }
}