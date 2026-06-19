package cl.colegio.ohiggins.asistencia.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "anotaciones")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Anotacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long alumnoId;
    private String alumnoNombre;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoAnotacion tipo;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(nullable = false)
    private Instant fecha;

    private Long registradoPorId;
    private String registradoPorNombre;
}