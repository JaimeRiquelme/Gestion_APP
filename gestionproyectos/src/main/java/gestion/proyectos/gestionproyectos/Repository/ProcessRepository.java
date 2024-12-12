package gestion.proyectos.gestionproyectos.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProcessRepository extends JpaRepository<Process, Long>{
    // realice the crud

    //Read

    List<Process> findAll();

    Process getProcessById(Long id);

    //Update

   Process updateProcess(Process process);

    //Delete

    void deleteProcess(Long id);

}
