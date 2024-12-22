package gestion.proyectos.gestionproyectos.Controller;

import gestion.proyectos.gestionproyectos.Entity.Incident;
import gestion.proyectos.gestionproyectos.Service.IncidentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/incident")
public class IncidentController {

    private final IncidentService incidentService;

    public IncidentController(IncidentService incidentService) {
        this.incidentService = incidentService;
    }

    // Create Incident
    @PostMapping("/create")
    public ResponseEntity<Incident> create(@RequestBody Incident incident) {
        return new ResponseEntity<>(incidentService.create(incident), HttpStatus.CREATED);
    }

    // Get All Incidents
    @GetMapping("/getAll")
    public ResponseEntity<List<Incident>> getAll() {
        return new ResponseEntity<>(incidentService.getAll(), HttpStatus.OK);
    }

    // Get Incident by ID
    @GetMapping("/getById/{id}")
    public ResponseEntity<Incident> getById(@PathVariable Long id) {
        try {
            Incident incident = incidentService.getById(id);
            return new ResponseEntity<>(incident, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Update Incident
    @PutMapping("/update/{id}")
    public ResponseEntity<Incident> update(@PathVariable Long id, @RequestBody Incident incident) {
        try {
            Incident updatedIncident = incidentService.update(id, incident);
            return new ResponseEntity<>(updatedIncident, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Delete Incident
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            incidentService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}