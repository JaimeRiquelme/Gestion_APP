package gestion.proyectos.gestionproyectos.Service;

import gestion.proyectos.gestionproyectos.Entity.Management;
import gestion.proyectos.gestionproyectos.Entity.Proyect;
import gestion.proyectos.gestionproyectos.Repository.ManagementRepository;
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
class ManagementServiceTest {

    @Mock
    private ManagementRepository managementRepository;

    @InjectMocks
    private ManagementService managementService;

    private Management testManagement1;
    private Management testManagement2;
    private Proyect testProyect;

    @BeforeEach
    void setUp() {
        // Configurar proyecto de prueba
        testProyect = new Proyect();
        testProyect.setIdProyecto(1L);
        testProyect.setNameProyect("Proyecto Test");

        // Configurar gestiones de prueba
        testManagement1 = new Management();
        testManagement1.setIdManagement(1L);
        testManagement1.setProyect(testProyect);
        testManagement1.setNameManagement("Gestión 1");
        testManagement1.setDescription("Descripción de gestión 1");

        testManagement2 = new Management();
        testManagement2.setIdManagement(2L);
        testManagement2.setProyect(testProyect);
        testManagement2.setNameManagement("Gestión 2");
        testManagement2.setDescription("Descripción de gestión 2");
    }

    @Test
    @DisplayName("Test: Guardar gestión exitosamente")
    void whenSaveManagement_thenReturnSavedManagement() {
        // Arrange
        when(managementRepository.save(any(Management.class))).thenReturn(testManagement1);

        // Act
        Management savedManagement = managementService.create(testManagement1);

        // Assert
        assertNotNull(savedManagement);
        assertEquals(testManagement1.getIdManagement(), savedManagement.getIdManagement());
        assertEquals(testManagement1.getNameManagement(), savedManagement.getNameManagement());
        assertEquals(testManagement1.getProyect(), savedManagement.getProyect());
        verify(managementRepository).save(testManagement1);
    }

    @Test
    @DisplayName("Test: Obtener todas las gestiones")
    void whenGetAll_thenReturnManagementList() {
        // Arrange
        List<Management> managementList = Arrays.asList(testManagement1, testManagement2);
        when(managementRepository.findAll()).thenReturn(managementList);

        // Act
        List<Management> result = managementService.getAll();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(testManagement1.getIdManagement(), result.get(0).getIdManagement());
        assertEquals(testManagement2.getIdManagement(), result.get(1).getIdManagement());
        verify(managementRepository).findAll();
    }

    @Test
    @DisplayName("Test: Obtener gestión por ID existente")
    void whenGetById_thenReturnManagement() {
        // Arrange
        when(managementRepository.findById(1L)).thenReturn(Optional.of(testManagement1));

        // Act
        Management found = managementService.getById(1L);

        // Assert
        assertNotNull(found);
        assertEquals(testManagement1.getIdManagement(), found.getIdManagement());
        assertEquals(testManagement1.getNameManagement(), found.getNameManagement());
        verify(managementRepository).findById(1L);
    }

    @Test
    @DisplayName("Test: Actualizar gestión existente")
    void whenUpdateManagement_thenReturnUpdatedManagement() {
        // Arrange
        Management managementToUpdate = testManagement1;
        managementToUpdate.setNameManagement("Gestión Actualizada");
        when(managementRepository.save(any(Management.class))).thenReturn(managementToUpdate);

        // Act
        Management updated = managementService.update(managementToUpdate.getIdManagement(), managementToUpdate);

        // Assert
        assertNotNull(updated);
        assertEquals("Gestión Actualizada", updated.getNameManagement());
        assertEquals(testManagement1.getIdManagement(), updated.getIdManagement());
        verify(managementRepository).save(managementToUpdate);
    }

    @Test
    @DisplayName("Test: Eliminar gestión existente")
    void whenDeleteExistingManagement_thenReturnTrue() throws Exception {
        // Arrange
        Long managementId = 1L;
        when(managementRepository.findById(managementId)).thenReturn(Optional.of(testManagement1));
        doNothing().when(managementRepository).deleteById(managementId);

        // Act
        managementService.delete(managementId);

        // Assert
        verify(managementRepository).findById(managementId);
        verify(managementRepository).deleteById(managementId);
    }

    @Test
    @DisplayName("Test: Eliminar gestión inexistente lanza excepción")
    void whenDeleteNonExistingManagement_thenThrowException() {
        // Arrange
        Long managementId = 999L;
        when(managementRepository.findById(managementId)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> {
            managementService.delete(managementId);
        });

        assertEquals("Management " + managementId + " does not exist", exception.getMessage());
        verify(managementRepository).findById(managementId);
        verify(managementRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("Test: Obtener gestión por ID inexistente lanza excepción")
    void whenGetByInvalidId_thenThrowException() {
        // Arrange
        when(managementRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(java.util.NoSuchElementException.class, () -> {
            managementService.getById(999L);
        });
        verify(managementRepository).findById(999L);
    }
}