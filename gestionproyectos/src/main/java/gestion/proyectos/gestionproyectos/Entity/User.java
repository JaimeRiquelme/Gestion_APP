package gestion.proyectos.gestionproyectos.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long id_usuario;

    private String names;

    private String second_names;

    @Column(unique = true)
    private String email;

    private String password;

    private String phone_number;

    @OneToMany(mappedBy = "id_usuario")
    private List<Proyect> proyects;
}
