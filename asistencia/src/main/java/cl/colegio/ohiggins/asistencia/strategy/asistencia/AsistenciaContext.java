package cl.colegio.ohiggins.asistencia.strategy.asistencia;

import cl.colegio.ohiggins.asistencia.model.EstadoAsistencia;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AsistenciaContext {

    private Long alumnoId;
    private Long cursoId;
    private LocalDate fecha;

    private EstadoAsistencia estado;

    private Long registradoPorId;
    private String registradoPorNombre;
}