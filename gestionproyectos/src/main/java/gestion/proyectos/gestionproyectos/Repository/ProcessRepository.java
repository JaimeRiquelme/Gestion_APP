package gestion.proyectos.gestionproyectos.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import gestion.proyectos.gestionproyectos.Entity.Process;

@Repository
public interface ProcessRepository extends JpaRepository<Process, Long> {

    //SELECT *
    //FROM process p
    //WHERE p.id_management = 3
    //AND p.name_process = 'Acta de Constitución del Proyecto';

    @Query(value = "SELECT * FROM process p WHERE p.id_management = :idManagement AND p.name_process = :nameProcess", nativeQuery = true)
    Process findByIdManagementAndNameProcess(@Param("idManagement") Long idManagement, @Param("nameProcess") String nameProcess);

}