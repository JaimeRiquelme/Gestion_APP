package gestion.proyectos.gestionproyectos.Controller;

import gestion.proyectos.gestionproyectos.Service.ChangeRequestDocumentService;
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

        } catch (IllegalArgumentException e) {
            logger.error("Validation error while generating change-request document", e);
            return ResponseEntity.badRequest()
                    .body("Error de validación: " + e.getMessage());
        } catch (IOException e) {
            logger.error("Error generating change-request document", e);
            return ResponseEntity.internalServerError()
                    .body("Error generando el documento: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error while generating change-request document", e);
            return ResponseEntity.internalServerError()
                    .body("Error inesperado: " + e.getMessage());
        }
    }

    private String generateFilename(String baseFilename) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        return baseFilename + "_" + timestamp + ".pdf";
    }
}