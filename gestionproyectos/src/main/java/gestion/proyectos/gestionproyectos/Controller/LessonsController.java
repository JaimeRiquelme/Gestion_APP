package gestion.proyectos.gestionproyectos.Controller;

import gestion.proyectos.gestionproyectos.Entity.Lessons;
import gestion.proyectos.gestionproyectos.Service.LessonsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/lessons")
public class LessonsController {

    private final LessonsService lessonsService;

    public LessonsController(LessonsService lessonsService) {
        this.lessonsService = lessonsService;
    }

    // Crear lección
    @PostMapping("/create")
    public ResponseEntity<Lessons> create(@RequestBody Lessons lessons) {
        return new ResponseEntity<>(lessonsService.create(lessons), HttpStatus.CREATED);
    }

    // Obtener todas las lecciones
    @GetMapping("/getAll")
    public ResponseEntity<List<Lessons>> getAll() {
        return new ResponseEntity<>(lessonsService.getAll(), HttpStatus.OK);
    }

    // Obtener lección por ID
    @GetMapping("/getById/{id}")
    public ResponseEntity<Lessons> getById(@PathVariable Long id) {
        try {
            Lessons lessons = lessonsService.getById(id);
            return new ResponseEntity<>(lessons, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Actualizar lección
    @PutMapping("/update/{id}")
    public ResponseEntity<Lessons> update(@PathVariable Long id, @RequestBody Lessons lessons) {
        try {
            Lessons updatedLesson = lessonsService.update(id, lessons);
            return new ResponseEntity<>(updatedLesson, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Eliminar lección
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            lessonsService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}