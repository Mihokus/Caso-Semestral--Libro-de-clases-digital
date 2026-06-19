package cl.colegio.ohiggins.asistencia.controller;

import cl.colegio.ohiggins.asistencia.dto.AsistenciaBulkRequest;
import cl.colegio.ohiggins.asistencia.dto.AsistenciaDTO;
import cl.colegio.ohiggins.asistencia.model.EstadoAsistencia;
import cl.colegio.ohiggins.asistencia.strategy.asistencia.AsistenciaStrategy;
import cl.colegio.ohiggins.asistencia.strategy.asistencia.AsistenciaStrategyFactory;
import cl.colegio.ohiggins.asistencia.strategy.asistencia.AsistenciaContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/asistencia")
public class AsistenciaController {

    @Autowired
    private AsistenciaStrategyFactory factory;

    @PostMapping
    public ResponseEntity<List<AsistenciaDTO>> registrar(@RequestBody AsistenciaBulkRequest req) {

        List<AsistenciaDTO> result = new ArrayList<>();

        for (AsistenciaBulkRequest.Entrada e : req.getAsistencias()) {

            AsistenciaStrategy strategy = factory.get(e.getEstado());

            AsistenciaContext ctx = new AsistenciaContext();
            ctx.setAlumnoId(e.getAlumnoId());
            ctx.setEstado(e.getEstado());
            ctx.setFecha(req.getFecha());
            ctx.setCursoId(req.getCursoId());
            ctx.setRegistradoPorId(req.getRegistradoPorId());
            ctx.setRegistradoPorNombre(req.getRegistradoPorNombre());

            result.add(strategy.ejecutar(ctx));
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }
}