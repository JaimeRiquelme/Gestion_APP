package gestion.proyectos.gestionproyectos.Controller;

import gestion.proyectos.gestionproyectos.Entity.Exit;
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

    // Obtener lista de Exit por ID de Process
    @GetMapping("/{id}/exits")
    public ResponseEntity<List<Exit>> getExitsByProcessId(@PathVariable Long id) {
        try {
            List<Exit> exits = processService.getExitsByProcessId(id);
            return new ResponseEntity<>(exits, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/getByIdManagementAndNameProcess")
    public ResponseEntity<Process> getByIdManagementAndNameProcess(@RequestParam Long idManagement, @RequestParam String nameProcess) {
        try {
            System.out.println("idManagement: " + idManagement);
            System.out.println("nameProcess: " + nameProcess);
            Process process = processService.getProcessByIdManagementAndNameProcess(idManagement, nameProcess);

            if(process != null) {
                return new ResponseEntity<>(process, HttpStatus.OK);
            }else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}