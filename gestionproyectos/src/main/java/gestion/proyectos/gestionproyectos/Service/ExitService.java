package gestion.proyectos.gestionproyectos.Service;

import gestion.proyectos.gestionproyectos.Entity.Exit;
import gestion.proyectos.gestionproyectos.Repository.ExitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExitService {
    
    @Autowired
    ExitRepository exitRepository;

    // Create
    public Exit save(Exit exit) {
        return exitRepository.save(exit);
    }

    // Read
    public List<Exit> getAll() {
        return (List<Exit>) exitRepository.findAll();
    }

    public Exit getById(Long id) {
        return exitRepository.findById(id).orElse(null);
    }

    // Update
    public Exit update(Exit exit) {
        return exitRepository.save(exit);
    }

    // Delete
    public boolean delete(Long id) throws Exception {
        Optional<Exit> entity = exitRepository.findById(id);
        if (entity.isPresent()) {
            exitRepository.deleteById(id);
            return true;
        } else {
            throw new Exception("Exit " + id + " does not exist");
        }
    }
}
