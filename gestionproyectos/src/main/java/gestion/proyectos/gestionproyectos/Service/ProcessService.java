package gestion.proyectos.gestionproyectos.Service;

import gestion.proyectos.gestionproyectos.Entity.Process;
import gestion.proyectos.gestionproyectos.Repository.ProcessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProcessService {

    @Autowired
    private ProcessRepository processRepository;

    public Process saveProcess(Process process) {
        return processRepository.save(process);
    }

    public Process getProcess(long id) {
        return processRepository.findById(id).orElse(null);
    }

    public List<Process> getProcesses() {
        return processRepository.findAll();
    }

    public void deleteProcess(long id) {
        processRepository.deleteById(id);
    }

    public Process updateProcess(Process process) {
        return processRepository.save(process);
    }
}