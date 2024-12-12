package gestion.proyectos.gestionproyectos.Service;


import gestion.proyectos.gestionproyectos.Repository.ProcessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProcessService {

    @Autowired
    private ProcessRepository processRepository;

    public void SaveProcess(Process process) {
        processRepository.save(process);
    }
    public Process GetProcess(long id) {
       return processRepository.getProcessById(id);
    }
    public List<Process> GetProcesses() {
        return processRepository.findAll();
    }

    public void DeleteProcess(long id) {
        processRepository.deleteProcess(id);
    }

    public void UpdateProcess(Process process) {
        processRepository.updateProcess(process);
    }
}
