package cl.colegio.ohiggins.asistencia.service.impl;

import cl.colegio.ohiggins.asistencia.model.Anotacion;
import cl.colegio.ohiggins.asistencia.repository.AnotacionRepository;
import cl.colegio.ohiggins.asistencia.service.AnotacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AnotacionServiceImpl implements AnotacionService {

    @Autowired
    private AnotacionRepository repository;

    @Override
    public Anotacion guardarAnotacion(Anotacion anotacion) {
        return repository.save(anotacion);
    }

    @Override
    public List<Anotacion> obtenerPorEstudiante(String rut) {
        return repository.findByRutEstudiante(rut);
    }
}