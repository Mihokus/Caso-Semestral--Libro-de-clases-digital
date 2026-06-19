package cl.colegio.ohiggins.asistencia.service.command;

import cl.colegio.ohiggins.asistencia.model.Asistencia;
import cl.colegio.ohiggins.asistencia.repository.AsistenciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AsistenciaCommandService {

    @Autowired
    private AsistenciaRepository asistenciaRepo;

    /**
     * Guarda una asistencia en base de datos
     */
    public Asistencia save(Asistencia asistencia) {
        return asistenciaRepo.save(asistencia);
    }

    /**
     * Guarda múltiples asistencias (bulk)
     */
    public List<Asistencia> saveAll(List<Asistencia> asistencias) {
        return asistenciaRepo.saveAll(asistencias);
    }
}