package gestion.proyectos.gestionproyectos.Repository;

import gestion.proyectos.gestionproyectos.Entity.Management;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ManagementRepository extends JpaRepository<Management, Long> {
    
}