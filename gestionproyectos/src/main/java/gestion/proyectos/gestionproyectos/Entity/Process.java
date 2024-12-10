package gestion.proyectos.gestionproyectos.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "process")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Process {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_process")
    private Long id_process;

    @ManyToOne
    @JoinColumn(name = "id_management")
    private Management id_management;

    private String name_process;

    private String description;

    private String state_process;

    private String start_date_planned;

    private String end_date_planned;

    private String start_date_real;

    private String end_date_real;

    @OneToMany(mappedBy = "id_process")
    private List<Exit> exits;

}
