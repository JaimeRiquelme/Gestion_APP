package gestion.proyectos.gestionproyectos.Service;

import gestion.proyectos.gestionproyectos.Entity.Parameter;
import gestion.proyectos.gestionproyectos.Repository.ParameterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ParameterService {

    @Autowired
    private ParameterRepository parameterRepository;

    // Create
    public Parameter save(Parameter parameter) {
        return parameterRepository.save(parameter);
    }

    // Read
    public List<Parameter> getAll() {
        return (List<Parameter>) parameterRepository.findAll();
    }

    public Parameter getById(Long id) {
        return parameterRepository.findById(id).orElse(null);
    }

    // Update
    public Parameter update(Parameter parameter) {
        return parameterRepository.save(parameter);
    }

    // Delete
    public boolean delete(Long id) throws Exception {
        Optional<Parameter> parameter = parameterRepository.findById(id);
        if (parameter.isPresent()) {
            parameterRepository.deleteById(id);
            return true;
        }
        throw new Exception("Parameter with id " + id + " not found");
    }
}