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

/**
 * Service responsible for generating the Work Performance Review PDF document.
 */
@Service
public class WorkPerformanceReviewDocumentService implements DocumentService {

    @Autowired
    private LatexService latexService;

    @Autowired
    private TemplatePathResolver pathResolver;

    @Autowired
    private ExitService exitService;

    @Autowired
    private ParameterService parameterService;

    // Define the required fields for the Work Performance Review document
    private static final Set<String> REQUIRED_FIELDS = new HashSet<>(Arrays.asList(
            "employeeName",
            "employeeDepartment",
            "employeeId",
            "reviewerName",
            "employeePosition",
            "reviewerTitle",
            "lastReviewDate",
            "currentDate",
            "employeeCharacteristicsEvaluations",
            "currentGoalsReview",
            "futureGoals",
            "comments"
    ));

    /**
     * Generates the Work Performance Review PDF document.
     *
     * @param data    A map containing the necessary data fields.
     * @param idExit  The exit identifier associated with the document.
     * @return        A byte array representing the generated PDF document.
     * @throws IOException If an error occurs during document generation.
     */
    @Override
    public byte[] generateDocument(Map<String, String> data, Long idExit) throws IOException {
        validateData(data);
        String templatePath;
        try {
            templatePath = pathResolver.resolve("revision_desempeno_de_trabajo.tex");
        } catch (Exception e) {
            throw new TemplateNotFoundException("The template 'revision_desempeno_de_trabajo.tex' was not found.");
        }

        try {
            byte[] documento = latexService.generateDocument(templatePath, data);
            exitService.updateAssumptionsDocument(idExit, documento);
            parameterService.saveParameters(data, idExit);
            return documento;
        } catch (IOException e) {
            throw new DocumentGenerationException("Error occurred while generating the PDF document.", e);
        }
    }

    /**
     * Validates the provided data to ensure all required fields are present and not empty.
     *
     * @param data The data map to validate.
     * @throws IllegalArgumentException If any required fields are missing or empty.
     */
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