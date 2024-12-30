package gestion.proyectos.gestionproyectos.Repository;

import gestion.proyectos.gestionproyectos.Entity.Management;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ManagementRepository extends JpaRepository<Management, Long> {

    @Query(value = "SELECT * FROM management m WHERE m.name_management = :nameManagement AND m.id_proyect = :idProyect", nativeQuery = true)
    Management findByNameManagementAndIdProyect(@Param("nameManagement") String nameManagement, @Param("idProyect") Long idProyect);
    
}