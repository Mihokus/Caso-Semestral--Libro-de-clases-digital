package cl.duoc.academic.model;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Evaluacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long alumnoId;
    private String nombre;
    private Double nota;
    private Double ponderacion;
    @ManyToOne
    @JoinColumn(name = "asignatura_id")
    private Asignatura asignatura;

    
}
