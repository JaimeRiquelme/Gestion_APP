package gestion.proyectos.gestionproyectos.Entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "exit")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "idExit")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Exit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_exit")
    private Long idExit;

    @ManyToOne
    @JoinColumn(name = "id_process")
    private Process process;

    @Column(name = "name_exit")
    private String nameExit;

    private byte[] document;
    private String state;

    @Column(name = "date_creation")
    private String dateCreation;

    @Column(name = "fate_validation")
    private String dateValidation;

    private String priority;
    private String responsible;
    private String description;

    @JsonManagedReference
    @OneToMany(mappedBy = "exit", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Parameter> parameters;
}
