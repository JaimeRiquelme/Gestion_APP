package gestion.proyectos.gestionproyectos.Service;

import gestion.proyectos.gestionproyectos.Entity.Management;
import gestion.proyectos.gestionproyectos.Repository.ManagementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ManagementService {
    
    @Autowired
    ManagementRepository managementRepository;

    // Create
    public Management save(Management management) {
        return managementRepository.save(management);
    }

    // Read
    public List<Management> getAll() {
        return (List<Management>) managementRepository.findAll();
    }

    public Management getById(Long id){
        return managementRepository.findById(id).get();
    }

    // Update
    public Management update(Management management) {
        return managementRepository.save(management);
    }

    // Delete
    public boolean delete(Long id) throws Exception {
        Optional<Management> entity = managementRepository.findById(id);
        if (entity.isPresent()) {
            managementRepository.deleteById(id);
            return true;
        } else {
            throw new Exception("Management " + id + " does not exist");
        }
    }
}
