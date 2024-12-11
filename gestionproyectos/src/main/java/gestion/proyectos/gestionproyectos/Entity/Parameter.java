package gestion.proyectos.gestionproyectos.Entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "parameter")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Parameter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_parameter")
    private Long id_parameter;

    @ManyToOne
    @JoinColumn(name = "id_exit")
    private Exit id_exit;

    @Column(unique = true)
    private String name_parameter;

    private String content;

    private String state;

}
