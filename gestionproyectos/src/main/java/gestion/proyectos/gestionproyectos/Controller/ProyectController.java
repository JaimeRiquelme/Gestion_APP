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

    @PostMapping("/create")
    public ResponseEntity<Proyect> create(@RequestBody Proyect proyect) {
        return new ResponseEntity<>(proyectService.create(proyect), HttpStatus.CREATED);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Proyect>> getAll() {
        return new ResponseEntity<>(proyectService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<Proyect> getById(@PathVariable Long id) {
        Proyect proyect = proyectService.getById(id);
        return proyect != null ?
                new ResponseEntity<>(proyect, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Proyect> update(@PathVariable Long id, @RequestBody Proyect proyect) {
        try {
            Proyect updatedProyect = proyectService.update(id, proyect);
            return new ResponseEntity<>(updatedProyect, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

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
