package gestion.proyectos.gestionproyectos.Controller;

import gestion.proyectos.gestionproyectos.Entity.Exit;
import gestion.proyectos.gestionproyectos.Service.ExitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/exit")
public class ExitController {
    
    @Autowired
    ExitService exitService;

    @PostMapping("/saveExit")
    public ResponseEntity<Exit> saveExit(@RequestBody Exit exit) {
        Exit newExit = exitService.save(exit);
        return ResponseEntity.ok(newExit);
    }

    @GetMapping("/getAllExit")
    public ResponseEntity<List<Exit>> listExits() {
        List<Exit> exits = exitService.getAll();
        return ResponseEntity.ok(exits);
    }

    @GetMapping("/GetExit/{id}")
    public ResponseEntity<Exit> getExitById(@PathVariable Long id) {
        Exit exit = exitService.getById(id);
        return ResponseEntity.ok(exit);
    }

    @PostMapping("/updateExit")
    public ResponseEntity<Exit> updateExit(@RequestBody Exit exit) {
        Exit updatedExit = exitService.update(exit);
        return ResponseEntity.ok(updatedExit);
    }

    @DeleteMapping("/DeleteExit/{id}")
    public ResponseEntity<Void> deleteExitById(@PathVariable Long id) throws Exception {
        exitService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
