package gestion.proyectos.gestionproyectos.Controller;


import gestion.proyectos.gestionproyectos.Entity.Proyect;
import gestion.proyectos.gestionproyectos.Service.ProyectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/proyects")
public class ProyectController {

    @Autowired
    private ProyectService proyectService;

    @PostMapping
    public ResponseEntity<Proyect> createProyect(@RequestBody Proyect proyect) {
        return new ResponseEntity<>(proyectService.saveProyect(proyect), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Iterable<Proyect>> getAllProyects() {
        return new ResponseEntity<>(proyectService.getProyects(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Proyect> getProyectById(@PathVariable Long id) {
        Proyect proyect = proyectService.getProyectById(id);
        return proyect != null ?
                new ResponseEntity<>(proyect, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Proyect> updateProyect(@PathVariable Long id, @RequestBody Proyect proyect) {
        proyect.setIdProyecto(id);
        return new ResponseEntity<>(proyectService.updateProyect(proyect), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProyect(@PathVariable Long id) {
        proyectService.deleteProyect(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
