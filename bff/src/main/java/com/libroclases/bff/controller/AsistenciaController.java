package com.libroclases.bff.controller;

import com.libroclases.bff.dto.AlumnoDTO;
import com.libroclases.bff.dto.AnotacionDTO;
import com.libroclases.bff.dto.AnotacionRequest;
import com.libroclases.bff.dto.AsistenciaBulkRequest;
import com.libroclases.bff.dto.AsistenciaDTO;
import com.libroclases.bff.service.asistencia.AsistenciaCommandService;
import com.libroclases.bff.service.asistencia.AsistenciaQueryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class AsistenciaController {

    private final AsistenciaQueryService queryService;
    private final AsistenciaCommandService commandService;

    @GetMapping("/api/alumnos")
    public List<AlumnoDTO> listAlumnos() {
        return queryService.listAlumnos();
    }

    @GetMapping("/api/alumnos/{id}")
    public AlumnoDTO getAlumno(@PathVariable Long id) {
        return queryService.getAlumno(id);
    }

    @GetMapping("/api/cursos/{cursoId}/alumnos")
    public List<AlumnoDTO> listByCurso(@PathVariable Long cursoId) {
        return queryService.listByCurso(cursoId);
    }

    @PostMapping("/api/asistencia")
    public ResponseEntity<List<AsistenciaDTO>> registrarBulk(
            @Valid @RequestBody AsistenciaBulkRequest req,
            @RequestHeader(value = "X-User-Id", defaultValue = "0") Long userId,
            @RequestHeader(value = "X-User-Name", defaultValue = "anonymous") String userName) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(commandService.registrarBulk(req, userId, userName));
    }

    @GetMapping("/api/asistencia/alumno/{alumnoId}")
    public List<AsistenciaDTO> historialAlumno(@PathVariable Long alumnoId) {
        return queryService.historialAlumno(alumnoId);
    }

    @GetMapping("/api/asistencia/curso/{cursoId}/{fecha}")
    public List<AsistenciaDTO> porCursoYFecha(@PathVariable Long cursoId,
                                              @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return queryService.porCursoYFecha(cursoId, fecha);
    }

    @PostMapping("/api/anotaciones")
    public ResponseEntity<AnotacionDTO> registrarAnotacion(
            @Valid @RequestBody AnotacionRequest req,
            @RequestHeader(value = "X-User-Id", defaultValue = "0") Long userId,
            @RequestHeader(value = "X-User-Name", defaultValue = "anonymous") String userName) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(commandService.registrarAnotacion(req, userId, userName));
    }

    @GetMapping("/api/anotaciones/alumno/{alumnoId}")
    public List<AnotacionDTO> anotacionesAlumno(@PathVariable Long alumnoId) {
        return queryService.anotacionesAlumno(alumnoId);
    }
}
