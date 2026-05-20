package cl.duoc.academic.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EvaluacionResponse {
    private Long id;
    private Long alumnoId;
    private String alumnoNombre;
    private Long asignaturaId;
    private String asignaturaNombre;
    private String nombre;
    private Double nota;
    private Double ponderacion;
    private LocalDate fecha;
    private RegistradoPor registradoPor;
}
