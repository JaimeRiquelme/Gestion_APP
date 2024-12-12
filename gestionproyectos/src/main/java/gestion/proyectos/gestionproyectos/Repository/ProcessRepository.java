package gestion.proyectos.gestionproyectos.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import gestion.proyectos.gestionproyectos.Entity.Process;

@Repository
public interface ProcessRepository extends JpaRepository<Process, Long> {

}