package gestion.proyectos.gestionproyectos.Controller;


import gestion.proyectos.gestionproyectos.Service.ProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/v1/process")
public class ProcessController {
    @Autowired
    private ProcessService processService;

    @PostMapping ("/saveProcess")
    public void saveProcess(@RequestBody Process process) {
        processService.SaveProcess(process);
    }
    @GetMapping ("/getAllProcess")
    public List<Process> getAllProcess() {
        return processService.GetProcesses();
    }

    @GetMapping ("/GetProcess/{id}")
    public ResponseEntity<Process> getProcess(@PathVariable long id) {
        Process process = processService.GetProcess(id);
        return new ResponseEntity<>(process, HttpStatus.OK);
    }

    @PostMapping  ("/DeleteProcess/{id}")
    public void deleteProcess(@PathVariable long id) {
        processService.DeleteProcess(id);
    }

}
