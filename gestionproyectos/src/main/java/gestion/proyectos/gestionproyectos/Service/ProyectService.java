package gestion.proyectos.gestionproyectos.Service;

import gestion.proyectos.gestionproyectos.Entity.Proyect;
import gestion.proyectos.gestionproyectos.Entity.User;
import gestion.proyectos.gestionproyectos.Repository.ProyectRepository;
import gestion.proyectos.gestionproyectos.Repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

@Service
public class ProyectService {

    private final ProyectRepository proyectRepository;
    private final UserRepository userRepository;

    public ProyectService(ProyectRepository proyectRepository, UserRepository userRepository) {
        this.proyectRepository = proyectRepository;
        this.userRepository = userRepository;
    }

    // Create
    public Proyect create(Proyect proyect) {
        Optional<User> optionalUser = userRepository.findById(proyect.getUser().getIdUsuario());
        if (optionalUser.isPresent()) {
            proyect.setUser(optionalUser.get());
        } else {
            throw new RuntimeException("User not found with id " + proyect.getUser().getIdUsuario());
        }
        validateDate(proyect.getStartDate());
        validateDate(proyect.getEstimatedEndDate());
        validateDate(proyect.getRealEstimatedEndDate());
        return proyectRepository.save(proyect);
    }

    // Read
    public List<Proyect> getAll() {
        return proyectRepository.findAll();
    }

    public Proyect getById(Long id) {
        return proyectRepository.findById(id).orElse(null);
    }

    // Update
    public Proyect update(Long id, Proyect proyectDetails) {
        Optional<Proyect> optionalProyect = proyectRepository.findById(id);
        if (optionalProyect.isPresent()) {
            Proyect existingProyect = optionalProyect.get();
            if (proyectDetails.getNameProyect() != null) {
                existingProyect.setNameProyect(proyectDetails.getNameProyect());
            }
            if (proyectDetails.getDescription() != null) {
                existingProyect.setDescription(proyectDetails.getDescription());
            }
            if (proyectDetails.getOrganization() != null) {
                existingProyect.setOrganization(proyectDetails.getOrganization());
            }
            if (proyectDetails.getStartDate() != null) {
                validateDate(proyectDetails.getStartDate());
                existingProyect.setStartDate(proyectDetails.getStartDate());
            }
            if (proyectDetails.getEstimatedEndDate() != null) {
                validateDate(proyectDetails.getEstimatedEndDate());
                existingProyect.setEstimatedEndDate(proyectDetails.getEstimatedEndDate());
            }
            if (proyectDetails.getRealEstimatedEndDate() != null) {
                validateDate(proyectDetails.getRealEstimatedEndDate());
                existingProyect.setRealEstimatedEndDate(proyectDetails.getRealEstimatedEndDate());
            }
            return proyectRepository.save(existingProyect);
        } else {
            throw new RuntimeException("Proyect not found with id " + id);
        }
    }

    // Delete
    public void delete(Long id) {
        Optional<Proyect> proyect = proyectRepository.findById(id);
        if (proyect.isPresent()) {
            proyectRepository.deleteById(id);
        } else {
            throw new RuntimeException("Proyect not found with id " + id);
        }
    }

    private void validateDate(String date) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate.parse(date, formatter);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("La fecha debe estar en el formato AAAA-MM-DD y ser una fecha válida. Ejemplo de fecha válida: 2024-12-31");
        }
    }
}
