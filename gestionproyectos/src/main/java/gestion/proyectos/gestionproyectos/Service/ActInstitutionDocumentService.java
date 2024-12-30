package gestion.proyectos.gestionproyectos.Service;

import gestion.proyectos.gestionproyectos.Entity.Exit;
import gestion.proyectos.gestionproyectos.Entity.Management;
import gestion.proyectos.gestionproyectos.Entity.Proyect;
import gestion.proyectos.gestionproyectos.Entity.Process;
import gestion.proyectos.gestionproyectos.exception.DocumentGenerationException;
import gestion.proyectos.gestionproyectos.exception.MissingFieldException;
import gestion.proyectos.gestionproyectos.exception.TemplateNotFoundException;
import gestion.proyectos.gestionproyectos.util.TemplatePathResolver;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class ActInstitutionDocumentService implements DocumentService {

    @Autowired
    private LatexService latexService;

    @Autowired
    private TemplatePathResolver pathResolver;

    @Autowired
    private ExitService exitService;

    @Autowired
    private ParameterService parameterService;

    @Autowired
    private ManagementService managementService;

    @Autowired
    private ProcessService processService;



    private static final Set<String> REQUIRED_FIELDS = new HashSet<>(Arrays.asList(
            "proyectName",
            "idProyect",
            "company",
            "proyectPromotor",
            "proyectLeader",
            "elaborationDate",
            "proyectPurpouse",
            "proyectDescription",
            "proyectStakeholders",
            "preliminaryScope",
            "descriptionObjectives",
            "objectivesMission",
            "objectivesVision",
            "objectivesValues",
            "successCriteria",
            "deliverables",
            "objectivesSpecific",
            "preliminaryRisks",
            "milestones",
            "rolesAndResponsabilities",
            "descriptionRequirements",
            "functionalRequirements",
            "nonFunctionalRequirements"
    ));

    @Override
    public byte[] generateDocument(Map<String, String> data, Long idExit) throws IOException {
        validateData(data);

        String templatePath;
        try {
            templatePath = pathResolver.resolve("acta_de_constitucion.tex");
        } catch (Exception e) {
            throw new TemplateNotFoundException("The template 'acta_de_constitucion.tex' was not found.");
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

    @Transactional
    public byte[] generateInstitutionDocumentProcess(Map<String, String> requestData, Long idProyecto) {
        try {
            // 1. Crear Management
            Management management = new Management();
            management.setNameManagement("Gestión de Integración");
            management.setDescription("Gestión de Integración del Proyecto");

            Proyect proyect = new Proyect();
            proyect.setIdProyecto(idProyecto);
            management.setProyect(proyect);

            Management savedManagement = managementService.create(management);

            // 2. Crear Process
            Process process = new Process();
            process.setNameProcess("Acta de Constitución del Proyecto");
            process.setDescription("Acta de Constitución del Proyecto");
            process.setStateProcess("Activo");
            process.setStartDatePlanned(LocalDate.now().toString());
            process.setEndDatePlanned(LocalDate.now().toString());
            process.setStartDateReal(LocalDate.now().toString());
            process.setEndDateReal(LocalDate.now().toString());
            process.setManagement(savedManagement);

            Process savedProcess = processService.create(process);

            // 3. Crear Exit
            Exit exit = new Exit();
            exit.setNameExit("Acta de Constitución del Proyecto");
            exit.setState("Acta de Constitución del Proyecto");
            exit.setDateCreation(LocalDate.now().toString());
            exit.setDateValidation(LocalDate.now().toString());
            exit.setPriority("Alta");
            exit.setResponsible(requestData.get("proyectLeader")); // Obtener del mapa de datos
            exit.setDescription("Acta de Constitución del Proyecto");
            exit.setProcess(savedProcess);

            Exit savedExit = exitService.create(exit);

            // 4. Generar el documento PDF
            byte[] pdfContent = generateDocument(requestData, savedExit.getIdExit());

            // 5. Actualizar el Exit con el documento
            exitService.updateAssumptionsDocument(savedExit.getIdExit(), pdfContent);
            parameterService.saveParameters(requestData, savedExit.getIdExit());

            return pdfContent;

        } catch (Exception e) {
            throw new DocumentGenerationException("Error en el proceso de generación del documento: " + e.getMessage(), e);
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
            throw new MissingFieldException("The following required fields are missing or empty: " + missingFields);
        }
    }
}
