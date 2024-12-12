package gestion.proyectos.gestionproyectos.Controller;


import gestion.proyectos.gestionproyectos.Entity.Management;
import gestion.proyectos.gestionproyectos.Service.ManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/api/v1/management")
public class ManagementController {
    @Autowired
	ManagementService service;

    @PostMapping("/saveProcess")
	public ResponseEntity<Management> saveManagement(@RequestBody Management management) {
		Management newManagement = service.save(management);
		return ResponseEntity.ok(newManagement);
	}

    @GetMapping("/getAllProcess")
	public ResponseEntity<List<Management>> listManagements() {
    	List<Management> managements = service.getAll();
		return ResponseEntity.ok(managements);
	}

	@GetMapping("/GetProcess/{id}")
	public ResponseEntity<Management> getManagementById(@PathVariable Long id) {
		Management management = service.getById(id);
		return ResponseEntity.ok(management);
	}

	@PostMapping("/updateUser")
	public ResponseEntity<Management> updateManagement(@RequestBody Management management){
		Management updatedManagement = service.update(management);
		return ResponseEntity.ok(updatedManagement);
	}

	@DeleteMapping("/DeleteProcess/{id}")
	public ResponseEntity<Boolean> deleteManagementById(@PathVariable Long id) throws Exception {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
}
