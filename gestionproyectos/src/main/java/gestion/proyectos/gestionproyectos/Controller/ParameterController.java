package gestion.proyectos.gestionproyectos.Controller;

import gestion.proyectos.gestionproyectos.Entity.Parameter;
import gestion.proyectos.gestionproyectos.Service.ParameterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/parameters")
public class ParameterController {

    private final ParameterService parameterService;

    public ParameterController(ParameterService parameterService) {
        this.parameterService = parameterService;
    }

    // Crear un nuevo Parameter
    @PostMapping("/create")
    public ResponseEntity<Parameter> create(@RequestBody Parameter parameter) {
        try {
            Parameter newParameter = parameterService.create(parameter);
            return new ResponseEntity<>(newParameter, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    // Obtener todos los Parameters
    @GetMapping("/getAll")
    public ResponseEntity<List<Parameter>> getAll() {
        List<Parameter> parameters = parameterService.getAll();
        return new ResponseEntity<>(parameters, HttpStatus.OK);
    }

    // Obtener un Parameter por su ID
    @GetMapping("/getById/{id}")
    public ResponseEntity<Parameter> getById(@PathVariable Long id) {
        try {
            Parameter parameter = parameterService.getById(id);
            return new ResponseEntity<>(parameter, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    // Actualizar un Parameter
    @PutMapping("/update/{id}")
    public ResponseEntity<Parameter> update(@PathVariable Long id, @RequestBody Parameter parameterDetails) {
        try {
            Parameter updatedParameter = parameterService.update(id, parameterDetails);
            return new ResponseEntity<>(updatedParameter, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    // Eliminar un Parameter
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            parameterService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}