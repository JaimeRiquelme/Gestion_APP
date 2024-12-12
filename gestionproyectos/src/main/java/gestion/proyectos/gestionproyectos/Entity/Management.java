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
    private Long idManagement;

    @ManyToOne
    @JoinColumn(name = "id_proyect")
    private Proyect proyect;  // Cambiado de id_proyect a proyect

    @Column(name = "name_management")
    private String nameManagement;

    private String description;

    @OneToMany(mappedBy = "management", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Process> processes;
}