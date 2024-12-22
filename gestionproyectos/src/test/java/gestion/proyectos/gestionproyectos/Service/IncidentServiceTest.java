package gestion.proyectos.gestionproyectos.Service;

import gestion.proyectos.gestionproyectos.Entity.Incident;
import gestion.proyectos.gestionproyectos.Entity.Proyect;
import gestion.proyectos.gestionproyectos.Entity.User;
import gestion.proyectos.gestionproyectos.Repository.IncidentRepository;
import org.junit.jupiter.api.BeforeEach;
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
public class IncidentServiceTest {

    @Mock
    private IncidentRepository incidentRepository;

    @InjectMocks
    private IncidentService incidentService;

    private Incident testIncident;
    private Proyect testProyect;
    private User testUser;

    @BeforeEach
    void setUp() {
        // Configurar User para pruebas
        testUser = new User();
        testUser.setIdUsuario(1L);
        testUser.setNames("Test User");
        testUser.setEmail("test@example.com");

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
    void whenSaveIncident_thenReturnSavedIncident() {
        // Given
        when(incidentRepository.save(any(Incident.class))).thenReturn(testIncident);

        // When
        Incident savedIncident = incidentService.create(testIncident);

        // Then
        assertNotNull(savedIncident);
        assertEquals(testIncident.getDescription(), savedIncident.getDescription());
        assertEquals(testIncident.getState(), savedIncident.getState());
        assertEquals(testIncident.getPriority(), savedIncident.getPriority());
        assertEquals(testIncident.getRegistrationDate(), savedIncident.getRegistrationDate());
        verify(incidentRepository).save(any(Incident.class));
    }

    @Test
    void whenGetIncidentById_thenReturnIncident() {
        // Given
        when(incidentRepository.findById(1L)).thenReturn(Optional.of(testIncident));

        // When
        Incident foundIncident = incidentService.getById(1L);

        // Then
        assertNotNull(foundIncident);
        assertEquals(testIncident.getIdIncident(), foundIncident.getIdIncident());
        assertEquals(testIncident.getDescription(), foundIncident.getDescription());
        verify(incidentRepository).findById(1L);
    }

    @Test
    void whenGetIncidentByIdWithInvalidId_thenReturnNull() {
        // Given
        when(incidentRepository.findById(99L)).thenReturn(Optional.empty());

        // When
        Incident foundIncident = incidentService.getById(99L);

        // Then
        assertNull(foundIncident);
        verify(incidentRepository).findById(99L);
    }

    @Test
    void whenGetAllIncidents_thenReturnList() {
        // Given
        List<Incident> incidents = Arrays.asList(testIncident);
        when(incidentRepository.findAll()).thenReturn(incidents);

        // When
        List<Incident> foundIncidents = incidentService.getAll();

        // Then
        assertNotNull(foundIncidents);
        assertFalse(foundIncidents.isEmpty());
        assertEquals(1, foundIncidents.size());
        assertEquals(testIncident.getDescription(), foundIncidents.get(0).getDescription());
        verify(incidentRepository).findAll();
    }

    @Test
    void whenUpdateIncident_thenReturnUpdatedIncident() {
        // Given
        String updatedDescription = "Updated Description";
        String updatedState = "Resolved";
        testIncident.setDescription(updatedDescription);
        testIncident.setState(updatedState);

        when(incidentRepository.save(any(Incident.class))).thenReturn(testIncident);

        // When
        Incident updatedIncident = incidentService.update(testIncident.getIdIncident(), testIncident);

        // Then
        assertNotNull(updatedIncident);
        assertEquals(updatedDescription, updatedIncident.getDescription());
        assertEquals(updatedState, updatedIncident.getState());
        verify(incidentRepository).save(any(Incident.class));
    }

    @Test
    void whenDeleteIncident_thenVerifyRepositoryCall() {
        // Given
        Long id = 1L;
        doNothing().when(incidentRepository).deleteById(id);

        // When
        incidentService.delete(id);

        // Then
        verify(incidentRepository).deleteById(id);
    }

    @Test
    void whenSaveIncidentWithNullProject_thenReturnSavedIncident() {
        // Given
        testIncident.setProyect(null);
        when(incidentRepository.save(any(Incident.class))).thenReturn(testIncident);

        // When
        Incident savedIncident = incidentService.create(testIncident);

        // Then
        assertNotNull(savedIncident);
        assertNull(savedIncident.getProyect());
        verify(incidentRepository).save(any(Incident.class));
    }

    @Test
    void whenSaveIncidentWithNullResponsible_thenReturnSavedIncident() {
        // Given
        testIncident.setResponsible(null);
        when(incidentRepository.save(any(Incident.class))).thenReturn(testIncident);

        // When
        Incident savedIncident = incidentService.create(testIncident);

        // Then
        assertNotNull(savedIncident);
        assertNull(savedIncident.getResponsible());
        verify(incidentRepository).save(any(Incident.class));
    }

    @Test
    void whenUpdateIncidentPriority_thenReturnUpdatedIncident() {
        // Given
        String newPriority = "Low";
        testIncident.setPriority(newPriority);

        when(incidentRepository.save(any(Incident.class))).thenReturn(testIncident);

        // When
        Incident updatedIncident = incidentService.update(testIncident.getIdIncident(), testIncident);

        // Then
        assertNotNull(updatedIncident);
        assertEquals(newPriority, updatedIncident.getPriority());
        verify(incidentRepository).save(any(Incident.class));
    }
}