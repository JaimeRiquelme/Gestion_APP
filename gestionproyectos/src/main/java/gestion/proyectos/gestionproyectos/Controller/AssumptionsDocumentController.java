package gestion.proyectos.gestionproyectos.Controller;

import gestion.proyectos.gestionproyectos.Service.AssumptionsDocumentService;
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
@RequestMapping("/api/documents/assumptions")
@CrossOrigin("*")
public class AssumptionsDocumentController {

    private static final Logger logger = LoggerFactory.getLogger(AssumptionsDocumentController.class);

    @Autowired
    private AssumptionsDocumentService assumptionsService;

    @PostMapping("/generate")
    public ResponseEntity<?> generateAssumptionsDocument(
            @RequestParam Long idExit,
            @RequestBody Map<String, String> requestData) {

        try {
            logger.info("Generating assumptions document with data: {}", requestData);

            byte[] pdfContent = assumptionsService.generateDocument(requestData, idExit);
            String filename = generateFilename("registro_de_supuestos");

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("filename", filename);

            logger.info("Successfully generated assumptions document: {}", filename);
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfContent);

        } catch (IllegalArgumentException e) {
            logger.error("Validation error while generating assumptions document", e);
            return ResponseEntity.badRequest()
                    .body("Error de validación: " + e.getMessage());
        } catch (IOException e) {
            logger.error("Error generating assumptions document", e);
            return ResponseEntity.internalServerError()
                    .body("Error generando el documento: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error while generating assumptions document", e);
            return ResponseEntity.internalServerError()
                    .body("Error inesperado: " + e.getMessage());
        }
    }

    private String generateFilename(String baseFilename) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        return baseFilename + "_" + timestamp + ".pdf";
    }
}