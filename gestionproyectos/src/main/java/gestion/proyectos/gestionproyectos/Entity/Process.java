package gestion.proyectos.gestionproyectos.Entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "process")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "idProcess")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Process {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_process")
    private Long idProcess;

    @ManyToOne
    @JoinColumn(name = "id_management")
    private Management management;

    @Column(name = "name_process")
    private String nameProcess;  // Cambiado de name_process a nameProcess

    private String description;

    @Column(name = "state_process")
    private String stateProcess;  // Cambiado de state_process a stateProcess

    @Column(name = "start_date_planned")
    private String startDatePlanned;  // Cambiado de start_date_planned a startDatePlanned

    @Column(name = "end_date_planned")
    private String endDatePlanned;  // Cambiado de end_date_planned a endDatePlanned

    @Column(name = "start_date_real")
    private String startDateReal;  // Cambiado de start_date_real a startDateReal

    @Column(name = "end_date_real")
    private String endDateReal;  // Cambiado de end_date_real a endDateReal

    @OneToMany(mappedBy = "process", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Exit> exits;
}
