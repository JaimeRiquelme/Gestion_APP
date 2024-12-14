package gestion.proyectos.gestionproyectos.Entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "incident")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "idIncident"
)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Incident {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_incident")
    private Long idIncident;

    @ManyToOne
    @JoinColumn(name = "id_proyect")
    private Proyect proyect;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "registration_date")
    private String registrationDate;

    private String state;

    private String priority;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private User responsible;
}