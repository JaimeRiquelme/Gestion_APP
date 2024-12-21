package gestion.proyectos.gestionproyectos.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "parameter")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Parameter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_parameter")
    private Long idParameter;

    @JsonBackReference // Evita la serialización de la relación para evitar ciclos infinitos
    @ManyToOne
    @JoinColumn(name = "exit_id")
    private Exit exit;

    @JsonProperty("idExit") // Exponer el ID del exit relacionado para permitir su uso en la serialización
    public Long getIdExit() {
        return exit != null ? exit.getIdExit() : null;
    }

    @JsonProperty("idExit") // Permitir seteo durante la deserialización
    public void setIdExit(Long idExit) {
        if (idExit != null) {
            this.exit = new Exit(); // Crear una instancia parcial de Exit asignando solo el ID
            this.exit.setIdExit(idExit);
        }
    }

    @Column(name = "name_parameter", unique = true)
    private String nameParameter;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    private String state;
}