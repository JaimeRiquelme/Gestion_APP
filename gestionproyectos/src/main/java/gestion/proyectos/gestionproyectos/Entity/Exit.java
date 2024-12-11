package gestion.proyectos.gestionproyectos.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "exit")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Exit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_exit")
    private Long id_exit;

    @ManyToOne
    @JoinColumn(name = "id_process")
    private Process id_process;

    private String name_exit;

    private byte[] document;

    private String state;

    private String date_creation;

    private String fate_validation;

    private String priority;

    private String responsible;

    private String description;

    @OneToMany(mappedBy = "id_exit",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Parameter> parameters;

}
