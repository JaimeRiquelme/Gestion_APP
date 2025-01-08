package gestion.proyectos.gestionproyectos.Repository;

import gestion.proyectos.gestionproyectos.Entity.Exit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ExitRepository extends JpaRepository<Exit, Long> {

    @Query(value = "SELECT * FROM exit e WHERE e.id_process = :idProcess AND e.name_exit = :nameExit", nativeQuery = true)
    Exit findByIdProcessAndNameExit(Long idProcess, String nameExit);

    @Query(value = "SELECT e.* FROM exit e " +
            "JOIN process p ON e.id_process = p.id_process " +
            "JOIN management m ON p.id_management = m.id_management " +
            "JOIN proyects pr ON m.id_proyect = pr.id_proyecto " +
            "WHERE pr.id_proyecto = :idProyecto AND e.name_exit = 'Acta de Constitución del Proyecto'", nativeQuery = true)
    Exit findActInstitutionByIdProyect(Long idProyecto);
    Exit findByNameExitAndState(String nameExit, String state);


}
