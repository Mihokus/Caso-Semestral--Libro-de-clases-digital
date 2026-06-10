package cl.colegio.ohiggins.asistencia.dto;

import cl.colegio.ohiggins.asistencia.model.EstadoAsistencia;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class AsistenciaBulkRequest {

    private Long cursoId;
    private LocalDate fecha;

    private Long registradoPorId;
    private String registradoPorNombre;

    private List<Entrada> asistencias;

    @Data
    public static class Entrada {
        private Long alumnoId;
        private EstadoAsistencia estado;
    }
}