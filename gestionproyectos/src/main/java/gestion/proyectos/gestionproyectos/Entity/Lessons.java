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
@Table(name = "lessons")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "idLesson"
)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Lessons {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_lesson")
    private Long idLesson;

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

    @Column(name = "recommendations", columnDefinition = "TEXT")
    private String recommendations;

    @Column(name = "registration_date")
    private String registrationDate;

    private String category;

    private String impact;
}