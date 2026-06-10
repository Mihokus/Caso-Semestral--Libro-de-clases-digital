package cl.colegio.ohiggins.asistencia.dto;

import cl.colegio.ohiggins.asistencia.model.TipoAnotacion;
import lombok.Data;

@Data
public class AnotacionRequest {

    private Long alumnoId;
    private TipoAnotacion tipo;
    private String descripcion;

    private Long registradoPorId;
    private String registradoPorNombre;
}