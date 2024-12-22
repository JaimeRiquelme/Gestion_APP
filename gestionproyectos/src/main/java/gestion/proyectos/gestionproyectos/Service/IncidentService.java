package gestion.proyectos.gestionproyectos.Service;

import gestion.proyectos.gestionproyectos.Entity.Incident;
import gestion.proyectos.gestionproyectos.Entity.Proyect;
import gestion.proyectos.gestionproyectos.Entity.User;
import gestion.proyectos.gestionproyectos.Repository.IncidentRepository;
import gestion.proyectos.gestionproyectos.Repository.ProyectRepository;
import gestion.proyectos.gestionproyectos.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

@Service
public class IncidentService {

    private final IncidentRepository incidentRepository;
    private final ProyectRepository proyectRepository;
    private final UserRepository userRepository;

    @Autowired
    public IncidentService(IncidentRepository incidentRepository, ProyectRepository proyectRepository, UserRepository userRepository) {
        this.incidentRepository = incidentRepository;
        this.proyectRepository = proyectRepository;
        this.userRepository = userRepository;
    }

    // Crear incidente
    public Incident create(Incident incident) {
        // Validar la existencia del proyecto relacionado
        Optional<Proyect> optionalProyect = proyectRepository.findById(incident.getProyect().getIdProyecto());
        if (optionalProyect.isPresent()) {
            incident.setProyect(optionalProyect.get());
        } else {
            throw new RuntimeException("Proyect not found with id " + incident.getProyect().getIdProyecto());
        }

        // Validar la existencia del usuario responsable
        Optional<User> optionalUser = userRepository.findById(incident.getResponsible().getIdUsuario());
        if (optionalUser.isPresent()) {
            incident.setResponsible(optionalUser.get());
        } else {
            throw new RuntimeException("User not found with id " + incident.getResponsible().getIdUsuario());
        }

        // Validar la fecha de registro
        validateDate(incident.getRegistrationDate());

        return incidentRepository.save(incident);
    }

    // Obtener todos los incidentes
    public List<Incident> getAll() {
        return incidentRepository.findAll();
    }

    // Obtener incidente por ID
    public Incident getById(Long id) {
        return incidentRepository.findById(id).orElseThrow(() -> new RuntimeException("Incident not found with id " + id));
    }

    // Actualizar incidente
    public Incident update(Long id, Incident incidentDetails) {
        Incident existingIncident = incidentRepository.findById(id).orElseThrow(() -> new RuntimeException("Incident not found with id " + id));

        if (incidentDetails.getDescription() != null) {
            existingIncident.setDescription(incidentDetails.getDescription());
        }
        if (incidentDetails.getRegistrationDate() != null) {
            validateDate(incidentDetails.getRegistrationDate()); // Validar fecha
            existingIncident.setRegistrationDate(incidentDetails.getRegistrationDate());
        }
        if (incidentDetails.getState() != null) {
            existingIncident.setState(incidentDetails.getState());
        }
        if (incidentDetails.getPriority() != null) {
            existingIncident.setPriority(incidentDetails.getPriority());
        }

        // Validar y actualizar proyecto relacionado, si se proporciona
        if (incidentDetails.getProyect() != null && incidentDetails.getProyect().getIdProyecto() != null) {
            Optional<Proyect> optionalProyect = proyectRepository.findById(incidentDetails.getProyect().getIdProyecto());
            if (optionalProyect.isPresent()) {
                existingIncident.setProyect(optionalProyect.get());
            } else {
                throw new RuntimeException("Proyect not found with id " + incidentDetails.getProyect().getIdProyecto());
            }
        }

        // Validar y actualizar usuario responsable, si se proporciona
        if (incidentDetails.getResponsible() != null && incidentDetails.getResponsible().getIdUsuario() != null) {
            Optional<User> optionalUser = userRepository.findById(incidentDetails.getResponsible().getIdUsuario());
            if (optionalUser.isPresent()) {
                existingIncident.setResponsible(optionalUser.get());
            } else {
                throw new RuntimeException("User not found with id " + incidentDetails.getResponsible().getIdUsuario());
            }
        }

        return incidentRepository.save(existingIncident);
    }

    // Eliminar incidente
    public void delete(Long id) {
        if (incidentRepository.existsById(id)) {
            incidentRepository.deleteById(id);
        } else {
            throw new RuntimeException("Incident not found with id " + id);
        }
    }

    // Método para validar fechas
    private void validateDate(String date) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate.parse(date, formatter);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("La fecha debe estar en el formato AAAA-MM-DD y ser válida. Ejemplo de fecha válida: 2024-01-01");
        }
    }
}