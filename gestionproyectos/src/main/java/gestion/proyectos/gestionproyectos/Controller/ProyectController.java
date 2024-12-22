package gestion.proyectos.gestionproyectos.Controller;

import gestion.proyectos.gestionproyectos.Entity.Proyect;
import gestion.proyectos.gestionproyectos.Service.ProyectService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/proyect")
public class ProyectController {

    private final ProyectService proyectService;

    public ProyectController(ProyectService proyectService) {
        this.proyectService = proyectService;
    }

    // Crear un proyecto
    @PostMapping("/create")
    public ResponseEntity<Proyect> create(@RequestBody Proyect proyect) {
        try {
            Proyect newProyect = proyectService.create(proyect);
            return new ResponseEntity<>(newProyect, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    // Obtener todos los proyectos
    @GetMapping("/getAll")
    public ResponseEntity<List<Proyect>> getAll() {
        List<Proyect> proyects = proyectService.getAll();
        return new ResponseEntity<>(proyects, HttpStatus.OK);
    }

    // Obtener proyecto por ID
    @GetMapping("/getById/{id}")
    public ResponseEntity<Proyect> getById(@PathVariable Long id) {
        try {
            Proyect proyect = proyectService.getById(id);
            return new ResponseEntity<>(proyect, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    // Actualizar un proyecto
    @PutMapping("/update/{id}")
    public ResponseEntity<Proyect> update(@PathVariable Long id, @RequestBody Proyect proyectDetails) {
        try {
            Proyect updatedProyect = proyectService.update(id, proyectDetails);
            return new ResponseEntity<>(updatedProyect, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    // Eliminar un proyecto
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            proyectService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}