package cl.duoc.academic.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RendimientoDTO {
    private Long asignaturaId;
    private String asignaturaNombre;
    private Double promedioCurso;
    private Integer cantidadEvaluaciones;
    private List<RendimientoAlumno> alumnos;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RendimientoAlumno {
        private Long alumnoId;
        private String alumnoNombre;
        private Double promedio;
        private Integer cantidadEvaluaciones;
    }
}
