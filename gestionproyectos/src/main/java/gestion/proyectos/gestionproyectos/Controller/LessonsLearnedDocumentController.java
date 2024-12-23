package gestion.proyectos.gestionproyectos.Controller;

import gestion.proyectos.gestionproyectos.Service.RegistroLeccionesAprendidasDocumentService;
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

        } catch (IllegalArgumentException e) {
            logger.error("Error de validación al generar el documento de registro de lecciones aprendidas", e);
            return ResponseEntity.badRequest()
                    .body("Error de validación: " + e.getMessage());
        } catch (IOException e) {
            logger.error("Error al generar el documento de registro de lecciones aprendidas", e);
            return ResponseEntity.internalServerError()
                    .body("Error generando el documento: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Error inesperado al generar el documento de registro de lecciones aprendidas", e);
            return ResponseEntity.internalServerError()
                    .body("Error inesperado: " + e.getMessage());
        }
    }

    private String generateFilename(String baseFilename) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        return baseFilename + "_" + timestamp + ".pdf";
    }
}