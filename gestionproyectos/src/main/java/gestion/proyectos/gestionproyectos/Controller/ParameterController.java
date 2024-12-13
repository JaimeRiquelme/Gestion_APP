package gestion.proyectos.gestionproyectos.Controller;

import gestion.proyectos.gestionproyectos.Entity.Parameter;
import gestion.proyectos.gestionproyectos.Service.ParameterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/parameters")
public class ParameterController {

    @Autowired
    private ParameterService parameterService;

    // Create
    @PostMapping("/saveParameter")
    public ResponseEntity<Parameter> createParameter(@RequestBody Parameter parameter) {
        Parameter newParameter = parameterService.save(parameter);
        return new ResponseEntity<>(newParameter, HttpStatus.CREATED);
    }

    // Read All
    @GetMapping("/getAllParameters")
    public ResponseEntity<List<Parameter>> getAllParameters() {
        List<Parameter> parameters = parameterService.getAll();
        return new ResponseEntity<>(parameters, HttpStatus.OK);
    }

    // Read One
    @GetMapping("/{id}")
    public ResponseEntity<Parameter> getParameterById(@PathVariable Long id) {
        Parameter parameter = parameterService.getById(id);
        if (parameter != null) {
            return new ResponseEntity<>(parameter, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Update
    @PutMapping("/{id}")
    public ResponseEntity<Parameter> updateParameter(@PathVariable Long id, @RequestBody Parameter parameter) {
        parameter.setIdParameter(id);
        Parameter updatedParameter = parameterService.update(parameter);
        if (updatedParameter != null) {
            return new ResponseEntity<>(updatedParameter, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteParameter(@PathVariable Long id) {
        try {
            parameterService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}