package gestion.proyectos.gestionproyectos.Service;

import gestion.proyectos.gestionproyectos.Entity.Exit;
import gestion.proyectos.gestionproyectos.Entity.Parameter;
import gestion.proyectos.gestionproyectos.Repository.ParameterRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ParameterServiceTest {

    @Mock
    private ParameterRepository parameterRepository;

    @Mock
    private ExitService exitService;

    @InjectMocks
    private ParameterService parameterService;

    private Parameter testParameter;
    private Exit testExit;

    @BeforeEach
    void setUp() {
        // Configure an Exit for testing
        testExit = new Exit();
        testExit.setIdExit(1L);
        testExit.setNameExit("Test Exit");

        // Configure a Parameter for testing
        testParameter = new Parameter();
        testParameter.setIdParameter(1L);
        testParameter.setNameParameter("Test Parameter");
        testParameter.setContent("Test Content");
        testParameter.setState("Active");
        testParameter.setExit(testExit);
    }

    @Test
    void whenCreateParameter_thenReturnSavedParameter() {
        // Given: the Exit is found and is valid
        when(exitService.getById(testExit.getIdExit())).thenReturn(testExit);
        when(parameterRepository.save(any(Parameter.class))).thenReturn(testParameter);

        // When: creating the parameter
        Parameter savedParameter = parameterService.create(testParameter);

        // Then: ensure the parameter is saved and has the correct data
        assertNotNull(savedParameter);
        assertEquals(testParameter.getNameParameter(), savedParameter.getNameParameter());
        assertEquals(testParameter.getContent(), savedParameter.getContent());
        assertEquals(testParameter.getState(), savedParameter.getState());
        verify(parameterRepository).save(any(Parameter.class));
    }

    @Test
    void whenCreateParameter_withInvalidExit_thenThrowException() {
        // Given: the Exit does not exist in the system
        Long invalidExitId = 999L;
        testParameter.setIdExit(invalidExitId);
        when(exitService.getById(invalidExitId)).thenReturn(null);

        // When & Then: creating a parameter with a non-existent Exit
        RuntimeException exception = assertThrows(RuntimeException.class, () -> parameterService.create(testParameter));
        assertTrue(exception.getMessage().contains("Exit not found with id 999"));
        verify(parameterRepository, never()).save(any(Parameter.class));
    }

    @Test
    void whenGetAll_thenReturnParameterList() {
        // Given: we return a single testParameter
        List<Parameter> parameters = Arrays.asList(testParameter);
        when(parameterRepository.findAllParameters()).thenReturn(parameters);

        // When: retrieving all parameters
        List<Parameter> foundParameters = parameterService.getAll();

        // Then: verify the repository method was called and the result is correct
        assertNotNull(foundParameters);
        assertEquals(1, foundParameters.size());
        assertEquals(testParameter.getNameParameter(), foundParameters.get(0).getNameParameter());
        verify(parameterRepository).findAllParameters();
    }

    @Test
    void whenGetById_thenReturnParameter() {
        // Given: the parameter is found by ID
        when(parameterRepository.findById(1L)).thenReturn(Optional.of(testParameter));

        // When: retrieving by ID
        Parameter foundParameter = parameterService.getById(1L);

        // Then: the returned parameter is correct
        assertNotNull(foundParameter);
        assertEquals(testParameter.getIdParameter(), foundParameter.getIdParameter());
        assertEquals(testParameter.getNameParameter(), foundParameter.getNameParameter());
        verify(parameterRepository).findById(1L);
    }

    @Test
    void whenGetByIdWithInvalidId_thenThrowRuntimeException() {
        // Given: no parameter found for this ID
        when(parameterRepository.findById(99L)).thenReturn(Optional.empty());

        // When & Then: trying to get a non-existent parameter should throw
        RuntimeException exception = assertThrows(RuntimeException.class, () -> parameterService.getById(99L));
        assertTrue(exception.getMessage().contains("Parameter not found with id 99"));
        verify(parameterRepository).findById(99L);
    }

    @Test
    void whenUpdateParameter_thenReturnUpdatedParameter() {
        // Given: the existing parameter is found, and we have new content
        Long existingId = testParameter.getIdParameter();
        when(parameterRepository.findById(existingId)).thenReturn(Optional.of(testParameter));
        when(parameterRepository.save(any(Parameter.class))).thenReturn(testParameter);

        Parameter parameterToUpdate = new Parameter();
        parameterToUpdate.setContent("Updated Content");
        parameterToUpdate.setState("Updated State");

        // When: updating the parameter
        Parameter updatedParameter = parameterService.update(existingId, parameterToUpdate);

        // Then: ensure the content and state have changed
        assertNotNull(updatedParameter);
        assertEquals("Updated Content", updatedParameter.getContent());
        assertEquals("Updated State", updatedParameter.getState());
        verify(parameterRepository).save(any(Parameter.class));
    }

    @Test
    void whenUpdateParameter_withInvalidExit_thenThrowException() {
        // Given: the parameter we want to update is found
        Long existingId = testParameter.getIdParameter();
        when(parameterRepository.findById(existingId)).thenReturn(Optional.of(testParameter));

        // The new parameter details point to an invalid exit
        Long invalidExitId = 999L;
        Parameter parameterDetails = new Parameter();
        parameterDetails.setIdExit(invalidExitId);
        when(exitService.getById(invalidExitId)).thenReturn(null);

        // When & Then: updating with an invalid Exit should fail
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                parameterService.update(existingId, parameterDetails)
        );
        assertTrue(exception.getMessage().contains("Exit not found with id 999"));
        verify(parameterRepository, never()).save(any(Parameter.class));
    }

    @Test
    void whenDeleteExistingParameter_thenVerifyDeleted() {
        // Given: the parameter with ID=1 exists in the repository
        when(parameterRepository.existsById(1L)).thenReturn(true);

        // When: deleting the parameter
        parameterService.delete(1L);

        // Then: verify the deleteById method was indeed called
        verify(parameterRepository).existsById(1L);
        verify(parameterRepository).deleteById(1L);
    }

    @Test
    void whenDeleteNonExistingParameter_thenThrowRuntimeException() {
        // Given: the parameter with ID=99 does not exist
        when(parameterRepository.existsById(99L)).thenReturn(false);

        // When & Then: attempting to delete a non-existent parameter
        RuntimeException exception = assertThrows(RuntimeException.class, () -> parameterService.delete(99L));
        assertTrue(exception.getMessage().contains("Parameter not found with id 99"));
        verify(parameterRepository, never()).deleteById(anyLong());
    }

    @Test
    void whenSaveParameterWithExit_thenReturnSavedParameterWithExit() {
        // Given: valid exit and parameter
        when(exitService.getById(testExit.getIdExit())).thenReturn(testExit);
        when(parameterRepository.save(any(Parameter.class))).thenReturn(testParameter);

        // When: creating the parameter
        Parameter savedParameter = parameterService.create(testParameter);

        // Then: the saved parameter should correctly reference the exit
        assertNotNull(savedParameter);
        assertNotNull(savedParameter.getExit());
        assertEquals(testExit.getIdExit(), savedParameter.getExit().getIdExit());
        verify(parameterRepository).save(any(Parameter.class));
    }

    @Test
    void whenSaveParameters_withExistingAndNewParameters_thenSuccessfullyCreatedOrUpdated() {
        // Given: exit exists in the system
        Long exitId = 1L;
        when(exitService.getById(exitId)).thenReturn(testExit);

        // Map of parameter data (one existing, one new)
        Map<String, String> data = Map.of(
                "ExistingParameter", "Updated Content",
                "NewParameter", "New Content"
        );

        // The existing parameter in the database
        Parameter existingParameter = new Parameter();
        existingParameter.setIdParameter(2L);
        existingParameter.setNameParameter("ExistingParameter");
        existingParameter.setContent("Old Content");
        existingParameter.setState("CREADO");
        existingParameter.setExit(testExit);

        // Return the existing parameter for one name, null for a new parameter
        when(parameterRepository.findByNameParameter("ExistingParameter")).thenReturn(existingParameter);
        when(parameterRepository.findByNameParameter("NewParameter")).thenReturn(null);

        // When: saving parameters
        parameterService.saveParameters(data, exitId);

        // Then: existing parameter is updated, new parameter is created
        assertEquals("Updated Content", existingParameter.getContent());
        assertEquals("ACTUALIZADO", existingParameter.getState());

        // Verify we do two saves: one for the update, one for the new parameter
        verify(parameterRepository, times(2)).save(any(Parameter.class));
    }
}