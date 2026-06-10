package cl.colegio.ohiggins.asistencia.controller;

import cl.colegio.ohiggins.asistencia.dto.AnotacionDTO;
import cl.colegio.ohiggins.asistencia.dto.AnotacionRequest;
import cl.colegio.ohiggins.asistencia.service.query.AnotacionQueryService;
import cl.colegio.ohiggins.asistencia.strategy.anotacion.AnotacionContext;
import cl.colegio.ohiggins.asistencia.strategy.anotacion.AnotacionStrategy;
import cl.colegio.ohiggins.asistencia.strategy.anotacion.AnotacionStrategyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/anotaciones")
public class AnotacionController {

    @Autowired
    private AnotacionStrategyFactory factory;

    @Autowired
    private AnotacionQueryService queryService;

    @PostMapping
    public ResponseEntity<AnotacionDTO> registrar(@RequestBody AnotacionRequest req) {

        AnotacionStrategy strategy = factory.get(req.getTipo());

        AnotacionContext ctx = new AnotacionContext();
        ctx.setAlumnoId(req.getAlumnoId());
        ctx.setRequest(req);

        AnotacionDTO dto = strategy.ejecutar(ctx);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(dto);
    }

    @GetMapping("/alumno/{alumnoId}")
    public List<AnotacionDTO> porAlumno(@PathVariable Long alumnoId) {

        return queryService.anotacionesAlumno(alumnoId);
    }
}