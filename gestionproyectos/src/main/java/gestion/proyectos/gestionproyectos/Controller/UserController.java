package gestion.proyectos.gestionproyectos.Controller;


import gestion.proyectos.gestionproyectos.Entity.User;
import gestion.proyectos.gestionproyectos.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private UserService userService;

    //Creamos el CRUD

    //Create
    @PostMapping("/saveUser")
    public User saveUser(User user){
        return userService.saveUser(user);
    }

    //Read
    @PostMapping("/getUsers")
    public List<User> getUsers(){
        return userService.getUsers();
    }

    @PostMapping("/getUserById")
    public User getUserById(Long id) {
        return userService.getUserById(id);
    }

    //Update
    @PostMapping("/updateUser")
    public User updateUser(User user){
        return userService.updateUser(user);
    }

    //Delete
    @PostMapping("/deleteUser")
    public String deleteUser(Long id){
        return userService.deleteUser(id);
    }


}
