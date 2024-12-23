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
public class RegistroLeccionesAprendidasDocumentService implements DocumentService {

    @Autowired
    private LatexService latexService;

    @Autowired
    private TemplatePathResolver pathResolver;

    @Autowired
    private ExitService exitService;

    @Autowired
    private ParameterService parameterService;

    private static final Set<String> REQUIRED_FIELDS = new HashSet<>(Arrays.asList(
            "projectName",
            "idProject",
            "lessonArea",
            "lessonType",
            "lessonNumber",
            "lessonTitle",
            "situationDescription",
            "objectivesImpact",
            "implementedActions",
            "lessonRecommendation"
    ));

    @Override
    public byte[] generateDocument(Map<String, String> data, Long idExit) throws IOException {
        validateData(data);
        String templatePath = pathResolver.resolve("registro_de_lecciones_aprendidas.tex");

        byte[] documento = latexService.generateDocument(templatePath, data);

        exitService.updateAssumptionsDocument(idExit, documento);
        parameterService.saveParameters(data, idExit);

        return documento;
    }

    @Override
    public void validateData(Map<String, String> data) {
        if (data == null) {
            throw new IllegalArgumentException("Los datos no pueden ser nulos");
        }

        Set<String> camposFaltantes = new HashSet<>();
        for (String campo : REQUIRED_FIELDS) {
            if (!data.containsKey(campo) || data.get(campo) == null || data.get(campo).trim().isEmpty()) {
                camposFaltantes.add(campo);
            }
        }

        if (!camposFaltantes.isEmpty()) {
            throw new IllegalArgumentException("Faltan campos requeridos: " + camposFaltantes);
        }
    }
}