package gestion.proyectos.gestionproyectos.Service;

import gestion.proyectos.gestionproyectos.Entity.User;
import gestion.proyectos.gestionproyectos.Repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Create
    public User create(User user) {
        return userRepository.save(user);
    }

    // Read
    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User getById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    // Update
    public User update(Long id, User userDetails) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User existingUser = optionalUser.get();
            if (userDetails.getNames() != null) {
                existingUser.setNames(userDetails.getNames());
            }
            if (userDetails.getSecondNames() != null) {
                existingUser.setSecondNames(userDetails.getSecondNames());
            }
            if (userDetails.getEmail() != null) {
                existingUser.setEmail(userDetails.getEmail());
            }
            if (userDetails.getPassword() != null) {
                existingUser.setPassword(userDetails.getPassword());
            }
            if (userDetails.getPhoneNumber() != null) {
                existingUser.setPhoneNumber(userDetails.getPhoneNumber());
            }
            if (userDetails.getProyects() != null) {
                existingUser.setProyects(userDetails.getProyects());
            }
            return userRepository.save(existingUser);
        } else {
            throw new RuntimeException("User not found with id " + id);
        }
    }

    // Delete
    public void delete(Long id) {
        userRepository.deleteById(id);
    }
}
