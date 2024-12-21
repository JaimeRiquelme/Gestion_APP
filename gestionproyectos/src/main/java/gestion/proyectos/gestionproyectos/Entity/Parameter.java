package gestion.proyectos.gestionproyectos.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "parameter")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Parameter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_parameter")
    private Long idParameter;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "exit_id")
    private Exit exit;

    @Column(name = "name_parameter", unique = true)
    private String nameParameter;  

    private String content;
    private String state;
}
