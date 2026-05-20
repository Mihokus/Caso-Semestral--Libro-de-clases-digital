package cl.duoc.academic.model;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data

public class Asignatura {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombreAsignatura;
    private String docenteNombre;

    @ManyToOne
    @JoinColumn(name="curso_id")
    private Curso curso;
}
