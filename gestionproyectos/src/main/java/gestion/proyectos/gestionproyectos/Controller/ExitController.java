package gestion.proyectos.gestionproyectos.Controller;

import gestion.proyectos.gestionproyectos.Entity.Exit;
import gestion.proyectos.gestionproyectos.Entity.Parameter;
import gestion.proyectos.gestionproyectos.Service.ExitService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/exit")
public class ExitController {

    private final ExitService exitService;

    public ExitController(ExitService exitService) {
        this.exitService = exitService;
    }

    // Crear un Exit
    @PostMapping("/create")
    public ResponseEntity<Exit> create(@RequestBody Exit exit) {
        try {
            Exit newExit = exitService.create(exit);
            return new ResponseEntity<>(newExit, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    // Obtener todos los Exit
    @GetMapping("/getAll")
    public ResponseEntity<List<Exit>> getAll() {
        List<Exit> exits = exitService.getAll();
        return new ResponseEntity<>(exits, HttpStatus.OK);
    }

    // Obtener Exit por ID
    @GetMapping("/getById/{id}")
    public ResponseEntity<Exit> getById(@PathVariable Long id) {
        try {
            Exit exit = exitService.getById(id);
            return new ResponseEntity<>(exit, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    // Actualizar Exit
    @PutMapping("/update/{id}")
    public ResponseEntity<Exit> update(@PathVariable Long id, @RequestBody Exit exitDetails) {
        try {
            Exit updatedExit = exitService.update(id, exitDetails);
            return new ResponseEntity<>(updatedExit, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    // Eliminar Exit
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            exitService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Obtener lista de Parameters por ID de Exit
    @GetMapping("/{id}/parameters")
    public ResponseEntity<List<Parameter>> getParametersByExitId(@PathVariable Long id) {
        try {
            // Llamamos al servicio para obtener los parámetros asociados al Exit
            List<Parameter> parameters = exitService.getParametersByExitId(id);
            return new ResponseEntity<>(parameters, HttpStatus.OK); // Retornamos la lista con estado 200
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND); // Retornamos 404 si no se encuentra el Exit
        }
    }

    // Obtener Exit por ID de Proceso y nombre de Exit
    @GetMapping("/getByIdProcessAndNameExit")
    public ResponseEntity<Exit> getByIdProcessAndNameExit(@RequestParam Long idProcess, @RequestParam String nameExit) {
        try {
            Exit exit = exitService.findByIdProcessAndNameExit(idProcess, nameExit);
            return new ResponseEntity<>(exit, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    // Nuevo endpoint para obtener el documento PDF por ID de Exit
    @GetMapping("{id}/document")
    public ResponseEntity<byte[]> getDocumentByIdExit(@PathVariable Long id) {
        try {
            byte[] pdfContent = exitService.getDocumentByIdExit(id);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("filename", "document.pdf");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfContent);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("Exit not found")) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND); // Exit no encontrado
            } else if (e.getMessage().contains("Document not found")) {
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT); // Documento es null
            } else {
                // Manejo de otras excepciones en tiempo de ejecución
                e.printStackTrace();
                return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            // Manejo de cualquier otra excepción inesperada
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
