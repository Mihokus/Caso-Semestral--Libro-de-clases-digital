package cl.duoc.academic.model;
import java.time.LocalDate;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Evaluacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long alumnoId;
    private String alumnoNombre;
    private String nombre;
    private Double nota;
    private Double ponderacion;
    private String asignaturaNombre;
    private LocalDate fecha;
    private Long registradoPorId;
    private String registradoPorNombre;

    @ManyToOne
    @JoinColumn(name = "asignatura_id")
    private Asignatura asignatura;

    @PrePersist
        protected void onCreate(){
            this.fecha = LocalDate.now();
        }
}
