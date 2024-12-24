package gestion.proyectos.gestionproyectos.Repository;

import gestion.proyectos.gestionproyectos.Entity.Parameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParameterRepository extends JpaRepository<Parameter, Long> {
    @Query("SELECT p FROM Parameter p")
    List<Parameter> findAllParameters();

    @Query("SELECT p FROM Parameter p WHERE p.nameParameter = ?1 AND p.exit.idExit = ?2")
    Parameter findByNameParameteraAndIdExit(String nameParameter, Long idExit);
}
