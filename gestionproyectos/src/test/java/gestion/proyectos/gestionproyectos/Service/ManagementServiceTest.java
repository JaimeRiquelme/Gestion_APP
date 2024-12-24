package gestion.proyectos.gestionproyectos.Service;

import gestion.proyectos.gestionproyectos.Entity.Management;
import gestion.proyectos.gestionproyectos.Entity.Proyect;
import gestion.proyectos.gestionproyectos.Repository.ManagementRepository;
import gestion.proyectos.gestionproyectos.Repository.ProyectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ManagementServiceTest {

    @Mock
    private ManagementRepository managementRepository;

    @Mock
    private ProyectRepository proyectRepository;

    @InjectMocks
    private ManagementService managementService;

    private Management testManagement1;
    private Management testManagement2;
    private Proyect testProyect;

    @BeforeEach
    void setUp() {
        testProyect = new Proyect();
        testProyect.setIdProyecto(1L);
        testProyect.setNameProyect("Proyecto Test");

        testManagement1 = new Management();
        testManagement1.setIdManagement(1L);
        testManagement1.setProyect(testProyect);
        testManagement1.setNameManagement("Gestión 1");
        testManagement1.setDescription("Descripción gestión 1");

        testManagement2 = new Management();
        testManagement2.setIdManagement(2L);
        testManagement2.setProyect(testProyect);
        testManagement2.setNameManagement("Gestión 2");
        testManagement2.setDescription("Descripción gestión 2");
    }

    @Test
    @DisplayName("Crear una nueva gestión con proyecto válido")
    void whenCreateManagement_thenReturnSavedManagement() {
        // Arrange
        when(proyectRepository.findById(testProyect.getIdProyecto()))
                .thenReturn(Optional.of(testProyect));
        when(managementRepository.save(any(Management.class)))
                .thenReturn(testManagement1);

        // Act
        Management savedManagement = managementService.create(testManagement1);

        // Assert
        assertNotNull(savedManagement);
        assertEquals(testManagement1.getIdManagement(), savedManagement.getIdManagement());
        assertEquals(testManagement1.getNameManagement(), savedManagement.getNameManagement());
        assertEquals(testManagement1.getDescription(), savedManagement.getDescription());
        verify(managementRepository).save(any(Management.class));
    }

    @Test
    @DisplayName("Crear una nueva gestión con proyecto inválido lanza excepción")
    void whenCreateManagement_withInvalidProject_thenThrowRuntimeException() {
        // Arrange
        Proyect invalidProject = new Proyect();
        invalidProject.setIdProyecto(999L);

        Management invalidManagement = new Management();
        invalidManagement.setProyect(invalidProject);
        invalidManagement.setNameManagement("Gestión Inválida");

        when(proyectRepository.findById(999L))
                .thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                managementService.create(invalidManagement));

        assertTrue(exception.getMessage().contains("Proyect not found with id 999"));
        verify(managementRepository, never()).save(any(Management.class));
    }

    @Test
    @DisplayName("Obtener todas las gestiones")
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
    @DisplayName("Obtener gestión por ID existente")
    void whenGetById_thenReturnManagement() {
        // Arrange
        when(managementRepository.findById(1L))
                .thenReturn(Optional.of(testManagement1));

        // Act
        Management found = managementService.getById(1L);

        // Assert
        assertNotNull(found);
        assertEquals(testManagement1.getIdManagement(), found.getIdManagement());
        assertEquals(testManagement1.getNameManagement(), found.getNameManagement());
        verify(managementRepository).findById(1L);
    }

    @Test
    @DisplayName("Obtener gestión por ID inexistente lanza excepción")
    void whenGetByInvalidId_thenThrowRuntimeException() {
        // Arrange
        when(managementRepository.findById(999L))
                .thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                managementService.getById(999L));

        assertTrue(exception.getMessage().contains("Management not found with id 999"));
        verify(managementRepository).findById(999L);
    }

    @Test
    @DisplayName("Actualizar gestión existente con proyecto válido")
    void whenUpdateManagement_thenReturnUpdatedManagement() {
        // Arrange
        Long managementId = testManagement1.getIdManagement();
        when(managementRepository.findById(managementId))
                .thenReturn(Optional.of(testManagement1));
        when(proyectRepository.findById(testProyect.getIdProyecto()))
                .thenReturn(Optional.of(testProyect));
        when(managementRepository.save(any(Management.class)))
                .thenReturn(testManagement1);

        Management updatedDetails = new Management();
        updatedDetails.setNameManagement("Gestión Actualizada");
        updatedDetails.setDescription("Descripción actualizada");

        // Act
        Management updated = managementService.update(managementId, updatedDetails);

        // Assert
        assertNotNull(updated);
        assertEquals("Gestión Actualizada", updated.getNameManagement());
        assertEquals("Descripción actualizada", updated.getDescription());
        verify(managementRepository).save(any(Management.class));
    }

    @Test
    @DisplayName("Actualizar gestión con proyecto inválido lanza excepción")
    void whenUpdateManagement_withInvalidProject_thenThrowRuntimeException() {
        // Arrange
        Long managementId = testManagement1.getIdManagement();
        when(managementRepository.findById(managementId))
                .thenReturn(Optional.of(testManagement1));

        Proyect invalidProject = new Proyect();
        invalidProject.setIdProyecto(999L);

        Management managementDetails = new Management();
        managementDetails.setProyect(invalidProject);

        when(proyectRepository.findById(999L))
                .thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                managementService.update(managementId, managementDetails));

        assertTrue(exception.getMessage().contains("Proyect not found with id 999"));
        verify(managementRepository, never()).save(any(Management.class));
    }

    @Test
    @DisplayName("Eliminar gestión existente")
    void whenDeleteExistingManagement_thenVerifyDeleted() {
        // Arrange
        Long managementId = testManagement1.getIdManagement();
        when(managementRepository.existsById(managementId)).thenReturn(true);
        doNothing().when(managementRepository).deleteById(managementId);

        // Act
        managementService.delete(managementId);

        // Assert
        verify(managementRepository).existsById(managementId);
        verify(managementRepository).deleteById(managementId);
    }

    @Test
    @DisplayName("Eliminar gestión inexistente lanza excepción")
    void whenDeleteNonExistingManagement_thenThrowRuntimeException() {
        // Arrange
        Long managementId = 999L;
        when(managementRepository.existsById(managementId)).thenReturn(false);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                managementService.delete(managementId));

        assertTrue(exception.getMessage().contains("Management not found with id 999"));
        verify(managementRepository, never()).deleteById(anyLong());
    }
}