package cl.colegio.ohiggins.asistencia.strategy.anotacion;

import cl.colegio.ohiggins.asistencia.dto.AnotacionRequest;
import lombok.Data;

@Data
public class AnotacionContext {

    private Long alumnoId;
    private AnotacionRequest request;
}