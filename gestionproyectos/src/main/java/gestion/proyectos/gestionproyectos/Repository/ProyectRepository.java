package gestion.proyectos.gestionproyectos.Repository;

import gestion.proyectos.gestionproyectos.Entity.Proyect;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProyectRepository extends JpaRepository<Proyect, Long> {

    List<Proyect> findByUser_IdUsuario(Long idUsuario);
}
