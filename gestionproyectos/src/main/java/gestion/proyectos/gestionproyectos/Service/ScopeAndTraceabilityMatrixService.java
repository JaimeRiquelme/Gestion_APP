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
public class ScopeAndTraceabilityMatrixService {

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
            "proyectLeader",
            "qaLeader",
            "elaborationDate",
            "documentationAndVersionControl",
            "requirementsTraceabilityMatrix"
    ));


    public byte[] generateDocument1(Map<String, String> data, Long idExit) throws IOException {
        validateData1(data);

        String templatePath;
        try {
            templatePath = pathResolver.resolve("Documentación de requisitos y Matriz de trazabilidad de requisitos.tex");
        } catch (Exception e) {
            throw new TemplateNotFoundException("The template 'Documento de requisitos y Matriz de trazabilidad.tex' was not found.");
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


    public void validateData1(Map<String, String> data) {
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
            throw new MissingFieldException("The following required fields are missing or empty: " + missingFields);
        }
    }
}
