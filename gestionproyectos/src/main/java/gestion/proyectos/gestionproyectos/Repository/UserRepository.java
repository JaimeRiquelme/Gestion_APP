package gestion.proyectos.gestionproyectos.Repository;

import gestion.proyectos.gestionproyectos.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    //Creamos el CRUD

    //Create
    User saveUser(User user);

    //Read

    List<User> getUsers();

    User getUserById(Long id);

    //Update

    User updateUser(User user);

    //Delete

    void deleteUser(Long id);


}
