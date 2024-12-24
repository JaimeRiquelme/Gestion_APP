package gestion.proyectos.gestionproyectos.Service;

import gestion.proyectos.gestionproyectos.Entity.Lessons;
import gestion.proyectos.gestionproyectos.Entity.Proyect;
import gestion.proyectos.gestionproyectos.Repository.LessonsRepository;
import gestion.proyectos.gestionproyectos.Repository.ProyectRepository;
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
public class LessonsServiceTest {

    @Mock
    private LessonsRepository lessonsRepository;

    @Mock
    private ProyectRepository proyectRepository;

    @InjectMocks
    private LessonsService lessonsService;

    private Lessons testLessons;
    private Proyect testProyect;

    @BeforeEach
    void setUp() {
        // Configure a Proyect for testing
        testProyect = new Proyect();
        testProyect.setIdProyecto(1L);
        testProyect.setNameProyect("Test Project");

        // Configure a Lessons object for testing
        testLessons = new Lessons();
        testLessons.setIdLesson(1L);
        testLessons.setDescription("Test Lesson Description");
        testLessons.setRecommendations("Test Recommendations");
        testLessons.setRegistrationDate(LocalDate.now().toString());
        testLessons.setCategory("Best Practice");
        testLessons.setImpact("High");
        testLessons.setProyect(testProyect);
    }

    @Test
    void whenSaveLessons_thenReturnSavedLessons() {
        // Given
        when(proyectRepository.findById(1L)).thenReturn(Optional.of(testProyect));
        when(lessonsRepository.save(any(Lessons.class))).thenReturn(testLessons);

        // When
        Lessons savedLessons = lessonsService.create(testLessons);

        // Then
        assertNotNull(savedLessons);
        assertEquals(testLessons.getDescription(), savedLessons.getDescription());
        assertEquals(testLessons.getRecommendations(), savedLessons.getRecommendations());
        assertEquals(testLessons.getCategory(), savedLessons.getCategory());
        assertEquals(testLessons.getImpact(), savedLessons.getImpact());
        verify(proyectRepository).findById(1L);
        verify(lessonsRepository).save(any(Lessons.class));
    }

    @Test
    void whenGetLessonsById_thenReturnLessons() {
        // Given
        when(lessonsRepository.findById(1L)).thenReturn(Optional.of(testLessons));

        // When
        Lessons foundLessons = lessonsService.getById(1L);

        // Then
        assertNotNull(foundLessons);
        assertEquals(testLessons.getIdLesson(), foundLessons.getIdLesson());
        assertEquals(testLessons.getDescription(), foundLessons.getDescription());
        verify(lessonsRepository).findById(1L);
    }

    @Test
    void whenGetLessonsByIdWithInvalidId_thenThrowRuntimeException() {
        // Given
        when(lessonsRepository.findById(99L)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> lessonsService.getById(99L));
        assertTrue(exception.getMessage().contains("Lesson not found with id 99"));
        verify(lessonsRepository).findById(99L);
    }

    @Test
    void whenUpdateLessons_thenReturnUpdatedLessons() {
        // Given
        when(lessonsRepository.findById(1L)).thenReturn(Optional.of(testLessons));
        String updatedDescription = "Updated Description";
        String updatedRecommendations = "Updated Recommendations";

        // New details for the update
        Lessons updatedDetails = new Lessons();
        updatedDetails.setDescription(updatedDescription);
        updatedDetails.setRecommendations(updatedRecommendations);

        when(lessonsRepository.save(any(Lessons.class))).thenReturn(testLessons);

        // When
        Lessons updatedLessons = lessonsService.update(testLessons.getIdLesson(), updatedDetails);

        // Then
        assertNotNull(updatedLessons);
        assertEquals(updatedDescription, updatedLessons.getDescription());
        assertEquals(updatedRecommendations, updatedLessons.getRecommendations());
        verify(lessonsRepository).save(any(Lessons.class));
    }

