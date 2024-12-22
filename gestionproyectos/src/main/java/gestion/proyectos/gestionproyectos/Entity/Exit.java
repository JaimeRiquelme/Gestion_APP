package gestion.proyectos.gestionproyectos.Entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "exit")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "idExit")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Exit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_exit")
    private Long idExit;

    @ManyToOne
    @JoinColumn(name = "id_process")
    @JsonIgnore // Ignorar la serialización directa del proceso
    private Process process;

    @JsonProperty("idProcess") // Exponer idProcess para serialización
    public Long getIdProcess() {
        return process != null ? process.getIdProcess() : null; // Obtener el ID del proceso relacionado
    }

    @JsonProperty("idProcess") // Permitir seteo durante la deserialización
    public void setIdProcess(Long idProcess) {
        if (idProcess != null) {
            this.process = new Process();
            this.process.setIdProcess(idProcess); // Solo establece el ID del proceso
        }
    }

    @Column(name = "name_exit")
    private String nameExit;

    private byte[] document;

    private String state;

    @Column(name = "date_creation")
    private String dateCreation;

    @Column(name = "date_validation")
    private String dateValidation;

    private String priority;

    private String responsible;

    private String description;

    @JsonIgnore
    @OneToMany(mappedBy = "exit", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Parameter> parameters = new ArrayList<>();

    // Métodos auxiliares para gestionar la relación bidireccional con los parámetros
    public void addParameter(Parameter parameter) {
        this.parameters.add(parameter);
        parameter.setExit(this); // Sincronizar la relación
    }

    public void removeParameter(Parameter parameter) {
        this.parameters.remove(parameter);
        parameter.setExit(null); // Romper la relación
    }
}