package gestion.proyectos.gestionproyectos.Service;

import gestion.proyectos.gestionproyectos.Entity.Process;
import gestion.proyectos.gestionproyectos.Entity.Management;
import gestion.proyectos.gestionproyectos.Repository.ProcessRepository;
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
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProcessServiceTest {

    @Mock
    private ProcessRepository processRepository;

    @Mock
    private ManagementRepository managementRepository;

    @InjectMocks
    private ProcessService processService;

    private Process testProcess1;
    private Process testProcess2;
    private Management testManagement;

    @BeforeEach
    void setUp() {
        // Configurar gestión de prueba
        testManagement = new Management();
        testManagement.setIdManagement(1L);
        testManagement.setNameManagement("Gestión Test");

        // Configurar proceso de prueba 1
        testProcess1 = new Process();
        testProcess1.setIdProcess(1L);
        testProcess1.setManagement(testManagement);
        testProcess1.setNameProcess("Proceso 1");
        testProcess1.setDescription("Descripción del proceso 1");
        testProcess1.setStateProcess("En progreso");
        testProcess1.setStartDatePlanned("2024-01-01");
        testProcess1.setEndDatePlanned("2024-06-30");
        testProcess1.setStartDateReal("2024-01-15");
        testProcess1.setEndDateReal(null);
        testProcess1.setExits(new ArrayList<>());

        // Configurar proceso de prueba 2
        testProcess2 = new Process();
        testProcess2.setIdProcess(2L);
        testProcess2.setManagement(testManagement);
        testProcess2.setNameProcess("Proceso 2");
        testProcess2.setDescription("Descripción del proceso 2");
        testProcess2.setStateProcess("Planificado");
        testProcess2.setStartDatePlanned("2024-07-01");
        testProcess2.setEndDatePlanned("2024-12-31");
        testProcess2.setStartDateReal(null);
        testProcess2.setEndDateReal(null);
        testProcess2.setExits(new ArrayList<>());
    }

    @Test
    @DisplayName("Crear proceso con datos válidos")
    void whenCreateProcess_thenReturnSavedProcess() {
        // Arrange
        when(managementRepository.findById(testManagement.getIdManagement())).thenReturn(Optional.of(testManagement));
        when(processRepository.save(any(Process.class))).thenReturn(testProcess1);

        // Act
        Process savedProcess = processService.create(testProcess1);

        // Assert
        assertNotNull(savedProcess);
        assertEquals(testProcess1.getNameProcess(), savedProcess.getNameProcess());
        assertEquals(testProcess1.getStateProcess(), savedProcess.getStateProcess());
        verify(processRepository).save(any(Process.class));
    }

    @Test
    @DisplayName("Crear proceso con Management inválido lanza excepción")
    void whenCreateProcessWithInvalidManagement_thenThrowException() {
        // Arrange
        when(managementRepository.findById(testManagement.getIdManagement())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> processService.create(testProcess1),
                "Management not found with id " + testManagement.getIdManagement());
        verify(processRepository, never()).save(any(Process.class));
    }


    @Test
    @DisplayName("Obtener todos los procesos")
    void whenGetAllProcesses_thenReturnList() {
        // Arrange
        List<Process> processes = Arrays.asList(testProcess1, testProcess2);
        when(processRepository.findAll()).thenReturn(processes);

        // Act
        List<Process> result = processService.getAll();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(processRepository).findAll();
    }

    @Test
    @DisplayName("Obtener proceso por ID existente")
    void whenGetProcessById_thenReturnProcess() {
        // Arrange
        when(processRepository.findById(1L)).thenReturn(Optional.of(testProcess1));

        // Act
        Process found = processService.getById(1L);

        // Assert
        assertNotNull(found);
        assertEquals(testProcess1.getIdProcess(), found.getIdProcess());
        verify(processRepository).findById(1L);
    }

    @Test
    @DisplayName("Obtener proceso por ID inexistente lanza excepción")
    void whenGetProcessByInvalidId_thenThrowException() {
        // Arrange
        when(processRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> processService.getById(999L),
                "Process not found with id 999");
    }

    @Test
    @DisplayName("Actualizar proceso existente")
    void whenUpdateProcess_thenReturnUpdatedProcess() {
        // Arrange
        Process updateDetails = new Process();
        updateDetails.setNameProcess("Proceso Actualizado");
        updateDetails.setStateProcess("Completado");
        updateDetails.setStartDatePlanned("2024-02-01");

        when(processRepository.findById(1L)).thenReturn(Optional.of(testProcess1));
        when(processRepository.save(any(Process.class))).thenReturn(testProcess1);

        // Act
        Process updated = processService.update(1L, updateDetails);

        // Assert
        assertNotNull(updated);
        assertEquals("Proceso Actualizado", updated.getNameProcess());
        verify(processRepository).save(any(Process.class));
    }

    @Test
    @DisplayName("Actualizar proceso inexistente lanza excepción")
    void whenUpdateNonExistentProcess_thenThrowException() {
        // Arrange
        when(processRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class,
                () -> processService.update(999L, testProcess1),
                "Process not found with id 999");
        verify(processRepository, never()).save(any(Process.class));
    }

    @Test
    @DisplayName("Actualizar proceso con fecha inválida lanza excepción")
    void whenUpdateProcessWithInvalidDate_thenThrowException() {
        // Arrange
        when(processRepository.findById(1L)).thenReturn(Optional.of(testProcess1));
        Process updateDetails = new Process();
        updateDetails.setStartDatePlanned("fecha-invalida");

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> processService.update(1L, updateDetails),
                "La fecha debe estar en el formato AAAA-MM-DD y ser válida");
        verify(processRepository, never()).save(any(Process.class));
    }

    @Test
    @DisplayName("Eliminar proceso existente")
    void whenDeleteExistingProcess_thenDeleteSuccessfully() {
        // Arrange
        Long processId = 1L;
        when(processRepository.existsById(processId)).thenReturn(true);

        // Act
        processService.delete(processId);

        // Assert
        verify(processRepository).deleteById(processId);
    }

    @Test
    @DisplayName("Eliminar proceso inexistente lanza excepción")
    void whenDeleteNonExistentProcess_thenThrowException() {
        // Arrange
        Long processId = 999L;
        when(processRepository.existsById(processId)).thenReturn(false);

        // Act & Assert
        assertThrows(RuntimeException.class,
                () -> processService.delete(processId),
                "Process not found with id 999");
        verify(processRepository, never()).deleteById(processId);
    }

    @Test
    @DisplayName("Actualizar proceso con nuevo Management")
    void whenUpdateProcessWithNewManagement_thenReturnUpdatedProcess() {
        // Arrange
        Management newManagement = new Management();
        newManagement.setIdManagement(2L);
        Process updateDetails = new Process();
        updateDetails.setManagement(newManagement);

        when(processRepository.findById(1L)).thenReturn(Optional.of(testProcess1));
        when(managementRepository.findById(2L)).thenReturn(Optional.of(newManagement));
        when(processRepository.save(any(Process.class))).thenReturn(testProcess1);

        // Act
        Process updated = processService.update(1L, updateDetails);

        // Assert
        assertNotNull(updated);
        verify(managementRepository).findById(2L);
        verify(processRepository).save(any(Process.class));
    }

    @Test
    @DisplayName("Validar múltiples fechas en proceso")
    void whenValidateMultipleDates_thenNoExceptions() {
        // Arrange
        testProcess1.setStartDatePlanned("2024-01-01");
        testProcess1.setEndDatePlanned("2024-12-31");
        testProcess1.setStartDateReal("2024-01-15");
        testProcess1.setEndDateReal("2024-12-15");

        when(managementRepository.findById(testManagement.getIdManagement())).thenReturn(Optional.of(testManagement));
        when(processRepository.save(any(Process.class))).thenReturn(testProcess1);

        // Act
        Process savedProcess = processService.create(testProcess1);

        // Assert
        assertNotNull(savedProcess);
        assertEquals("2024-01-01", savedProcess.getStartDatePlanned());
        assertEquals("2024-12-31", savedProcess.getEndDatePlanned());
        assertEquals("2024-01-15", savedProcess.getStartDateReal());
        assertEquals("2024-12-15", savedProcess.getEndDateReal());
    }
}