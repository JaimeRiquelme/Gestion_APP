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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_usuario")
    @JsonIgnore
    private User user;

    @JsonProperty("idUsuario")
    public Long getIdUsuario() {
        return user != null ? user.getIdUsuario() : null;
    }

    @JsonProperty("idUsuario")
    public void setIdUsuario(Long idUsuario) {
        if (idUsuario != null) {
            this.user = new User();
            this.user.setIdUsuario(idUsuario); // Solo asignar el id del usuario
        }
    }

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