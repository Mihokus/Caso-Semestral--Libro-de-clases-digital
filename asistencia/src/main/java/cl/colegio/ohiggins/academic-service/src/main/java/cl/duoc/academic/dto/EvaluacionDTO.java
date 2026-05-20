package cl.duoc.academic.dto;
import java.time.LocalDate;

import lombok.Data;

@Data
public class EvaluacionDTO {
    private Long asignaturaId;
    private String asignaturaNombre;
    private String nombre;
    private Long alumnoId;
    private String alumnoNombre;
    private Double nota;
    private Double ponderacion;
    private LocalDate fecha;
    private Long registradoPorId;
    private String registradoPorNombre;
}