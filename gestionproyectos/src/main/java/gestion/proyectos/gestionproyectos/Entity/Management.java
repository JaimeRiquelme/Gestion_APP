package gestion.proyectos.gestionproyectos.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "management")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Management {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_management")
    private Long id_management;

    @ManyToOne
    @JoinColumn(name = "id_proyect")
    private Proyect id_proyect;

    private String name_management;

    private String description;

    @OneToMany(mappedBy = "id_management",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Process> processes;

}
