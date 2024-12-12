package gestion.proyectos.gestionproyectos.Repository;

import gestion.proyectos.gestionproyectos.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}