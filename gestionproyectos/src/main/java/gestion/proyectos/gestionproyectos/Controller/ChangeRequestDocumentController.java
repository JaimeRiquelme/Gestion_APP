package gestion.proyectos.gestionproyectos.Controller;

import gestion.proyectos.gestionproyectos.Service.ChangeRequestDocumentService;
import gestion.proyectos.gestionproyectos.exception.DocumentGenerationException;
import gestion.proyectos.gestionproyectos.exception.MissingFieldException;
import gestion.proyectos.gestionproyectos.exception.TemplateNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@RestController
@RequestMapping("/api/documents/change-request")
@CrossOrigin("*")
public class ChangeRequestDocumentController {

    private static final Logger logger = LoggerFactory.getLogger(ChangeRequestDocumentController.class);

    @Autowired
    private ChangeRequestDocumentService changeRequestService;

    @PostMapping("/generate")
    public ResponseEntity<?> generateChangeRequestDocument(
            @RequestParam Long idExit,
            @RequestBody Map<String, String> requestData) {

        try {
            logger.info("Generating change-request document with data: {}", requestData);

            byte[] pdfContent = changeRequestService.generateDocument(requestData, idExit);
            String filename = generateFilename("solicitud_cambio");

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("filename", filename);

            logger.info("Successfully generated change-request document: {}", filename);
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