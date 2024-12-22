package gestion.proyectos.gestionproyectos.Service;

import gestion.proyectos.gestionproyectos.Entity.Exit;
import gestion.proyectos.gestionproyectos.Repository.ExitRepository;
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

    @InjectMocks
    private ExitService exitService;

    private Exit testExit;

    @BeforeEach
    void setUp() {
        testExit = new Exit();
        testExit.setIdExit(1L);
        testExit.setNameExit("Test Exit");
        testExit.setState("Active");
        testExit.setPriority("High");
        testExit.setResponsible("John Doe");
        testExit.setDescription("Test Description");
    }

    @Test
    void whenSaveExit_thenReturnSavedExit() {
        // Given
        when(exitRepository.save(any(Exit.class))).thenReturn(testExit);

        // When
        Exit savedExit = exitService.create(testExit);

        // Then
        assertNotNull(savedExit);
        assertEquals(testExit.getNameExit(), savedExit.getNameExit());
        verify(exitRepository).save(any(Exit.class));
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
    void whenGetByIdWithInvalidId_thenReturnNull() {
        // Given
        when(exitRepository.findById(99L)).thenReturn(Optional.empty());

        // When
        Exit foundExit = exitService.getById(99L);

        // Then
        assertNull(foundExit);
        verify(exitRepository).findById(99L);
    }

    @Test
    void whenUpdateExit_thenReturnUpdatedExit() {
        // Given
        Exit exitToUpdate = testExit;
        exitToUpdate.setNameExit("Updated Name");
        when(exitRepository.save(any(Exit.class))).thenReturn(exitToUpdate);

        // When
        Exit updatedExit = exitService.update(exitToUpdate.getIdProcess(), exitToUpdate);

        // Then
        assertNotNull(updatedExit);
        assertEquals("Updated Name", updatedExit.getNameExit());
        verify(exitRepository).save(any(Exit.class));
    }

    @Test
    void whenDeleteExistingExit_thenReturnTrue() throws Exception {
        // Given
        when(exitRepository.findById(1L)).thenReturn(Optional.of(testExit));
        doNothing().when(exitRepository).deleteById(1L);

        // When
        exitService.delete(1L);

        // Then
        verify(exitRepository).findById(1L);
        verify(exitRepository).deleteById(1L);
    }

    @Test
    void whenDeleteNonExistingExit_thenThrowException() {
        // Given
        when(exitRepository.findById(99L)).thenReturn(Optional.empty());

        // When & Then
        Exception exception = assertThrows(Exception.class, () -> {
            exitService.delete(99L);
        });

        assertEquals("Exit 99 does not exist", exception.getMessage());
        verify(exitRepository).findById(99L);
        verify(exitRepository, never()).deleteById(any());
    }
}