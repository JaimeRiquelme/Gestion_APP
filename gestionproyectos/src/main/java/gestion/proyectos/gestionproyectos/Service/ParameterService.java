package gestion.proyectos.gestionproyectos.Service;

import gestion.proyectos.gestionproyectos.Entity.Parameter;
import gestion.proyectos.gestionproyectos.Entity.Exit;
import gestion.proyectos.gestionproyectos.Repository.ParameterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ParameterService {

    private final ParameterRepository parameterRepository;
    private final ExitService exitService;

    @Autowired
    public ParameterService(ParameterRepository parameterRepository, ExitService exitService) {
        this.parameterRepository = parameterRepository;
        this.exitService = exitService;
    }

    // Crear un nuevo Parameter asociado a un Exit
    public Parameter create(Parameter parameter) {
        // Validar la relación con Exit
        if (parameter.getIdExit() != null) {
            Exit exit = exitService.getById(parameter.getIdExit());
            if (exit != null) {
                parameter.setExit(exit); // Asociar Exit al parámetro
            } else {
                throw new RuntimeException("Exit not found with id " + parameter.getIdExit());
            }
        }
        return parameterRepository.save(parameter);
    }

    // Obtener todos los Parameters
    public List<Parameter> getAll() {
        return parameterRepository.findAllParameters();
    }

    // Obtener un Parameter por su ID
    public Parameter getById(Long id) {
        return parameterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Parameter not found with id " + id));
    }

    // Actualizar un Parameter existente por su ID
    public Parameter update(Long id, Parameter parameterDetails) {
        // Buscar el parámetro existente
        Parameter existingParameter = parameterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Parameter not found with id " + id));

        // Actualizar los atributos del parámetro según los datos proporcionados
        if (parameterDetails.getNameParameter() != null) {
            existingParameter.setNameParameter(parameterDetails.getNameParameter());
        }
        if (parameterDetails.getContent() != null) {
            existingParameter.setContent(parameterDetails.getContent());
        }
        if (parameterDetails.getState() != null) {
            existingParameter.setState(parameterDetails.getState());
        }

        // Validar y asociar un nuevo Exit si se proporciona un idExit
        if (parameterDetails.getIdExit() != null) {
            Exit exit = exitService.getById(parameterDetails.getIdExit());
            if (exit != null) {
                existingParameter.setExit(exit);
            } else {
                throw new RuntimeException("Exit not found with id " + parameterDetails.getIdExit());
            }
        }

        return parameterRepository.save(existingParameter);
    }

    // Eliminar un Parameter por su ID
    public void delete(Long id) {
        if (parameterRepository.existsById(id)) {
            parameterRepository.deleteById(id);
        } else {
            throw new RuntimeException("Parameter not found with id " + id);
        }
    }

    // Guardar una lista de Parameters a partir de un Map<String, String> asociado a un Exit
    public void saveParameters(Map<String, String> data, Long idExit) {
        // Obtener el Exit asociado al idExit
        Exit exit = exitService.getById(idExit);
        if (exit == null) {
            throw new RuntimeException("Exit not found with id " + idExit);
        }

        System.out.println("Guardando parámetros asociados a Exit con id: " + idExit);

        for (Map.Entry<String, String> entry : data.entrySet()) {
            System.out.println("Clave = " + entry.getKey() + ", Valor = " + entry.getValue());

            // Buscar el parámetro existente por su nombre
            Parameter existingParameter = parameterRepository.findByNameParameter(entry.getKey());

            if (existingParameter == null) {
                // Crear un nuevo parámetro si no existe
                Parameter parameter = new Parameter();
                parameter.setNameParameter(entry.getKey());
                parameter.setContent(entry.getValue());
                parameter.setExit(exit); // Asociar Exit al nuevo parámetro
                parameter.setState("CREADO");
                parameterRepository.save(parameter);
            } else {
                // Actualizar el contenido del parámetro existente
                existingParameter.setContent(entry.getValue());
                existingParameter.setExit(exit); // Reasociar el Exit si es necesario
                existingParameter.setState("ACTUALIZADO");
                parameterRepository.save(existingParameter);
            }
        }
    }
}