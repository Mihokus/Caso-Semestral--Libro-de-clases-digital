package cl.duoc.academic.dto;
import lombok.Data;

@Data
public class EvaluacionDTO {
    private Long asignaturaId;
    private String nombre;
    private Long alumnoId;
    private Double nota;
    private Double ponderacion;
}