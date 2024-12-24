package gestion.proyectos.gestionproyectos.Controller;

import gestion.proyectos.gestionproyectos.Service.RegistroLeccionesAprendidasDocumentService;
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
@RequestMapping("/api/documents/LessonsLearned")
@CrossOrigin("*")
public class LessonsLearnedDocumentController {

    private static final Logger logger = LoggerFactory.getLogger(LessonsLearnedDocumentController.class);

    @Autowired
    private RegistroLeccionesAprendidasDocumentService registroService;

    @PostMapping("/generate")
    public ResponseEntity<?> generateRegistroLeccionesAprendidasDocument(
            @RequestParam Long idExit,
            @RequestBody Map<String, String> requestData) {

        try {
            logger.info("Generando documento de registro de lecciones aprendidas con datos: {}", requestData);

            byte[] pdfContent = registroService.generateDocument(requestData, idExit);
            String filename = generateFilename("registro_lecciones_aprendidas");

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("filename", filename);

            logger.info("Documento de registro de lecciones aprendidas generado exitosamente: {}", filename);
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