    @Test
    void whenDeleteLessons_withValidId_thenVerifyRepositoryCall() {
        // Given
        Long id = 1L;
        when(lessonsRepository.existsById(id)).thenReturn(true);
        doNothing().when(lessonsRepository).deleteById(id);

        // When
        lessonsService.delete(id);

        // Then
        verify(lessonsRepository).existsById(id);
        verify(lessonsRepository).deleteById(id);
    }

    @Test
    void whenDeleteLessons_withInvalidId_thenThrowRuntimeException() {
        // Given
        Long nonExistingId = 99L;
        when(lessonsRepository.existsById(nonExistingId)).thenReturn(false);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> lessonsService.delete(nonExistingId));
        assertTrue(exception.getMessage().contains("Lesson not found with id 99"));
        verify(lessonsRepository).existsById(nonExistingId);
        verify(lessonsRepository, never()).deleteById(anyLong());
    }

    @Test
    void whenSaveLessonsWithNullProject_thenReturnSavedLessons() {
        // Given
        testLessons.setProyect(null);
        when(lessonsRepository.save(any(Lessons.class))).thenReturn(testLessons);

        // When
        Lessons savedLessons = lessonsService.create(testLessons);

        // Then
        assertNotNull(savedLessons);
        assertNull(savedLessons.getProyect());
        verify(lessonsRepository).save(any(Lessons.class));
    }

    @Test
    void whenUpdateLessonsWithNewCategory_thenReturnUpdatedLessons() {
        // Given
        when(lessonsRepository.findById(1L)).thenReturn(Optional.of(testLessons));
        String newCategory = "Technical";

        Lessons updatedDetails = new Lessons();
        updatedDetails.setCategory(newCategory);

        when(lessonsRepository.save(any(Lessons.class))).thenReturn(testLessons);

        // When
        Lessons updatedLessons = lessonsService.update(testLessons.getIdLesson(), updatedDetails);

        // Then
        assertNotNull(updatedLessons);
        assertEquals(newCategory, updatedLessons.getCategory());
        verify(lessonsRepository).save(any(Lessons.class));
    }

    @Test
    void whenUpdateLessonsWithNewImpact_thenReturnUpdatedLessons() {
        // Given
        when(lessonsRepository.findById(1L)).thenReturn(Optional.of(testLessons));
        String newImpact = "Low";

        Lessons updatedDetails = new Lessons();
        updatedDetails.setImpact(newImpact);

        when(lessonsRepository.save(any(Lessons.class))).thenReturn(testLessons);

        // When
        Lessons updatedLessons = lessonsService.update(testLessons.getIdLesson(), updatedDetails);

        // Then
        assertNotNull(updatedLessons);
        assertEquals(newImpact, updatedLessons.getImpact());
        verify(lessonsRepository).save(any(Lessons.class));
    }

    @Test
    void whenCreateLessons_withInvalidDate_thenThrowException() {
        // Given
        when(proyectRepository.findById(1L)).thenReturn(Optional.of(testProyect));
        testLessons.setRegistrationDate("invalid-date");

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> lessonsService.create(testLessons));
        verify(lessonsRepository, never()).save(any(Lessons.class));
    }

    @Test
    void whenCreateLessons_withInvalidProject_thenThrowException() {
        // Given
        // The project ID is set to 1, but we mock an empty result
        when(proyectRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> lessonsService.create(testLessons));
        assertTrue(exception.getMessage().contains("Proyect not found with id 1"));
        verify(lessonsRepository, never()).save(any(Lessons.class));
    }

    @Test
    void whenUpdateLessonsWithInvalidDate_thenThrowIllegalArgumentException() {
        // Given
        when(lessonsRepository.findById(1L)).thenReturn(Optional.of(testLessons));

        // Prepare updated lesson details with an invalid date
        Lessons updatedLessonDetails = new Lessons();
        updatedLessonDetails.setRegistrationDate("invalid-date");

        // When & Then
        assertThrows(IllegalArgumentException.class, () ->
                lessonsService.update(testLessons.getIdLesson(), updatedLessonDetails)
        );
        verify(lessonsRepository, never()).save(any(Lessons.class));
    }
}