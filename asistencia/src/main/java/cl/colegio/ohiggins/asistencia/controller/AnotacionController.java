package cl.colegio.ohiggins.asistencia.controller;

import cl.colegio.ohiggins.asistencia.dto.AnotacionDTO;
import cl.colegio.ohiggins.asistencia.dto.AnotacionRequest;
import cl.colegio.ohiggins.asistencia.facade.AsistenciaFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/anotaciones")
public class AnotacionController {

    @Autowired private AsistenciaFacade facade;

    @PostMapping
    public ResponseEntity<AnotacionDTO> registrar(@RequestBody AnotacionRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(facade.registrarAnotacion(req));
    }

    @GetMapping("/alumno/{alumnoId}")
    public List<AnotacionDTO> anotacionesAlumno(@PathVariable Long alumnoId) {
        return facade.anotacionesAlumno(alumnoId);
    }
}
