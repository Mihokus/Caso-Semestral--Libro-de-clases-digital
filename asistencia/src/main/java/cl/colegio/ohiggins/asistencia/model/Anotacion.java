package cl.colegio.ohiggins.asistencia.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "anotaciones")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Anotacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rut_estudiante", nullable = false)
    private String rutEstudiante;

    @Column(nullable = false)
    private String tipo; // Ejemplo: "POSITIVA", "NEGATIVA"

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(nullable = false)
    private LocalDate fecha;
}