package cl.colegio.ohiggins.asistencia.dto;

import cl.colegio.ohiggins.asistencia.model.TipoAnotacion;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnotacionDTO {
    private Long id;
    private Long alumnoId;
    private String alumnoNombre;
    private TipoAnotacion tipo;
    private String descripcion;
    private Instant fecha;
    private RegistradoPor registradoPor;
}
