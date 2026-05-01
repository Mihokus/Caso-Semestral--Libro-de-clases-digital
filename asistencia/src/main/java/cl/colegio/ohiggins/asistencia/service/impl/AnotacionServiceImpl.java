package cl.colegio.ohiggins.asistencia.service;

import cl.colegio.ohiggins.asistencia.model.Anotacion;
import cl.colegio.ohiggins.asistencia.repository.AnotacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnotacionServiceImpl implements AnotacionService {
    @Autowired
    private AnotacionRepository repository;

    @Override
    public Anotacion guardarAnotacion(Anotacion anotacion) {
        return repository.save(anotacion);
    }
}