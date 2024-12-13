package gestion.proyectos.gestionproyectos.Service;

import gestion.proyectos.gestionproyectos.Entity.Proyect;
import gestion.proyectos.gestionproyectos.Repository.ProyectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProyectService {

    @Autowired
    private ProyectRepository proyectRepository;

    public Proyect saveProyect(Proyect proyect) {
        return proyectRepository.save(proyect);
    }

    public Proyect getProyectById(Long id) {
        return proyectRepository.findById(id).orElse(null);
    }

    public void deleteProyect(Long id) {
        proyectRepository.deleteById(id);
    }

    public Proyect updateProyect(Proyect proyect) {
        return proyectRepository.save(proyect);
    }

    public Iterable<Proyect> getProyects() {
        return proyectRepository.findAll();
    }

}
