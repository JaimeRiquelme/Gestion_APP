package gestion.proyectos.gestionproyectos.Entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_proyect")
    @JsonIgnore
    private Proyect proyect;

    @JsonProperty("idProyecto") // Exponer idProyect para serialización
    public Long getIdProyect() {
        return proyect != null ? proyect.getIdProyecto() : null; // Obtener el id del Proyect relacionado
    }

    @JsonProperty("idProyecto") // Permitir seteo durante la deserialización
    public void setIdProyect(Long idProyect) {
        if (idProyect != null) {
            this.proyect = new Proyect();
            this.proyect.setIdProyecto(idProyect); // Asociar solo el id del proyecto
        }
    }

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "registration_date")
    private String registrationDate;

    private String state;

    private String priority;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    @JsonIgnore
    private User responsible;

    @JsonProperty("idUsuario") // Exponer idUsuario para JSON
    public Long getIdUsuario() {
        return responsible != null ? responsible.getIdUsuario() : null; // Obtener el id del usuario responsable
    }

    @JsonProperty("idUsuario") // Permitir seteo con id durante la deserialización
    public void setIdUsuario(Long idUsuario) {
        if (idUsuario != null) {
            this.responsible = new User();
            this.responsible.setIdUsuario(idUsuario); // Asociar solo el id del usuario responsable
        }
    }
}