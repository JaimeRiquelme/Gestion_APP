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
            @RequestParam Long idExit,
            @RequestBody Map<String, String> requestData) {

        try {
            byte[] pdfContent = actInstitutionService.generateDocument(requestData, idExit);
            String filename = generateFilename("acta_de_constitucion");

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("filename", filename);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfContent);

        } catch (MissingFieldException e) {
            return ResponseEntity.badRequest()
                    .body("Validation error: Missing or invalid fields: " + e.getMessage());
        } catch (TemplateNotFoundException e) {
            return ResponseEntity.status(404)
                    .body("Error: The template for generating the document was not found. Details: " + e.getMessage());
        } catch (DocumentGenerationException e) {
            return ResponseEntity.internalServerError()
                    .body("Error while generating the PDF document. Details: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("An unexpected error occurred: " + e.getMessage());
        }
    }

    private String generateFilename(String baseFilename) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        return baseFilename + "_" + timestamp + ".pdf";
    }
}