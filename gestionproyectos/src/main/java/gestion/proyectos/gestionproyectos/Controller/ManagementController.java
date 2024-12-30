package gestion.proyectos.gestionproyectos.Controller;

import gestion.proyectos.gestionproyectos.Entity.Management;
import gestion.proyectos.gestionproyectos.Entity.Process;
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

	// Obtener lista de Process por ID de Management
	@GetMapping("/{id}/processes")
	public ResponseEntity<List<Process>> getProcessesByManagementId(@PathVariable Long id) {
		try {
			// Llamamos al servicio para obtener los procesos asociados al Management
			List<Process> processes = managementService.getProcessesByManagementId(id);
			return new ResponseEntity<>(processes, HttpStatus.OK); // Retornamos la lista con estado 200
		} catch (RuntimeException e) {
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND); // Retornamos 404 si no se encuentra el Management
		}
	}

	// Obtener Management por nombre y ID de Proyecto
	@GetMapping("/getByNameAndIdProyect")
	public ResponseEntity<Management> getByNameManagementAndIdProyect(@RequestParam String nameManagement, @RequestParam Long idProyect) {
		try {
			Management management = managementService.findByNameManagementAndIdProyect(nameManagement, idProyect);

			if(management != null) {
				return new ResponseEntity<>(management, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		} catch (RuntimeException e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}