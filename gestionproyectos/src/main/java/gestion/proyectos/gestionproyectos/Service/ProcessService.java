package gestion.proyectos.gestionproyectos.Service;

import gestion.proyectos.gestionproyectos.Entity.Process;
import gestion.proyectos.gestionproyectos.Entity.Management;
import gestion.proyectos.gestionproyectos.Repository.ProcessRepository;
import gestion.proyectos.gestionproyectos.Repository.ManagementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

@Service
public class ProcessService {

    private final ProcessRepository processRepository;
    private final ManagementRepository managementRepository;

    @Autowired
    public ProcessService(ProcessRepository processRepository, ManagementRepository managementRepository) {
        this.processRepository = processRepository;
        this.managementRepository = managementRepository;
    }

    // Crear un proceso
    public Process create(Process process) {
        // Validar la existencia del Management relacionado
        if (process.getManagement() != null && process.getManagement().getIdManagement() != null) {
            Optional<Management> optionalManagement = managementRepository.findById(process.getManagement().getIdManagement());
            if (optionalManagement.isPresent()) {
                process.setManagement(optionalManagement.get());
            } else {
                throw new RuntimeException("Management not found with id " + process.getManagement().getIdManagement());
            }
        }

        // Validar fechas individualmente
        validateDate(process.getStartDatePlanned());
        validateDate(process.getEndDatePlanned());
        validateDate(process.getStartDateReal());
        validateDate(process.getEndDateReal());

        return processRepository.save(process);
    }

    // Obtener todos los procesos
    public List<Process> getAll() {
        return processRepository.findAll();
    }

    // Obtener un proceso por ID
    public Process getById(Long id) {
        return processRepository.findById(id).orElseThrow(() -> new RuntimeException("Process not found with id " + id));
    }

    // Actualizar un proceso
    public Process update(Long id, Process processDetails) {
        Process existingProcess = processRepository.findById(id).orElseThrow(() -> new RuntimeException("Process not found with id " + id));

        if (processDetails.getNameProcess() != null) {
            existingProcess.setNameProcess(processDetails.getNameProcess());
        }
        if (processDetails.getDescription() != null) {
            existingProcess.setDescription(processDetails.getDescription());
        }
        if (processDetails.getStateProcess() != null) {
            existingProcess.setStateProcess(processDetails.getStateProcess());
        }
        if (processDetails.getStartDatePlanned() != null) {
            validateDate(processDetails.getStartDatePlanned());
            existingProcess.setStartDatePlanned(processDetails.getStartDatePlanned());
        }
        if (processDetails.getEndDatePlanned() != null) {
            validateDate(processDetails.getEndDatePlanned());
            existingProcess.setEndDatePlanned(processDetails.getEndDatePlanned());
        }
        if (processDetails.getStartDateReal() != null) {
            validateDate(processDetails.getStartDateReal());
            existingProcess.setStartDateReal(processDetails.getStartDateReal());
        }
        if (processDetails.getEndDateReal() != null) {
            validateDate(processDetails.getEndDateReal());
            existingProcess.setEndDateReal(processDetails.getEndDateReal());
        }

        // Validar y actualizar Management relacionado
        if (processDetails.getManagement() != null && processDetails.getManagement().getIdManagement() != null) {
            Optional<Management> optionalManagement = managementRepository.findById(processDetails.getManagement().getIdManagement());
            if (optionalManagement.isPresent()) {
                existingProcess.setManagement(optionalManagement.get());
            } else {
                throw new RuntimeException("Management not found with id " + processDetails.getManagement().getIdManagement());
            }
        }

        // Validar fechas actualizadas de manera individual
        validateDate(existingProcess.getStartDatePlanned());
        validateDate(existingProcess.getEndDatePlanned());
        validateDate(existingProcess.getStartDateReal());
        validateDate(existingProcess.getEndDateReal());

        return processRepository.save(existingProcess);
    }

    // Eliminar un proceso
    public void delete(Long id) {
        System.out.println("Hola");
        if (processRepository.existsById(id)) {
            processRepository.deleteById(id);
        } else {
            throw new RuntimeException("Process not found with id " + id);
        }
    }

    // Método para validar una fecha individualmente
    private void validateDate(String date) {
        if (date != null && !date.isBlank()) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate.parse(date, formatter);
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("La fecha debe estar en el formato AAAA-MM-DD y ser válida. Ejemplo de fecha válida: 2024-01-01");
            }
        }
    }
}