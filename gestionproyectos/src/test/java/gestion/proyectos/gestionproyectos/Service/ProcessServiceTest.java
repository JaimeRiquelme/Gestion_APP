package gestion.proyectos.gestionproyectos.Service;

import gestion.proyectos.gestionproyectos.Entity.Process;
import gestion.proyectos.gestionproyectos.Entity.Management;
import gestion.proyectos.gestionproyectos.Repository.ProcessRepository;
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
    @DisplayName("Test: Guardar proceso nuevo")
    void whenSaveProcess_thenReturnSavedProcess() {
        // Arrange
        when(processRepository.save(any(Process.class))).thenReturn(testProcess1);

        // Act
        Process savedProcess = processService.create(testProcess1);

        // Assert
        assertNotNull(savedProcess);
        assertEquals(testProcess1.getIdProcess(), savedProcess.getIdProcess());
        assertEquals(testProcess1.getNameProcess(), savedProcess.getNameProcess());
        assertEquals(testProcess1.getStateProcess(), savedProcess.getStateProcess());
        assertEquals(testProcess1.getStartDatePlanned(), savedProcess.getStartDatePlanned());
        assertEquals(testProcess1.getEndDatePlanned(), savedProcess.getEndDatePlanned());
        verify(processRepository).save(testProcess1);
    }

    @Test
    @DisplayName("Test: Obtener proceso por ID existente")
    void whenGetProcess_thenReturnProcess() {
        // Arrange
        when(processRepository.findById(1L)).thenReturn(Optional.of(testProcess1));

        // Act
        Process found = processService.getById(1L);

        // Assert
        assertNotNull(found);
        assertEquals(testProcess1.getIdProcess(), found.getIdProcess());
        assertEquals(testProcess1.getNameProcess(), found.getNameProcess());
        assertEquals(testProcess1.getStateProcess(), found.getStateProcess());
        verify(processRepository).findById(1L);
    }

    @Test
    @DisplayName("Test: Obtener proceso por ID inexistente")
    void whenGetProcessByInvalidId_thenReturnNull() {
        // Arrange
        when(processRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        Process result = processService.getById(999L);

        // Assert
        assertNull(result);
        verify(processRepository).findById(999L);
    }

    @Test
    @DisplayName("Test: Obtener todos los procesos")
    void whenGetProcesses_thenReturnProcessList() {
        // Arrange
        List<Process> processList = Arrays.asList(testProcess1, testProcess2);
        when(processRepository.findAll()).thenReturn(processList);

        // Act
        List<Process> result = processService.getAll();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(testProcess1.getNameProcess(), result.get(0).getNameProcess());
        assertEquals(testProcess2.getNameProcess(), result.get(1).getNameProcess());
        verify(processRepository).findAll();
    }

    @Test
    @DisplayName("Test: Actualizar proceso existente")
    void whenUpdateProcess_thenReturnUpdatedProcess() {
        // Arrange
        Process processToUpdate = testProcess1;
        processToUpdate.setNameProcess("Proceso Actualizado");
        processToUpdate.setStateProcess("Completado");
        processToUpdate.setEndDateReal("2024-06-15");
        when(processRepository.save(any(Process.class))).thenReturn(processToUpdate);

        // Act
        Process updated = processService.update(processToUpdate.getIdProcess(), processToUpdate);

        // Assert
        assertNotNull(updated);
        assertEquals("Proceso Actualizado", updated.getNameProcess());
        assertEquals("Completado", updated.getStateProcess());
        assertEquals("2024-06-15", updated.getEndDateReal());
        assertEquals(testProcess1.getIdProcess(), updated.getIdProcess());
        verify(processRepository).save(processToUpdate);
    }

    @Test
    @DisplayName("Test: Eliminar proceso")
    void whenDeleteProcess_thenVerifyDeletion() {
        // Arrange
        Long processId = 1L;
        doNothing().when(processRepository).deleteById(processId);

        // Act
        processService.delete(processId);

        // Assert
        verify(processRepository, times(1)).deleteById(processId);
    }
}