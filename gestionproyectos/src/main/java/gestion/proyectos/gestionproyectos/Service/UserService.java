package gestion.proyectos.gestionproyectos.Service;

import gestion.proyectos.gestionproyectos.Entity.User;
import gestion.proyectos.gestionproyectos.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    //Creamos el CRUD
    //Create
    public User saveUser(User user){
        return userRepository.save(user);
    }

    //Read
    public List<User> getUsers(){
        return userRepository.findAll();
    }

    public User getUserById(Long id){
        return userRepository.findById(id).orElse(null);
    }

    //Update
    public User updateUser(User user){
        User existingUser = userRepository.findById(user.getId_usuario()).orElse(null);
        existingUser.setNames(user.getNames());
        existingUser.setSecond_names(user.getSecond_names());
        existingUser.setEmail(user.getEmail());
        existingUser.setPassword(user.getPassword());
        existingUser.setPhone_number(user.getPhone_number());
        return userRepository.save(existingUser);
    }

    //Delete
    public String deleteUser(Long id){
        userRepository.deleteById(id);
        return "Usuario eliminado, id:  "+id;
    }


}
