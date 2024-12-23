package gestion.proyectos.gestionproyectos.Controller;

import gestion.proyectos.gestionproyectos.Service.ScopeManagementPlanDocumentService;
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

/**
 * Controller for handling HTTP requests related to Scope Management Plan documents.
 */
@RestController
@RequestMapping("/api/documents/scope-management-plan")
@CrossOrigin("*")
public class ScopeManagementPlanDocumentController {

    private static final Logger logger = LoggerFactory.getLogger(ScopeManagementPlanDocumentController.class);

    @Autowired
    private ScopeManagementPlanDocumentService scopeService;

    /**
     * Endpoint to generate a Scope Management Plan PDF document.
     *
     * @param idExit      The exit identifier associated with the document.
     * @param requestData The data required to generate the document.
     * @return            A ResponseEntity containing the generated PDF or an error message.
     */
    @PostMapping("/generate")
    public ResponseEntity<?> generateScopeManagementPlanDocument(
            @RequestParam Long idExit,
            @RequestBody Map<String, String> requestData) {

        try {
            logger.info("Generating Scope Management Plan document with data: {}", requestData);

            byte[] pdfContent = scopeService.generateDocument(requestData, idExit);
            String filename = generateFilename("gestion_del_alcance");

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("filename", filename);

            logger.info("Scope Management Plan document generated successfully: {}", filename);
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfContent);

        } catch (IllegalArgumentException e) {
            logger.error("Validation error while generating Scope Management Plan document", e);
            return ResponseEntity.badRequest()
                    .body("Validation Error: " + e.getMessage());
        } catch (IOException e) {
            logger.error("IO error while generating Scope Management Plan document", e);
            return ResponseEntity.internalServerError()
                    .body("Error generating document: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error while generating Scope Management Plan document", e);
            return ResponseEntity.internalServerError()
                    .body("Unexpected Error: " + e.getMessage());
        }
    }

    /**
     * Generates a unique filename for the PDF document based on the current timestamp.
     *
     * @param baseFilename The base name for the file.
     * @return             A unique filename with a timestamp.
     */
    private String generateFilename(String baseFilename) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        return baseFilename + "_" + timestamp + ".pdf";
    }
}