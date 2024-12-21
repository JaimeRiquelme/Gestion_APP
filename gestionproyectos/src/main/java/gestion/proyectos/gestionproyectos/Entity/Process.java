package gestion.proyectos.gestionproyectos.Entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "process")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "idProcess"
)
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
    @JsonIgnore // Ignorar la serialización directa de Management
    private Management management;

    @JsonProperty("idManagement") // Exponer idManagement para serialización
    public Long getIdManagement() {
        return management != null ? management.getIdManagement() : null; // Obtener el id de Management relacionado
    }

    @JsonProperty("idManagement") // Permitir seteo durante la deserialización
    public void setIdManagement(Long idManagement) {
        if (idManagement != null) {
            this.management = new Management();
            this.management.setIdManagement(idManagement); // Asociar solo el id de Management
        }
    }

    @Column(name = "name_process")
    private String nameProcess;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "state_process")
    private String stateProcess;

    @Column(name = "start_date_planned")
    private String startDatePlanned;

    @Column(name = "end_date_planned")
    private String endDatePlanned;

    @Column(name = "start_date_real")
    private String startDateReal;

    @Column(name = "end_date_real")
    private String endDateReal;

    @JsonIgnore
    @OneToMany(mappedBy = "process", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Exit> exits;
}