package gestion.proyectos.gestionproyectos.Service;

import gestion.proyectos.gestionproyectos.util.TemplatePathResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class AssumptionsDocumentService implements DocumentService {

    @Autowired
    private LatexService latexService;

    @Autowired
    private TemplatePathResolver pathResolver;

    private static final Set<String> REQUIRED_FIELDS = new HashSet<>(Arrays.asList(
            "proyectName",
            "idProyect",
            "elaborationDate",
            "proyectObjectives",
            "proyectOutputs",
            "proyectTasks",
            "proyectOutOfScope",
            "proyectAssumptions",
            "proyectStartDate",
            "kickOffDate",
            "proyectEndDate",
            "proyectDeadlines",
            "proyectMilestones",
            "budgetRestrictions",
            "qualityRestrictions",
            "teamRestrictions",
            "juridicalRestrictions",
            "timeEstimate",
            "approvals"
    ));

    @Override
    public byte[] generateDocument(Map<String, String> data) throws IOException {
        validateData(data);
        String templatePath = pathResolver.resolve("registro_de_supuestos.tex");
        return latexService.generateDocument(templatePath, data);
    }

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