package gestion.proyectos.gestionproyectos.Service;

import gestion.proyectos.gestionproyectos.Entity.Incident;
import gestion.proyectos.gestionproyectos.Entity.Proyect;
import gestion.proyectos.gestionproyectos.Entity.User;
import gestion.proyectos.gestionproyectos.Repository.IncidentRepository;
import gestion.proyectos.gestionproyectos.Repository.ProyectRepository;
import gestion.proyectos.gestionproyectos.Repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IncidentServiceTest {

    @Mock
    private IncidentRepository incidentRepository;

    @Mock
    private ProyectRepository proyectRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private IncidentService incidentService;

    private Incident testIncident;
    private Proyect testProyect;
    private User testUser;

    @BeforeEach
    void setUp() {
        // Configurar User para pruebas
        testUser = User.builder()
                .idUsuario(1L)
                .names("Test User")
                .email("test@example.com")
                .build();

        // Configurar Proyect para pruebas
        testProyect = new Proyect();
        testProyect.setIdProyecto(1L);
        testProyect.setNameProyect("Test Project");
        testProyect.setDescription("Test Project Description");
        testProyect.setOrganization("Test Organization");
        testProyect.setStartDate(LocalDate.now().toString());
        testProyect.setEstimatedEndDate(LocalDate.now().plusMonths(1).toString());
        testProyect.setUser(testUser);

        // Configurar Incident para pruebas
        testIncident = new Incident();
        testIncident.setIdIncident(1L);
        testIncident.setDescription("Test Incident Description");
        testIncident.setState("Active");
        testIncident.setPriority("High");
        testIncident.setRegistrationDate(LocalDate.now().toString());
        testIncident.setResponsible(testUser);
        testIncident.setProyect(testProyect);
    }

    @Test
    @DisplayName("Guardar incidente nuevo")
    void whenSaveIncident_thenReturnSavedIncident() {
        // Arrange
        when(proyectRepository.findById(testProyect.getIdProyecto())).thenReturn(Optional.of(testProyect));
        when(userRepository.findById(testUser.getIdUsuario())).thenReturn(Optional.of(testUser));
        when(incidentRepository.save(any(Incident.class))).thenReturn(testIncident);

        // Act
        Incident savedIncident = incidentService.create(testIncident);

        // Assert
        assertNotNull(savedIncident);
        assertEquals(testIncident.getDescription(), savedIncident.getDescription());
        assertEquals(testIncident.getState(), savedIncident.getState());
        assertEquals(testIncident.getPriority(), savedIncident.getPriority());
        assertEquals(testIncident.getRegistrationDate(), savedIncident.getRegistrationDate());
        verify(incidentRepository).save(any(Incident.class));
    }

    @Test
    @DisplayName("Guardar incidente con proyecto inválido lanza excepción")
    void whenSaveIncidentWithInvalidProyect_thenThrowException() {
        // Arrange
        when(proyectRepository.findById(testProyect.getIdProyecto())).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            incidentService.create(testIncident);
        });

        assertEquals("Proyect not found with id " + testProyect.getIdProyecto(), exception.getMessage());
        verify(incidentRepository, never()).save(any(Incident.class));
    }

    @Test
    @DisplayName("Guardar incidente con usuario inválido lanza excepción")
    void whenSaveIncidentWithInvalidUser_thenThrowException() {
        // Arrange
        when(proyectRepository.findById(testProyect.getIdProyecto())).thenReturn(Optional.of(testProyect));
        when(userRepository.findById(testUser.getIdUsuario())).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            incidentService.create(testIncident);
        });

        assertEquals("User not found with id " + testUser.getIdUsuario(), exception.getMessage());
        verify(incidentRepository, never()).save(any(Incident.class));
    }

    @Test
    @DisplayName("Obtener todos los incidentes")
    void whenGetAllIncidents_thenReturnIncidentList() {
        // Arrange
        List<Incident> incidentList = Arrays.asList(testIncident);
        when(incidentRepository.findAll()).thenReturn(incidentList);

        // Act
        List<Incident> result = incidentService.getAll();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testIncident.getDescription(), result.get(0).getDescription());
        verify(incidentRepository).findAll();
    }

    @Test
    @DisplayName("Obtener incidente por ID existente")
    void whenGetIncidentById_thenReturnIncident() {
        // Arrange
        when(incidentRepository.findById(1L)).thenReturn(Optional.of(testIncident));

        // Act
        Incident found = incidentService.getById(1L);

        // Assert
        assertNotNull(found);
        assertEquals(testIncident.getIdIncident(), found.getIdIncident());
        assertEquals(testIncident.getDescription(), found.getDescription());
        verify(incidentRepository).findById(1L);
    }

    @Test
    @DisplayName("Obtener incidente por ID inexistente")
    void whenGetIncidentByInvalidId_thenThrowException() {
        // Arrange
        when(incidentRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            incidentService.getById(999L);
        });

        assertEquals("Incident not found with id 999", exception.getMessage());
        verify(incidentRepository).findById(999L);
    }

    @Test
    @DisplayName("Actualizar incidente existente")
    void whenUpdateIncident_thenReturnUpdatedIncident() {
        // Arrange
        String updatedDescription = "Updated Incident Description";
        String updatedState = "Resolved";
        Incident updatedIncidentDetails = new Incident();
        updatedIncidentDetails.setDescription(updatedDescription);
        updatedIncidentDetails.setState(updatedState);

        when(incidentRepository.findById(1L)).thenReturn(Optional.of(testIncident));
        when(incidentRepository.save(any(Incident.class))).thenReturn(testIncident);

        // Act
        Incident updatedIncident = incidentService.update(1L, updatedIncidentDetails);

        // Assert
        assertNotNull(updatedIncident);
        assertEquals(updatedDescription, updatedIncident.getDescription());
        assertEquals(updatedState, updatedIncident.getState());
        verify(incidentRepository).save(any(Incident.class));
    }

    @Test
    @DisplayName("Actualizar incidente inexistente lanza excepción")
    void whenUpdateNonExistentIncident_thenThrowException() {
        // Arrange
        Incident updatedIncidentDetails = new Incident();
        updatedIncidentDetails.setDescription("Updated Incident Description");
        updatedIncidentDetails.setState("Resolved");

        when(incidentRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            incidentService.update(999L, updatedIncidentDetails);
        });

        assertEquals("Incident not found with id 999", exception.getMessage());
        verify(incidentRepository, never()).save(any(Incident.class));
    }

    @Test
    @DisplayName("Eliminar incidente existente")
    void whenDeleteIncident_thenVerifyDeletion() {
        // Arrange
        Long incidentId = 1L;
        when(incidentRepository.existsById(incidentId)).thenReturn(true);

        // Act
        incidentService.delete(incidentId);

        // Assert
        verify(incidentRepository, times(1)).deleteById(incidentId);
    }

    @Test
    @DisplayName("Eliminar incidente inexistente lanza excepción")
    void whenDeleteNonExistentIncident_thenThrowException() {
        // Arrange
        Long incidentId = 999L;
        when(incidentRepository.existsById(incidentId)).thenReturn(false);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            incidentService.delete(incidentId);
        });

        assertEquals("Incident not found with id 999", exception.getMessage());
        verify(incidentRepository, never()).deleteById(incidentId);
    }

}