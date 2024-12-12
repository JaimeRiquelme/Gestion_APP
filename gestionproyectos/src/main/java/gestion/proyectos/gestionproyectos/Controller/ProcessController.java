package gestion.proyectos.gestionproyectos.Controller;

import gestion.proyectos.gestionproyectos.Entity.Process; // Agregar esta importación
import gestion.proyectos.gestionproyectos.Service.ProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/v1/process")
public class ProcessController {
    @Autowired
    private ProcessService processService;

    @PostMapping("/saveProcess")
    public ResponseEntity<Process> saveProcess(@RequestBody Process process) {
        Process savedProcess = processService.saveProcess(process);
        return new ResponseEntity<>(savedProcess, HttpStatus.CREATED);
    }

    @GetMapping("/getAllProcess")
    public ResponseEntity<List<Process>> getAllProcess() {
        List<Process> processes = processService.getProcesses();
        return new ResponseEntity<>(processes, HttpStatus.OK);
    }

    @GetMapping("/getProcess/{id}")
    public ResponseEntity<Process> getProcess(@PathVariable long id) {
        Process process = processService.getProcess(id);
        if (process != null) {
            return new ResponseEntity<>(process, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/deleteProcess/{id}")
    public ResponseEntity<Void> deleteProcess(@PathVariable long id) {
        processService.deleteProcess(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}