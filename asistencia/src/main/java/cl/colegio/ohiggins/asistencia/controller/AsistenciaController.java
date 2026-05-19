package cl.colegio.ohiggins.asistencia.controller;

import cl.colegio.ohiggins.asistencia.dto.AsistenciaBulkRequest;
import cl.colegio.ohiggins.asistencia.dto.AsistenciaDTO;
import cl.colegio.ohiggins.asistencia.facade.AsistenciaFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/asistencia")
public class AsistenciaController {

    @Autowired private AsistenciaFacade facade;

    @PostMapping
    public ResponseEntity<List<AsistenciaDTO>> registrarBulk(@RequestBody AsistenciaBulkRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(facade.registrarAsistenciaBulk(req));
    }

    @GetMapping("/alumno/{alumnoId}")
    public List<AsistenciaDTO> historialAlumno(@PathVariable Long alumnoId) {
        return facade.historialAlumno(alumnoId);
    }

    @GetMapping("/curso/{cursoId}/{fecha}")
    public List<AsistenciaDTO> porCursoYFecha(@PathVariable Long cursoId,
                                              @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return facade.porCursoYFecha(cursoId, fecha);
    }
}
