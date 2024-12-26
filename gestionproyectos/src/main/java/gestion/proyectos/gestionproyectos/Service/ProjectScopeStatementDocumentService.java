package gestion.proyectos.gestionproyectos.Service;

import gestion.proyectos.gestionproyectos.exception.DocumentGenerationException;
import gestion.proyectos.gestionproyectos.exception.MissingFieldException;
import gestion.proyectos.gestionproyectos.exception.TemplateNotFoundException;
import gestion.proyectos.gestionproyectos.util.TemplatePathResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class ProjectScopeStatementDocumentService implements DocumentService {

    @Autowired
    private LatexService latexService;

    @Autowired
    private TemplatePathResolver pathResolver;

    @Autowired
    private ExitService exitService;

    @Autowired
    private ParameterService parameterService;

    private static final Set<String> REQUIRED_FIELDS = new HashSet<>(Arrays.asList(
            "proyectName",
            "idProyect",
            "elaborationDate",
            "purposeJustificationProject",
            "scopeDescription",
            "highLevelRequirements",
            "boundaries",
            "strategy",
            "deliverables",
            "acceptanceCriteria",
            "restrictions",
            "costEstimation",
            "costBenefitAnalysis",
            "proyectPromotor",
            "proyectPromotorTitle"
    ));

    @Override
    public byte[] generateDocument(Map<String, String> data, Long idExit) throws IOException {
        validateData(data); // Validar los campos requeridos
        String templatePath;

        try {
            templatePath = pathResolver.resolve("enunciado-de-alcance-del-proyecto.tex"); // Ruta de la plantilla
        } catch (Exception e) {
            throw new TemplateNotFoundException("The template 'enunciado-de-alcance-del-proyecto.tex' was not found.");
        }

        try {
            // Generación del documento con LaTeX
            byte[] document = latexService.generateDocument(templatePath, data);
            // Actualización del documento generado en la salida
            exitService.updateAssumptionsDocument(idExit, document);
            // Guardar los parámetros utilizados
            parameterService.saveParameters(data, idExit);
            return document;
        } catch (IOException e) {
            throw new DocumentGenerationException("Error occurred while generating the PDF document.", e);
        }
    }

    @Override
    public void validateData(Map<String, String> data) {
        if (data == null) {
            throw new MissingFieldException("Provided data is null.");
        }

        Set<String> missingFields = new HashSet<>();
        for (String field : REQUIRED_FIELDS) {
            if (!data.containsKey(field) || data.get(field) == null || data.get(field).trim().isEmpty()) {
                missingFields.add(field);
            }
        }

        if (!missingFields.isEmpty()) {
            throw new IllegalArgumentException("The following required fields are missing or empty: " + missingFields);
        }
    }
}