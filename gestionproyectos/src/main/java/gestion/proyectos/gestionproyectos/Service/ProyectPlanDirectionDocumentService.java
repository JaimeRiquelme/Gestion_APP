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
public class ProyectPlanDirectionDocumentService  implements DocumentService{
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
            "company",
            "proyectPromotor",
            "proyectLeader",
            "introduction",
            "managmentApproach",
            "proyectScope",
            "milestones",
            "scheduleBaseline",
            "wbs",
            "changePlan",
            "comunicationManagmentPlanDescription",
            "proyectInfoCommunication",
            "communicationManagmentDiagram",
            "workLogistic",
            "reportDefinitions",
            "costManagmentPlanDescription",
            "costManagmentMetodology",
            "costMeasurment",
            "costChangeControl",
            "costEstimateMetodology",
            "adquisitionManagmentPlanDescription",
            "adquisitionIdentification",
            "adquisitionPlanning",
            "statementOfWork",
            "scopeManagmentPlanDescription",
            "scopeApproach",
            "scopeDefinition",
            "scopeStatement",
            "requirementGatheringStrategy",
            "verificationAndControlOfScope",
            "scheduleManagmentPlanDescription",
            "scheduleManagmentApproach",
            "activityIdentificationAndSequenciationStrategy",
            "scheduleControl",
            "scheduleRevision",
            "scheduleChangesAndUmbral",
            "qualityManagmentPlanDescription",
            "qualityManagmentApproach",
            "politicsAndNorms",
            "standardsAndRequisitesOfQuality",
            "qualityControl",
            "riskManagmentPlanDescription",
            "riskIdentification",
            "riskCategorizationAndPriority",
            "riskMonitoringAndControl",
            "riskPreventionAndMitigation",
            "riskRegister",
            " resourceManagmentPlanDescription",
            " rolsAndResponsabilities",
            "resourceEstimate",
            "costBaseline",
            "qualityBaseline",
            "proyectPromotorTitle"
    ));

    @Override
    public byte[] generateDocument(Map<String, String> data, Long idExit) throws IOException {
        validateData(data);
        String templatePath = pathResolver.resolve("plan_de_direccion_del_proyecto.tex");

        byte[] Documento = latexService.generateDocument(templatePath, data);

        exitService.updateAssumptionsDocument(idExit, Documento);
        parameterService.saveParameters(data, idExit);

        return Documento;
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
