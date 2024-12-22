package gestion.proyectos.gestionproyectos.Service;

import gestion.proyectos.gestionproyectos.Entity.Management;
import gestion.proyectos.gestionproyectos.Entity.Proyect;
import gestion.proyectos.gestionproyectos.Repository.ManagementRepository;
import gestion.proyectos.gestionproyectos.Repository.ProyectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ManagementService {

    private final ManagementRepository managementRepository;
    private final ProyectRepository proyectRepository;

    @Autowired
    public ManagementService(ManagementRepository managementRepository, ProyectRepository proyectRepository) {
        this.managementRepository = managementRepository;
        this.proyectRepository = proyectRepository;
    }

    // Crear Management
    public Management create(Management management) {
        // Validar la existencia del proyecto asociado
        if (management.getProyect() != null && management.getProyect().getIdProyecto() != null) {
            Optional<Proyect> optionalProyect = proyectRepository.findById(management.getProyect().getIdProyecto());
            if (optionalProyect.isPresent()) {
                management.setProyect(optionalProyect.get());
            } else {
                throw new RuntimeException("Proyect not found with id " + management.getProyect().getIdProyecto());
            }
        }

        return managementRepository.save(management);
    }

    // Obtener todas las gestiones
    public List<Management> getAll() {
        return (List<Management>) managementRepository.findAll();
    }

    // Obtener Management por ID
    public Management getById(Long id) {
        return managementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Management not found with id " + id));
    }

    // Actualizar Management
    public Management update(Long id, Management managementDetails) {
        Management existingManagement = managementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Management not found with id " + id));

        if (managementDetails.getNameManagement() != null) {
            existingManagement.setNameManagement(managementDetails.getNameManagement());
        }
        if (managementDetails.getDescription() != null) {
            existingManagement.setDescription(managementDetails.getDescription());
        }

        // Validar y actualizar proyecto relacionado, si se proporciona
        if (managementDetails.getProyect() != null && managementDetails.getProyect().getIdProyecto() != null) {
            Optional<Proyect> optionalProyect = proyectRepository.findById(managementDetails.getProyect().getIdProyecto());
            if (optionalProyect.isPresent()) {
                existingManagement.setProyect(optionalProyect.get());
            } else {
                throw new RuntimeException("Proyect not found with id " + managementDetails.getProyect().getIdProyecto());
            }
        }

        return managementRepository.save(existingManagement);
    }

    // Eliminar Management
    public void delete(Long id) {
        if (managementRepository.existsById(id)) {
            managementRepository.deleteById(id);
        } else {
            throw new RuntimeException("Management not found with id " + id);
        }
    }
}