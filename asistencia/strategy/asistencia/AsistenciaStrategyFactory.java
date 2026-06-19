package cl.colegio.ohiggins.asistencia.strategy.asistencia;

import cl.colegio.ohiggins.asistencia.model.EstadoAsistencia;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Component
public class AsistenciaStrategyFactory {

    private final Map<EstadoAsistencia, AsistenciaStrategy> strategies =
            new EnumMap<>(EstadoAsistencia.class);

    public AsistenciaStrategyFactory(List<AsistenciaStrategy> list) {

        for (AsistenciaStrategy s : list) {

            if (s instanceof PresenteStrategy) {
                strategies.put(EstadoAsistencia.PRESENTE, s);
            }

            if (s instanceof AusenteStrategy) {
                strategies.put(EstadoAsistencia.AUSENTE, s);
            }

            if (s instanceof JustificadoStrategy) {
                strategies.put(EstadoAsistencia.JUSTIFICADO, s);
            }
        }
    }

    public AsistenciaStrategy get(EstadoAsistencia estado) {
        return strategies.get(estado);
    }
}