package gestion.proyectos.gestionproyectos.Controller;

import gestion.proyectos.gestionproyectos.Entity.Incident;
import gestion.proyectos.gestionproyectos.Service.IncidentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("/api/v1/incidents")
public class IndicentController {

    @Autowired
    private IncidentService incidentService;

    @PostMapping("/CreateIncident")
    public ResponseEntity<Incident> createIncident(@RequestBody Incident incident) {
        return new ResponseEntity<>(incidentService.saveIncident(incident), HttpStatus.CREATED);
    }

    @PostMapping("/GetIncidentById")
    public ResponseEntity<Incident> getIncidentById(@RequestBody Long id) {
        Incident incident = incidentService.getIncidentById(id);
        return incident != null ?
                new ResponseEntity<>(incident, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/DeleteIncident")
    public ResponseEntity<Void> deleteIncident(@RequestBody Long id) {
        incidentService.deleteIncident(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/UpdateIncident")
    public ResponseEntity<Incident> updateIncident(@RequestBody Incident incident) {
        return new ResponseEntity<>(incidentService.updateIncident(incident), HttpStatus.OK);
    }

    @PostMapping("/GetIncidents")
    public ResponseEntity<List<Incident>> getIncidents() {
        return new ResponseEntity<>(incidentService.getIncidents(), HttpStatus.OK);
    }

}
