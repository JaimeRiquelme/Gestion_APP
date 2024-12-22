package gestion.proyectos.gestionproyectos.Controller;

import gestion.proyectos.gestionproyectos.Entity.Process;
import gestion.proyectos.gestionproyectos.Service.ProcessService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/process")
public class ProcessController {

    private final ProcessService processService;

    public ProcessController(ProcessService processService) {
        this.processService = processService;
    }

    // Crear un proceso
    @PostMapping("/create")
    public ResponseEntity<Process> create(@RequestBody Process process) {
        return new ResponseEntity<>(processService.create(process), HttpStatus.CREATED);
    }

    // Obtener todos los procesos
    @GetMapping("/getAll")
    public ResponseEntity<List<Process>> getAll() {
        return new ResponseEntity<>(processService.getAll(), HttpStatus.OK);
    }

    // Obtener proceso por ID
    @GetMapping("/getById/{id}")
    public ResponseEntity<Process> getById(@PathVariable Long id) {
        try {
            Process process = processService.getById(id);
            return new ResponseEntity<>(process, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Actualizar un proceso
    @PutMapping("/update/{id}")
    public ResponseEntity<Process> update(@PathVariable Long id, @RequestBody Process process) {
        try {
            Process updatedProcess = processService.update(id, process);
            return new ResponseEntity<>(updatedProcess, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Eliminar un proceso
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            processService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}