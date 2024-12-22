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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ParameterServiceTest {

    @Mock
    private ParameterRepository parameterRepository;

    @InjectMocks
    private ParameterService parameterService;

    private Parameter testParameter;
    private Exit testExit;

    @BeforeEach
    void setUp() {
        // Configurar Exit para pruebas
        testExit = new Exit();
        testExit.setIdExit(1L);
        testExit.setNameExit("Test Exit");

        // Configurar Parameter para pruebas
        testParameter = new Parameter();
        testParameter.setIdParameter(1L);
        testParameter.setNameParameter("Test Parameter");
        testParameter.setContent("Test Content");
        testParameter.setState("Active");
        testParameter.setExit(testExit);
    }

    @Test
    void whenSaveParameter_thenReturnSavedParameter() {
        // Given
        when(parameterRepository.save(any(Parameter.class))).thenReturn(testParameter);

        // When
        Parameter savedParameter = parameterService.create(testParameter);

        // Then
        assertNotNull(savedParameter);
        assertEquals(testParameter.getNameParameter(), savedParameter.getNameParameter());
        assertEquals(testParameter.getContent(), savedParameter.getContent());
        assertEquals(testParameter.getState(), savedParameter.getState());
        verify(parameterRepository).save(any(Parameter.class));
    }

    @Test
    void whenGetAll_thenReturnParameterList() {
        // Given
        List<Parameter> parameters = Arrays.asList(testParameter);
        when(parameterRepository.findAll()).thenReturn(parameters);

        // When
        List<Parameter> foundParameters = parameterService.getAll();

        // Then
        assertNotNull(foundParameters);
        assertEquals(1, foundParameters.size());
        assertEquals(testParameter.getNameParameter(), foundParameters.get(0).getNameParameter());
        verify(parameterRepository).findAll();
    }

    @Test
    void whenGetById_thenReturnParameter() {
        // Given
        when(parameterRepository.findById(1L)).thenReturn(Optional.of(testParameter));

        // When
        Parameter foundParameter = parameterService.getById(1L);

        // Then
        assertNotNull(foundParameter);
        assertEquals(testParameter.getIdParameter(), foundParameter.getIdParameter());
        assertEquals(testParameter.getNameParameter(), foundParameter.getNameParameter());
        verify(parameterRepository).findById(1L);
    }

    @Test
    void whenGetByIdWithInvalidId_thenReturnNull() {
        // Given
        when(parameterRepository.findById(99L)).thenReturn(Optional.empty());

        // When
        Parameter foundParameter = parameterService.getById(99L);

        // Then
        assertNull(foundParameter);
        verify(parameterRepository).findById(99L);
    }

    @Test
    void whenUpdateParameter_thenReturnUpdatedParameter() {
        // Given
        Parameter parameterToUpdate = testParameter;
        parameterToUpdate.setContent("Updated Content");
        when(parameterRepository.save(any(Parameter.class))).thenReturn(parameterToUpdate);

        // When
        Parameter updatedParameter = parameterService.update(parameterToUpdate.getIdParameter(), parameterToUpdate);

        // Then
        assertNotNull(updatedParameter);
        assertEquals("Updated Content", updatedParameter.getContent());
        verify(parameterRepository).save(any(Parameter.class));
    }

    @Test
    void whenDeleteExistingParameter_thenReturnTrue() throws Exception {
        // Given
        when(parameterRepository.findById(1L)).thenReturn(Optional.of(testParameter));
        doNothing().when(parameterRepository).deleteById(1L);

        // When
        parameterService.delete(1L);

        // Then
        verify(parameterRepository).findById(1L);
        verify(parameterRepository).deleteById(1L);
    }

    @Test
    void whenDeleteNonExistingParameter_thenThrowException() {
        // Given
        when(parameterRepository.findById(99L)).thenReturn(Optional.empty());

        // When & Then
        Exception exception = assertThrows(Exception.class, () -> {
            parameterService.delete(99L);
        });

        assertEquals("Parameter with id 99 not found", exception.getMessage());
        verify(parameterRepository).findById(99L);
        verify(parameterRepository, never()).deleteById(any());
    }

    @Test
    void whenSaveParameterWithExit_thenReturnSavedParameterWithExit() {
        // Given
        when(parameterRepository.save(any(Parameter.class))).thenReturn(testParameter);

        // When
        Parameter savedParameter = parameterService.create(testParameter);

        // Then
        assertNotNull(savedParameter);
        assertNotNull(savedParameter.getExit());
        assertEquals(testExit.getIdExit(), savedParameter.getExit().getIdExit());
        assertEquals(testExit.getNameExit(), savedParameter.getExit().getNameExit());
        verify(parameterRepository).save(any(Parameter.class));
    }
}