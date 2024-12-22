package gestion.proyectos.gestionproyectos.Service;

import gestion.proyectos.gestionproyectos.Entity.Lessons;
import gestion.proyectos.gestionproyectos.Entity.Proyect;
import gestion.proyectos.gestionproyectos.Repository.LessonsRepository;
import gestion.proyectos.gestionproyectos.Repository.ProyectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

@Service
public class LessonsService {

    private final LessonsRepository lessonsRepository;
    private final ProyectRepository proyectRepository;

    @Autowired
    public LessonsService(LessonsRepository lessonsRepository, ProyectRepository proyectRepository) {
        this.lessonsRepository = lessonsRepository;
        this.proyectRepository = proyectRepository;
    }

    // Crear lección
    public Lessons create(Lessons lessons) {
        // Validar la existencia del proyecto relacionado
        if (lessons.getProyect() != null && lessons.getProyect().getIdProyecto() != null) {
            Optional<Proyect> optionalProyect = proyectRepository.findById(lessons.getProyect().getIdProyecto());
            if (optionalProyect.isPresent()) {
                lessons.setProyect(optionalProyect.get());
            } else {
                throw new RuntimeException("Proyect not found with id " + lessons.getProyect().getIdProyecto());
            }
        }

        // Validar la fecha de registro
        validateDate(lessons.getRegistrationDate());

        return lessonsRepository.save(lessons);
    }

    // Obtener todas las lecciones
    public List<Lessons> getAll() {
        return lessonsRepository.findAll();
    }

    // Obtener lección por ID
    public Lessons getById(Long id) {
        return lessonsRepository.findById(id).orElseThrow(() -> new RuntimeException("Lesson not found with id " + id));
    }

    // Actualizar lección
    public Lessons update(Long id, Lessons lessonDetails) {
        Lessons existingLesson = lessonsRepository.findById(id).orElseThrow(() -> new RuntimeException("Lesson not found with id " + id));

        if (lessonDetails.getDescription() != null) {
            existingLesson.setDescription(lessonDetails.getDescription());
        }
        if (lessonDetails.getRecommendations() != null) {
            existingLesson.setRecommendations(lessonDetails.getRecommendations());
        }
        if (lessonDetails.getRegistrationDate() != null) {
            validateDate(lessonDetails.getRegistrationDate()); // Validar la fecha
            existingLesson.setRegistrationDate(lessonDetails.getRegistrationDate());
        }
        if (lessonDetails.getCategory() != null) {
            existingLesson.setCategory(lessonDetails.getCategory());
        }
        if (lessonDetails.getImpact() != null) {
            existingLesson.setImpact(lessonDetails.getImpact());
        }

        // Validar y actualizar proyecto relacionado, si se proporciona
        if (lessonDetails.getProyect() != null && lessonDetails.getProyect().getIdProyecto() != null) {
            Optional<Proyect> optionalProyect = proyectRepository.findById(lessonDetails.getProyect().getIdProyecto());
            if (optionalProyect.isPresent()) {
                existingLesson.setProyect(optionalProyect.get());
            } else {
                throw new RuntimeException("Proyect not found with id " + lessonDetails.getProyect().getIdProyecto());
            }
        }

        return lessonsRepository.save(existingLesson);
    }

    // Eliminar lección
    public void delete(Long id) {
        if (lessonsRepository.existsById(id)) {
            lessonsRepository.deleteById(id);
        } else {
            throw new RuntimeException("Lesson not found with id " + id);
        }
    }

    // Método para validar fechas
    private void validateDate(String date) {
        if (date != null && !date.isBlank()) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate.parse(date, formatter);
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("La fecha debe estar en el formato AAAA-MM-DD y ser válida. Ejemplo de fecha válida: 2024-01-01");
            }
        }
    }
}