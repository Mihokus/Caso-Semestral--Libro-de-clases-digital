package cl.colegio.ohiggins.asistencia.service.command;

import cl.colegio.ohiggins.asistencia.model.Anotacion;
import cl.colegio.ohiggins.asistencia.repository.AnotacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnotacionCommandService {

    @Autowired
    private AnotacionRepository anotacionRepo;

    /**
     * Guarda una anotación en base de datos
     */
    public Anotacion save(Anotacion anotacion) {
        return anotacionRepo.save(anotacion);
    }
}