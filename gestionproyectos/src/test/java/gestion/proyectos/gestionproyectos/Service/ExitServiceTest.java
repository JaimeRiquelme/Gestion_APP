package gestion.proyectos.gestionproyectos.Service;

import gestion.proyectos.gestionproyectos.Entity.Exit;
import gestion.proyectos.gestionproyectos.Entity.Process;
import gestion.proyectos.gestionproyectos.Repository.ExitRepository;
import gestion.proyectos.gestionproyectos.Repository.ProcessRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExitServiceTest {

    @Mock
    private ExitRepository exitRepository;

    @Mock
    private ProcessRepository processRepository;

    @InjectMocks
    private ExitService exitService;

    private Exit testExit;
    private Process testProcess;

    @BeforeEach
    void setUp() {
        testProcess = new Process();
        testProcess.setIdProcess(1L);

        testExit = new Exit();
        testExit.setIdExit(1L);
        testExit.setNameExit("Test Exit");
        testExit.setState("Active");
        testExit.setPriority("High");
        testExit.setResponsible("John Doe");
        testExit.setDescription("Test Description");
        testExit.setDateCreation("2024-01-01");
        testExit.setDateValidation("2024-12-31");
        testExit.setProcess(testProcess);
    }

    @Test
    void whenCreateExit_withValidData_thenReturnSavedExit() {
        // Given
        when(processRepository.findById(1L)).thenReturn(Optional.of(testProcess));
        when(exitRepository.save(any(Exit.class))).thenReturn(testExit);

        // When
        Exit savedExit = exitService.create(testExit);

        // Then
        assertNotNull(savedExit);
        assertEquals(testExit.getNameExit(), savedExit.getNameExit());
        verify(exitRepository).save(any(Exit.class));
    }

    @Test
    void whenCreateExit_withInvalidDate_thenThrowException() {
        // Given
        when(processRepository.findById(1L)).thenReturn(Optional.of(testProcess));
        testExit.setDateCreation("invalid-date");

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> exitService.create(testExit));
    }

    @Test
    void whenGetAll_thenReturnExitList() {
        // Given
        List<Exit> exits = Arrays.asList(testExit);
        when(exitRepository.findAll()).thenReturn(exits);

        // When
        List<Exit> foundExits = exitService.getAll();

        // Then
        assertNotNull(foundExits);
        assertEquals(1, foundExits.size());
        verify(exitRepository).findAll();
    }

    @Test
    void whenGetById_thenReturnExit() {
        // Given
        when(exitRepository.findById(1L)).thenReturn(Optional.of(testExit));

        // When
        Exit foundExit = exitService.getById(1L);

        // Then
        assertNotNull(foundExit);
        assertEquals(testExit.getIdExit(), foundExit.getIdExit());
        verify(exitRepository).findById(1L);
    }

    @Test
    void whenUpdateExit_withValidData_thenReturnUpdatedExit() {
        // Given
        Exit exitToUpdate = new Exit();
        exitToUpdate.setNameExit("Updated Name");
        exitToUpdate.setProcess(testProcess);

        when(exitRepository.findById(1L)).thenReturn(Optional.of(testExit));
        when(processRepository.findById(1L)).thenReturn(Optional.of(testProcess));
        when(exitRepository.save(any(Exit.class))).thenReturn(exitToUpdate);

        // When
        Exit updatedExit = exitService.update(1L, exitToUpdate);

        // Then
        assertNotNull(updatedExit);
        assertEquals("Updated Name", updatedExit.getNameExit());
        verify(exitRepository).save(any(Exit.class));
    }

    @Test
    void whenDelete_withExistingExit_thenDeleteSuccessfully() {
        // Given
        when(exitRepository.existsById(1L)).thenReturn(true);
        doNothing().when(exitRepository).deleteById(1L);

        // When & Then
        assertDoesNotThrow(() -> exitService.delete(1L));
        verify(exitRepository).deleteById(1L);
    }

    @Test
    void whenDelete_withNonExistingExit_thenThrowException() {
        // Given
        when(exitRepository.existsById(99L)).thenReturn(false);

        // When & Then
        assertThrows(RuntimeException.class, () -> exitService.delete(99L),
                "Exit not found with id 99");
        verify(exitRepository, never()).deleteById(any());
    }

    @Test
    void whenUpdateAssumptionsDocument_withValidExit_thenUpdateSuccessfully() {
        // Given
        byte[] document = "test document".getBytes();
        when(exitRepository.findById(1L)).thenReturn(Optional.of(testExit));
        when(exitRepository.save(any(Exit.class))).thenReturn(testExit);

        // When
        exitService.updateAssumptionsDocument(1L, document);

        // Then
        verify(exitRepository).save(any(Exit.class));
    }

    @Test
    void whenCreateExit_withInvalidProcess_thenThrowException() {
        // Given
        when(processRepository.findById(any())).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> exitService.create(testExit));
    }
}