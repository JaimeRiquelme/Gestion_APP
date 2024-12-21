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
@Table(name = "management")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "idManagement"
)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Management {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_management")
    private Long idManagement;

    @ManyToOne
    @JoinColumn(name = "id_proyecto")
    @JsonIgnore // Ignorar la serialización directa del proyecto
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

    @Column(name = "name_management")
    private String nameManagement;

    private String description;

    @OneToMany(mappedBy = "management", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Process> processes;
}