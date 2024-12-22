package gestion.proyectos.gestionproyectos.Service;

import gestion.proyectos.gestionproyectos.Entity.Lessons;
import gestion.proyectos.gestionproyectos.Entity.Proyect;
import gestion.proyectos.gestionproyectos.Repository.LessonsRepository;
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

    @InjectMocks
    private LessonsService lessonsService;

    private Lessons testLessons;
    private Proyect testProyect;

    @BeforeEach
    void setUp() {
        // Configurar Proyect para pruebas
        testProyect = new Proyect();
        testProyect.setIdProyecto(1L);
        testProyect.setNameProyect("Test Project");

        // Configurar Lessons para pruebas
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
        when(lessonsRepository.save(any(Lessons.class))).thenReturn(testLessons);

        // When
        Lessons savedLessons = lessonsService.create(testLessons);

        // Then
        assertNotNull(savedLessons);
        assertEquals(testLessons.getDescription(), savedLessons.getDescription());
        assertEquals(testLessons.getRecommendations(), savedLessons.getRecommendations());
        assertEquals(testLessons.getCategory(), savedLessons.getCategory());
        assertEquals(testLessons.getImpact(), savedLessons.getImpact());
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
    void whenGetLessonsByIdWithInvalidId_thenReturnNull() {
        // Given
        when(lessonsRepository.findById(99L)).thenReturn(Optional.empty());

        // When
        Lessons foundLessons = lessonsService.getById(99L);

        // Then
        assertNull(foundLessons);
        verify(lessonsRepository).findById(99L);
    }

    @Test
    void whenUpdateLessons_thenReturnUpdatedLessons() {
        // Given
        String updatedDescription = "Updated Description";
        String updatedRecommendations = "Updated Recommendations";
        testLessons.setDescription(updatedDescription);
        testLessons.setRecommendations(updatedRecommendations);

        when(lessonsRepository.save(any(Lessons.class))).thenReturn(testLessons);

        // When
        Lessons updatedLessons = lessonsService.update(testLessons.getIdLesson(), testLessons);

        // Then
        assertNotNull(updatedLessons);
        assertEquals(updatedDescription, updatedLessons.getDescription());
        assertEquals(updatedRecommendations, updatedLessons.getRecommendations());
        verify(lessonsRepository).save(any(Lessons.class));
    }

    @Test
    void whenDeleteLessons_thenVerifyRepositoryCall() {
        // Given
        Long id = 1L;
        doNothing().when(lessonsRepository).deleteById(id);

        // When
        lessonsService.delete(id);

        // Then
        verify(lessonsRepository).deleteById(id);
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
        String newCategory = "Technical";
        testLessons.setCategory(newCategory);

        when(lessonsRepository.save(any(Lessons.class))).thenReturn(testLessons);

        // When
        Lessons updatedLessons = lessonsService.update(testLessons.getIdLesson(), testLessons);

        // Then
        assertNotNull(updatedLessons);
        assertEquals(newCategory, updatedLessons.getCategory());
        verify(lessonsRepository).save(any(Lessons.class));
    }

    @Test
    void whenUpdateLessonsWithNewImpact_thenReturnUpdatedLessons() {
        // Given
        String newImpact = "Low";
        testLessons.setImpact(newImpact);

        when(lessonsRepository.save(any(Lessons.class))).thenReturn(testLessons);

        // When
        Lessons updatedLessons = lessonsService.update(testLessons.getIdLesson(), testLessons);

        // Then
        assertNotNull(updatedLessons);
        assertEquals(newImpact, updatedLessons.getImpact());
        verify(lessonsRepository).save(any(Lessons.class));
    }
}