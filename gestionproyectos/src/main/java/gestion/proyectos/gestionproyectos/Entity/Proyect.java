package gestion.proyectos.gestionproyectos.Entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "proyects")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Proyect {

    /*atributos a generar:
    * id_proyecto PK
    * id_usuario FK con User
    * nombre_proyecto
    * descripcion
    * organizacion
    * fecha_inicio
    * fecha_estimada_fin
    * fecha_estimada_real*/

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_proyecto")
    private Long id_proyecto;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private User id_usuario;

    private String name_proyect;

    private String description;

    private String organization;

    private String start_date;

    private String estimated_end_date;

    private String real_estimated_end_date;

    @OneToMany(mappedBy = "id_proyect")
    private List<Management> managements;



}
