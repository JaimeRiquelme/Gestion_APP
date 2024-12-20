package gestion.proyectos.gestionproyectos.Controller;

import gestion.proyectos.gestionproyectos.Service.AssumptionsDocumentService;
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
@RequestMapping("/api/documents")
@CrossOrigin("*")
public class DocumentController {
    private static final Logger logger = LoggerFactory.getLogger(DocumentController.class);

    @Autowired
    private AssumptionsDocumentService assumptionsService;

    @Autowired
    private ChangeRequestDocumentService changeRequestService;

    @PostMapping("/generate")
    public ResponseEntity<?> generateDocument(
            @RequestParam String type,
            @RequestBody Map<String, String> requestData) {
        try {
            logger.info("Generating document of type: {} with data: {}", type, requestData);

            byte[] pdfContent;
            String filename;

            switch (type.toLowerCase()) {
                case "assumptions":
                    pdfContent = assumptionsService.generateDocument(requestData);
                    filename = generateFilename("registro_de_supuestos");
                    break;
                case "change-request":
                    pdfContent = changeRequestService.generateDocument(requestData);
                    filename = generateFilename("solicitud_cambio");
                    break;
                default:
                    logger.error("Unsupported document type requested: {}", type);
                    return ResponseEntity.badRequest()
                            .body("Tipo de documento no soportado: " + type);
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("filename", filename);

            logger.info("Successfully generated document: {}", filename);
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfContent);

        } catch (IllegalArgumentException e) {
            logger.error("Validation error while generating document", e);
            return ResponseEntity.badRequest()
                    .body("Error de validación: " + e.getMessage());
        } catch (IOException e) {
            logger.error("Error generating document", e);
            return ResponseEntity.internalServerError()
                    .body("Error generando el documento: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error while generating document", e);
            return ResponseEntity.internalServerError()
                    .body("Error inesperado: " + e.getMessage());
        }
    }

    private String generateFilename(String baseFilename) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        return baseFilename + "_" + timestamp + ".pdf";
    }
}