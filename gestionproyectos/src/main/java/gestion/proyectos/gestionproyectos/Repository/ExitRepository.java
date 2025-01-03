package gestion.proyectos.gestionproyectos.Repository;

import gestion.proyectos.gestionproyectos.Entity.Exit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ExitRepository extends JpaRepository<Exit, Long> {

    @Query(value = "SELECT * FROM exit e WHERE e.id_process = :idProcess AND e.name_exit = :nameExit", nativeQuery = true)
    Exit findByIdProcessAndNameExit(Long idProcess, String nameExit);
    
    
}
