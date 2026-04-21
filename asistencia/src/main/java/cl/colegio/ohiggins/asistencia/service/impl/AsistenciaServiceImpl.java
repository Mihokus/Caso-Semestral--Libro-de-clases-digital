package cl.colegio.ohiggins.asistencia.service.impl;

import cl.colegio.ohiggins.asistencia.model.Asistencia;
import cl.colegio.ohiggins.asistencia.repository.AsistenciaRepository;
import cl.colegio.ohiggins.asistencia.service.AsistenciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AsistenciaServiceImpl implements AsistenciaService {

    @Autowired
    private AsistenciaRepository repository;

    @Override
    public Asistencia registrar(Asistencia asistencia) {
        return repository.save(asistencia);
    }

    @Override
    public List<Asistencia> obtenerTodo() {
        return repository.findAll();
    }
}