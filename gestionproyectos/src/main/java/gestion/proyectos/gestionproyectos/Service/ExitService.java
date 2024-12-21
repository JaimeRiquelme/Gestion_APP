package gestion.proyectos.gestionproyectos.Service;

import gestion.proyectos.gestionproyectos.Entity.Exit;
import gestion.proyectos.gestionproyectos.Entity.Process;
import gestion.proyectos.gestionproyectos.Repository.ExitRepository;
import gestion.proyectos.gestionproyectos.Repository.ProcessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

@Service
public class ExitService {

    private final ExitRepository exitRepository;
    private final ProcessRepository processRepository;

    @Autowired
    public ExitService(ExitRepository exitRepository, ProcessRepository processRepository) {
        this.exitRepository = exitRepository;
        this.processRepository = processRepository;
    }

    // Crear un Exit asociado a un proceso
    public Exit create(Exit exit) {
        // Validar proceso relacionado
        if (exit.getIdProcess() != null) {
            Optional<Process> optionalProcess = processRepository.findById(exit.getIdProcess());
            if (optionalProcess.isPresent()) {
                exit.setProcess(optionalProcess.get());
            } else {
                throw new RuntimeException("Process not found with id " + exit.getIdProcess());
            }
        }

        // Validar fechas de creación y validación del Exit
        validateDate(exit.getDateCreation());
        validateDate(exit.getDateValidation());

        return exitRepository.save(exit);
    }

    // Obtener todos los Exit
    public List<Exit> getAll() {
        return exitRepository.findAll();
    }

    // Obtener un Exit por su ID
    public Exit getById(Long id) {
        return exitRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Exit not found with id " + id));
    }

    // Actualizar un Exit
    public Exit update(Long id, Exit exitDetails) {
        Exit existingExit = exitRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Exit not found with id " + id));

        // Actualizar atributos individualmente si son proporcionados
        if (exitDetails.getNameExit() != null) {
            existingExit.setNameExit(exitDetails.getNameExit());
        }
        if (exitDetails.getState() != null) {
            existingExit.setState(exitDetails.getState());
        }
        if (exitDetails.getDateCreation() != null) {
            validateDate(exitDetails.getDateCreation());
            existingExit.setDateCreation(exitDetails.getDateCreation());
        }
        if (exitDetails.getDateValidation() != null) {
            validateDate(exitDetails.getDateValidation());
            existingExit.setDateValidation(exitDetails.getDateValidation());
        }
        if (exitDetails.getPriority() != null) {
            existingExit.setPriority(exitDetails.getPriority());
        }
        if (exitDetails.getResponsible() != null) {
            existingExit.setResponsible(exitDetails.getResponsible());
        }
        if (exitDetails.getDescription() != null) {
            existingExit.setDescription(exitDetails.getDescription());
        }

        // Validar y asociar nuevo proceso si es necesario
        if (exitDetails.getIdProcess() != null) {
            Optional<Process> optionalProcess = processRepository.findById(exitDetails.getIdProcess());
            if (optionalProcess.isPresent()) {
                existingExit.setProcess(optionalProcess.get());
            } else {
                throw new RuntimeException("Process not found with id " + exitDetails.getIdProcess());
            }
        }

        return exitRepository.save(existingExit);
    }

    // Eliminar un Exit por su ID
    public void delete(Long id) {
        if (exitRepository.existsById(id)) {
            exitRepository.deleteById(id);
        } else {
            throw new RuntimeException("Exit not found with id " + id);
        }
    }

    // Dejar intacto el método updateAssumptionsDocument
    public void updateAssumptionsDocument(Long idExit, byte[] Documento) {
        Exit exit = exitRepository.findById(idExit).orElse(null);
        if (exit != null) {
            exit.setDocument(Documento);
            exitRepository.save(exit);
        }
    }

    // Método para validar fechas
    private void validateDate(String date) {
        if (date != null && !date.isBlank()) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate.parse(date, formatter);
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("La fecha debe estar en el formato AAAA-MM-DD y ser válida. Ejemplo de fecha válida: 2024-12-31");
            }
        }
    }
}