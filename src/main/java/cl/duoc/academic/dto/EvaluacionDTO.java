package cl.duoc.academic.dto;
import lombok.Data;

@Data
public class EvaluacionDTO {
    private Long asignaturaId;
    private String nombre;
    private Double nota;
}