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
public class ChangeRequestDocumentService implements DocumentService {

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
            "requestNumber",
            "applicant",
            "requestDate",
            "applicantContact",
            "priority",
            "changeArticle",
            "changeDescription"
    ));

    @Override
    public byte[] generateDocument(Map<String, String> data,Long idExit) throws IOException {
        validateData(data);
        String templatePath = pathResolver.resolve("solicitud_de_cambio.tex");

        byte[] Documento = latexService.generateDocument(templatePath, data);

        exitService.updateAssumptionsDocument(idExit, Documento);
        parameterService.saveParameters(data, idExit);

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