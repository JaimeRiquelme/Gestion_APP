package gestion.proyectos.gestionproyectos.Service;

import gestion.proyectos.gestionproyectos.Entity.Proyect;
import gestion.proyectos.gestionproyectos.Entity.User;
import gestion.proyectos.gestionproyectos.Repository.ProyectRepository;
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

@ExtendWith(MockitoExtension.class)
class ProyectServiceTest {

    @Mock
    private ProyectRepository proyectRepository;

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
    @DisplayName("Test: Obtener todos los proyectos")
    void whenGetProyects_thenReturnProyectList() {
        // Arrange
        List<Proyect> proyectList = Arrays.asList(testProyect1, testProyect2);
        when(proyectRepository.findAll()).thenReturn(proyectList);

        // Act
        Iterable<Proyect> result = proyectService.getAll();
        List<Proyect> resultList = (List<Proyect>) result;

        // Assert
        assertNotNull(resultList);
        assertEquals(2, resultList.size());
        assertEquals(testProyect1.getNameProyect(), resultList.get(0).getNameProyect());
        assertEquals(testProyect2.getNameProyect(), resultList.get(1).getNameProyect());
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
        Proyect proyectToUpdate = testProyect1;
        proyectToUpdate.setNameProyect("Proyecto Actualizado");
        when(proyectRepository.save(any(Proyect.class))).thenReturn(proyectToUpdate);

        // Act
        Proyect updated = proyectService.update(proyectToUpdate.getIdProyecto(), proyectToUpdate);

        // Assert
        assertNotNull(updated);
        assertEquals("Proyecto Actualizado", updated.getNameProyect());
        assertEquals(testProyect1.getIdProyecto(), updated.getIdProyecto());
        verify(proyectRepository).save(proyectToUpdate);
    }

    @Test
    @DisplayName("Test: Eliminar proyecto")
    void whenDeleteProyect_thenVerifyDeletion() {
        // Arrange
        Long proyectId = 1L;
        doNothing().when(proyectRepository).deleteById(proyectId);

        // Act
        proyectService.delete(proyectId);

        // Assert
        verify(proyectRepository, times(1)).deleteById(proyectId);
    }
}