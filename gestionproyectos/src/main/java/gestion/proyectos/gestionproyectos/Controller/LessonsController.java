package gestion.proyectos.gestionproyectos.Controller;


import gestion.proyectos.gestionproyectos.Entity.Lessons;
import gestion.proyectos.gestionproyectos.Service.LessonsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/lessons")
public class LessonsController {

    @Autowired
    private LessonsService lessonsService;

    @PostMapping("/CreateLessons")
    public ResponseEntity<Lessons> createLessons(@RequestBody Lessons lessons) {
        return new ResponseEntity<>(lessonsService.saveLessons(lessons), HttpStatus.CREATED);
    }

    @PostMapping("/GetLessonsById")
    public ResponseEntity<Lessons> getLessonsById(@RequestBody Long id) {
        Lessons lessons = lessonsService.getLessonsById(id);
        return lessons != null ?
                new ResponseEntity<>(lessons, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/DeleteLessons")
    public ResponseEntity<Void> deleteLessons(@RequestBody Long id) {
        lessonsService.deleteLessons(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/UpdateLessons")
    public ResponseEntity<Lessons> updateLessons(@RequestBody Lessons lessons) {
        return new ResponseEntity<>(lessonsService.updateLessons(lessons), HttpStatus.OK);
    }



}
