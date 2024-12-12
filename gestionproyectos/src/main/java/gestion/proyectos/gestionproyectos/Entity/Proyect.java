package gestion.proyectos.gestionproyectos.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "proyects")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Proyect {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_proyecto")
    private Long idProyecto;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private User user;

    @Column(name = "name_proyect")
    private String nameProyect;

    private String description;

    private String organization;

    @Column(name = "start_date")
    private String startDate;

    @Column(name = "estimated_end_date")
    private String estimatedEndDate;

    @Column(name = "real_estimated_end_date")
    private String realEstimatedEndDate;

    @OneToMany(mappedBy = "proyect", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Management> managements;
}