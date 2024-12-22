package gestion.proyectos.gestionproyectos.Controller;

import gestion.proyectos.gestionproyectos.Entity.Management;
import gestion.proyectos.gestionproyectos.Service.ManagementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/management")
public class ManagementController {

	private final ManagementService managementService;

	public ManagementController(ManagementService managementService) {
		this.managementService = managementService;
	}

	// Crear Management
	@PostMapping("/create")
	public ResponseEntity<Management> create(@RequestBody Management management) {
		return new ResponseEntity<>(managementService.create(management), HttpStatus.CREATED);
	}

	// Obtener todos los Managements
	@GetMapping("/getAll")
	public ResponseEntity<List<Management>> getAll() {
		return new ResponseEntity<>(managementService.getAll(), HttpStatus.OK);
	}

	// Obtener Management por ID
	@GetMapping("/getById/{id}")
	public ResponseEntity<Management> getById(@PathVariable Long id) {
		try {
			Management management = managementService.getById(id);
			return new ResponseEntity<>(management, HttpStatus.OK);
		} catch (RuntimeException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	// Actualizar Management
	@PutMapping("/update/{id}")
	public ResponseEntity<Management> update(@PathVariable Long id, @RequestBody Management management) {
		try {
			Management updatedManagement = managementService.update(id, management);
			return new ResponseEntity<>(updatedManagement, HttpStatus.OK);
		} catch (RuntimeException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	// Eliminar Management
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		try {
			managementService.delete(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (RuntimeException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
}