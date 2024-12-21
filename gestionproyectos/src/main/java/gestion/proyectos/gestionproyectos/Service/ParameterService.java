package gestion.proyectos.gestionproyectos.Service;

import gestion.proyectos.gestionproyectos.Entity.Parameter;
import gestion.proyectos.gestionproyectos.Repository.ParameterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ParameterService {

    @Autowired
    private ParameterRepository parameterRepository;

    @Autowired
    private ExitService exitService;

    // Create
    public Parameter save(Parameter parameter) {
        return parameterRepository.save(parameter);
    }

    // Read
    public List<Parameter> getAll() {
        return parameterRepository.findAllParameters();
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

    //Metodo para guardar los parametros dado un map string, string

    public void saveParameters(Map<String, String> data, Long idExit) {

        System.out.println("Guardando parametros");
        for (Map.Entry<String, String> entry : data.entrySet()) {

            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
            Parameter Buscar = parameterRepository.findByNameParameter(entry.getKey());

            if (Buscar == null) {
                Parameter parameter = new Parameter();
                parameter.setNameParameter(entry.getKey());
                parameter.setContent(entry.getValue());
                parameter.setExit(exitService.getById(idExit));
                parameter.setState("COMPLETADO");
                parameterRepository.save(parameter);
            } else {
                Buscar.setContent(entry.getValue());
                parameterRepository.save(Buscar);
            }
        }
    }
}