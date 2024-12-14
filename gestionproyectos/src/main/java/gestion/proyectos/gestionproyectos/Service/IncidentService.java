package gestion.proyectos.gestionproyectos.Service;

import gestion.proyectos.gestionproyectos.Entity.Incident;
import gestion.proyectos.gestionproyectos.Repository.IncidentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IncidentService {

    @Autowired
    private IncidentRepository incidentRepository;

    public Incident saveIncident(Incident incident) {
        return incidentRepository.save(incident);
    }

    public Incident getIncidentById(Long id) {
        return incidentRepository.findById(id).orElse(null);
    }

    public void deleteIncident(Long id) {
        incidentRepository.deleteById(id);
    }

    public Incident updateIncident(Incident incident) {
        return incidentRepository.save(incident);
    }

    public List<Incident> getIncidents() {
        return incidentRepository.findAll();
    }
}
