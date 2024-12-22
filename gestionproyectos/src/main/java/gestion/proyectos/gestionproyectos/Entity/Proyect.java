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
@Table(name = "proyects")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "idProyecto"
)
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
    @JsonIgnore // Ignorar la serialización directa de User
    private User user;

    @JsonProperty("idUsuario") // Exponer idUsuario para serialización
    public Long getIdUsuario() {
        return user != null ? user.getIdUsuario() : null; // Obtener el id del usuario relacionado
    }

    @JsonProperty("idUsuario") // Permitir seteo durante la deserialización
    public void setIdUsuario(Long idUsuario) {
        if (idUsuario != null) {
            this.user = new User();
            this.user.setIdUsuario(idUsuario); // Asociar solo el id del usuario
        }
    }

    @Column(name = "name_proyect")
    private String nameProyect;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "organization")
    private String organization;

    @Column(name = "start_date")
    private String startDate;

    @Column(name = "estimated_end_date")
    private String estimatedEndDate;

    @Column(name = "real_estimated_end_date")
    private String realEstimatedEndDate;

    @JsonIgnore
    @OneToMany(mappedBy = "proyect", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Management> managements;

    @JsonIgnore
    @OneToMany(mappedBy = "proyect", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Incident> incidents;

    @JsonIgnore
    @OneToMany(mappedBy = "proyect", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Lessons> lessons;
}