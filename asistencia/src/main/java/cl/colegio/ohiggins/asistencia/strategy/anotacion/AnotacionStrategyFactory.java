package cl.colegio.ohiggins.asistencia.strategy.anotacion;

import cl.colegio.ohiggins.asistencia.model.TipoAnotacion;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Component
public class AnotacionStrategyFactory {

    private final Map<TipoAnotacion, AnotacionStrategy> strategies =
            new EnumMap<>(TipoAnotacion.class);

    public AnotacionStrategyFactory(List<AnotacionStrategy> list) {

        for (AnotacionStrategy s : list) {

            if (s instanceof PositivaStrategy) {
                strategies.put(TipoAnotacion.POSITIVA, s);
            }

            if (s instanceof NegativaStrategy) {
                strategies.put(TipoAnotacion.NEGATIVA, s);
            }

            if (s instanceof NeutralStrategy) {
                strategies.put(TipoAnotacion.NEUTRAL, s);
            }
        }
    }

    public AnotacionStrategy get(TipoAnotacion tipo) {
        return strategies.get(tipo);
    }
}