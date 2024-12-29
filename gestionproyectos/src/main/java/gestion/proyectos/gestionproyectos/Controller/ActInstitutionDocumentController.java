package gestion.proyectos.gestionproyectos.Controller;


import gestion.proyectos.gestionproyectos.Service.ActInstitutionDocumentService;
import gestion.proyectos.gestionproyectos.exception.DocumentGenerationException;
import gestion.proyectos.gestionproyectos.exception.MissingFieldException;
import gestion.proyectos.gestionproyectos.exception.TemplateNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@RestController
@RequestMapping("/api/documents/institution")
@CrossOrigin("*")
public class ActInstitutionDocumentController {

    @Autowired
    private ActInstitutionDocumentService actInstitutionService;

    @PostMapping("/generate")
    public ResponseEntity<?> generateAssumptionsDocument(
            @RequestParam Long idProyecto,  // Cambiado de idExit a idProyecto
            @RequestBody Map<String, String> requestData) {

        try {
            // Ahora llamamos al nuevo método que maneja todo el proceso transaccional
            byte[] pdfContent = actInstitutionService.generateInstitutionDocumentProcess(requestData, idProyecto);
            String filename = generateFilename("acta_de_constitucion");

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("filename", filename);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfContent);

        } catch (MissingFieldException e) {
            return ResponseEntity.badRequest()
                    .body("Error de validación: Campos faltantes o inválidos: " + e.getMessage());
        } catch (TemplateNotFoundException e) {
            return ResponseEntity.status(404)
                    .body("Error: No se encontró la plantilla para generar el documento. Detalles: " + e.getMessage());
        } catch (DocumentGenerationException e) {
            return ResponseEntity.internalServerError()
                    .body("Error al generar el documento PDF. Detalles: " + e.getMessage());
        } catch (Exception e) {
            // Log del error para debugging
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body("Ocurrió un error inesperado: " + e.getMessage());
        }
    }

    private String generateFilename(String baseFilename) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        return baseFilename + "_" + timestamp + ".pdf";
    }
}