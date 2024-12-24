package gestion.proyectos.gestionproyectos.Service;

import gestion.proyectos.gestionproyectos.Entity.Proyect;
import gestion.proyectos.gestionproyectos.Entity.User;
import gestion.proyectos.gestionproyectos.Repository.ProyectRepository;
import gestion.proyectos.gestionproyectos.Repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProyectServiceTest {

    @Mock
    private ProyectRepository proyectRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private ProyectService proyectService;

    private Proyect testProyect1;
    private Proyect testProyect2;
    private User testUser;

    @BeforeEach
    void setUp() {
        // Configurar usuario de prueba
        testUser = new User();
        testUser.setIdUsuario(1L);
        testUser.setNames("John Doe");

        // Configurar proyectos de prueba
        testProyect1 = new Proyect();
        testProyect1.setIdProyecto(1L);
        testProyect1.setNameProyect("Proyecto Test 1");
        testProyect1.setDescription("Descripción del proyecto 1");
        testProyect1.setOrganization("Organización 1");
        testProyect1.setStartDate("2024-01-01");
        testProyect1.setEstimatedEndDate("2024-12-31");
        testProyect1.setUser(testUser);

        testProyect2 = new Proyect();
        testProyect2.setIdProyecto(2L);
        testProyect2.setNameProyect("Proyecto Test 2");
        testProyect2.setDescription("Descripción del proyecto 2");
        testProyect2.setOrganization("Organización 2");
        testProyect2.setStartDate("2024-02-01");
        testProyect2.setEstimatedEndDate("2024-11-30");
        testProyect2.setUser(testUser);
    }

    @Test
    @DisplayName("Test: Guardar proyecto nuevo")
    void whenSaveProyect_thenReturnSavedProyect() {
        // Arrange
        when(userRepository.findById(testUser.getIdUsuario())).thenReturn(Optional.of(testUser));
        when(proyectRepository.save(any(Proyect.class))).thenReturn(testProyect1);

        // Act
        Proyect savedProyect = proyectService.create(testProyect1);

        // Assert
        assertNotNull(savedProyect);
        assertEquals(testProyect1.getIdProyecto(), savedProyect.getIdProyecto());
        assertEquals(testProyect1.getNameProyect(), savedProyect.getNameProyect());
        assertEquals(testProyect1.getDescription(), savedProyect.getDescription());
        verify(proyectRepository).save(testProyect1);
    }

    @Test
    @DisplayName("Test: Guardar proyecto con usuario inválido lanza excepción")
    void whenSaveProyectWithInvalidUser_thenThrowException() {
        // Arrange
        when(userRepository.findById(testUser.getIdUsuario())).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            proyectService.create(testProyect1);
        });

        assertEquals("User not found with id " + testUser.getIdUsuario(), exception.getMessage());
        verify(proyectRepository, never()).save(any(Proyect.class));
    }

    @Test
    @DisplayName("Test: Obtener todos los proyectos")
    void whenGetAll_thenReturnProyectList() {
        // Arrange
        List<Proyect> proyectList = Arrays.asList(testProyect1, testProyect2);
        when(proyectRepository.findAll()).thenReturn(proyectList);

        // Act
        List<Proyect> result = proyectService.getAll();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(testProyect1.getNameProyect(), result.get(0).getNameProyect());
        assertEquals(testProyect2.getNameProyect(), result.get(1).getNameProyect());
        verify(proyectRepository).findAll();
    }

    @Test
    @DisplayName("Test: Obtener proyecto por ID existente")
    void whenGetProyectById_thenReturnProyect() {
        // Arrange
        when(proyectRepository.findById(1L)).thenReturn(Optional.of(testProyect1));

        // Act
        Proyect found = proyectService.getById(1L);

        // Assert
        assertNotNull(found);
        assertEquals(testProyect1.getIdProyecto(), found.getIdProyecto());
        assertEquals(testProyect1.getNameProyect(), found.getNameProyect());
        verify(proyectRepository).findById(1L);
    }

    @Test
    @DisplayName("Test: Obtener proyecto por ID inexistente")
    void whenGetProyectByInvalidId_thenReturnNull() {
        // Arrange
        when(proyectRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        Proyect result = proyectService.getById(999L);

        // Assert
        assertNull(result);
        verify(proyectRepository).findById(999L);
    }

    @Test
    @DisplayName("Test: Actualizar proyecto existente")
    void whenUpdateProyect_thenReturnUpdatedProyect() {
        // Arrange
        when(proyectRepository.findById(1L)).thenReturn(Optional.of(testProyect1));
        when(proyectRepository.save(any(Proyect.class))).thenReturn(testProyect1);

        Proyect updatedDetails = new Proyect();
        updatedDetails.setNameProyect("Proyecto Actualizado");
        updatedDetails.setDescription("Descripción actualizada");

        // Act
        Proyect updated = proyectService.update(1L, updatedDetails);

        // Assert
        assertNotNull(updated);
        assertEquals("Proyecto Actualizado", updated.getNameProyect());
        assertEquals("Descripción actualizada", updated.getDescription());
        verify(proyectRepository).save(any(Proyect.class));
    }



    @Test
    @DisplayName("Test: Eliminar proyecto inexistente lanza excepción")
    void whenDeleteNonExistentProyect_thenThrowException() {
        // Arrange
        when(proyectRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            proyectService.delete(999L);
        });

        assertEquals("No se encontró el proyecto con ID: 999", exception.getMessage());
        verify(proyectRepository, never()).deleteById(999L);
    }



}