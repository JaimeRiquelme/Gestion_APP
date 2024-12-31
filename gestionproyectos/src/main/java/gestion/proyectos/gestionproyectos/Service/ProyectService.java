package gestion.proyectos.gestionproyectos.Service;

import gestion.proyectos.gestionproyectos.Entity.*;
import gestion.proyectos.gestionproyectos.Repository.ProyectRepository;
import gestion.proyectos.gestionproyectos.Repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProyectService {

    @Autowired
    private EntityManager entityManager;

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
        return proyectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proyect not found with id " + id));
    }

    // Read by User
    public List<Proyect> getProyectsByUser(User user) {
        return proyectRepository.findByUser_IdUsuario(user.getIdUsuario());
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
    @Transactional
    public void delete(Long id) {
        Proyect proyect = proyectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se encontró el proyecto con ID: " + id));
        try {
            String[] queries = {
                    "DELETE FROM parameter WHERE exit_id IN (SELECT id_exit FROM exit WHERE id_process IN (SELECT id_process FROM process WHERE id_management IN (SELECT id_management FROM management WHERE id_proyect = :id)))", // Eliminar los parameters relacionados con los exits
                    "DELETE FROM exit WHERE id_process IN (SELECT id_process FROM process WHERE id_management IN (SELECT id_management FROM management WHERE id_proyect = :id))", // Eliminar los exits relacionados con los process
                    "DELETE FROM process WHERE id_management IN (SELECT id_management FROM management WHERE id_proyect = :id)", // Eliminar los process relacionados con los managements
                    "DELETE FROM management WHERE id_proyect = :id", // Eliminar los managements relacionados
                    "DELETE FROM incident WHERE id_proyect = :id",  // Eliminar los incidentes relacionados
                    "DELETE FROM lessons WHERE id_proyect = :id",   // Eliminar las lecciones relacionadas
                    "DELETE FROM proyects WHERE id_proyecto = :id"  // Eliminar el proyecto
            };

            try {
                // Primero eliminamos los parameters asociados a los exits
                int rowsAffected = entityManager.createNativeQuery(queries[0])
                        .setParameter("id", id)
                        .executeUpdate();
                System.out.println("Parameters eliminados, filas afectadas: " + rowsAffected);

                // Luego eliminamos los exits asociados a los process
                rowsAffected = entityManager.createNativeQuery(queries[1])
                        .setParameter("id", id)
                        .executeUpdate();
                System.out.println("Exits eliminados, filas afectadas: " + rowsAffected);

                // Eliminar los process asociados a los managements
                rowsAffected = entityManager.createNativeQuery(queries[2])
                        .setParameter("id", id)
                        .executeUpdate();
                System.out.println("Processes eliminados, filas afectadas: " + rowsAffected);

                // Después eliminamos los managements
                rowsAffected = entityManager.createNativeQuery(queries[3])
                        .setParameter("id", id)
                        .executeUpdate();
                System.out.println("Gestiones eliminadas, filas afectadas: " + rowsAffected);

                // Luego eliminamos los incidentes
                rowsAffected = entityManager.createNativeQuery(queries[4])
                        .setParameter("id", id)
                        .executeUpdate();
                System.out.println("Incidentes eliminados, filas afectadas: " + rowsAffected);

                // Luego eliminamos las lecciones
                rowsAffected = entityManager.createNativeQuery(queries[5])
                        .setParameter("id", id)
                        .executeUpdate();
                System.out.println("Lecciones eliminadas, filas afectadas: " + rowsAffected);

                // Finalmente eliminamos el proyecto
                rowsAffected = entityManager.createNativeQuery(queries[6])
                        .setParameter("id", id)
                        .executeUpdate();
                System.out.println("Proyecto eliminado, filas afectadas: " + rowsAffected);

                System.out.println("Proyecto y sus relaciones eliminadas exitosamente.");
            } catch (Exception e) {
                System.err.println("Error al eliminar el proyecto: " + e.getMessage());
                throw new RuntimeException("Error al eliminar el proyecto y sus relaciones: " + e.getMessage());
            }


        } catch (Exception e) {
            System.err.println("Error al eliminar el proyecto: " + e.getMessage());
            throw new RuntimeException("Error al eliminar el proyecto y sus relaciones: " + e.getMessage());
        }
    }

    private void validateDate(String date) {
        if (date != null) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate.parse(date, formatter);
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("La fecha debe estar en el formato AAAA-MM-DD y ser una fecha válida. Ejemplo de fecha válida: 2024-12-31");
            }
        }
    }

    public List<Management> managementsByIdProyect(Long idProyect) {
        Proyect proyect = getById(idProyect);
        return proyect.getManagements();
    }

    public List<Incident> getIncidentsByProyectId(Long idProyect) {
        Proyect proyect = getById(idProyect);
        return proyect.getIncidents();
    }

    public List<Lessons> getLessonsByProyectId(Long idProyect) {
        Proyect proyect = getById(idProyect);
        return proyect.getLessons();
    }
}