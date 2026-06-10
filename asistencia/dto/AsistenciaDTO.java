package cl.colegio.ohiggins.asistencia.dto;

import cl.colegio.ohiggins.asistencia.model.EstadoAsistencia;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AsistenciaDTO {

    private Long id;
    private Long alumnoId;
    private String alumnoNombre;
    private Long cursoId;
    private String cursoNombre;
    private LocalDate fecha;
    private EstadoAsistencia estado;

    private RegistradoPor registradoPor;
}