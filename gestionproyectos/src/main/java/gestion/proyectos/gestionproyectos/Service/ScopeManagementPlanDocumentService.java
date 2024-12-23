package gestion.proyectos.gestionproyectos.Service;

import gestion.proyectos.gestionproyectos.util.TemplatePathResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Service responsible for generating the Scope Management Plan PDF document.
 */
@Service
public class ScopeManagementPlanDocumentService implements DocumentService {

    @Autowired
    private LatexService latexService;

    @Autowired
    private TemplatePathResolver pathResolver;

    @Autowired
    private ExitService exitService;

    @Autowired
    private ParameterService parameterService;

    // Define the required fields for the Scope Management Plan document
    private static final Set<String> REQUIRED_FIELDS = new HashSet<>(Arrays.asList(
            "proyectName",
            "idProject",
            "productLeader",
            "projectLeader",
            "elaborationDate",
            "scopeApproach",
            "scopePlanObjectives",
            "scopeDefinition",
            "scopeExclusions",
            "scopeLimitations",
            "scopeStatement",
            "scopeOutputs",
            "projectAssumptions",
            "requirementGathering",
            "requirementsGatheringStrategy",
            "requirementsAnalysis",
            "requirementsDocumentation",
            "verificationAndControlOfScope",
            "scopeApproval",
            "stakeholdersApproval",
            "changeControlSystem",
            "edtApproval"
    ));

    /**
     * Generates the Scope Management Plan PDF document.
     *
     * @param data    A map containing the necessary data fields.
     * @param idExit  The exit identifier associated with the document.
     * @return        A byte array representing the generated PDF document.
     * @throws IOException If an error occurs during document generation.
     */
    @Override
    public byte[] generateDocument(Map<String, String> data, Long idExit) throws IOException {
        validateData(data);
        String templatePath = pathResolver.resolve("plan_de_gestion_del_alcance.tex");

        byte[] document = latexService.generateDocument(templatePath, data);

        exitService.updateAssumptionsDocument(idExit, document);
        parameterService.saveParameters(data, idExit);

        return document;
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
            throw new IllegalArgumentException("Data cannot be null");
        }

        Set<String> missingFields = new HashSet<>();
        for (String field : REQUIRED_FIELDS) {
            if (!data.containsKey(field) || data.get(field) == null || data.get(field).trim().isEmpty()) {
                missingFields.add(field);
            }
        }

        if (!missingFields.isEmpty()) {
            throw new IllegalArgumentException("Missing required fields: " + missingFields);
        }
    }
}