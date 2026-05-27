package com.libroclases.bff.controller;

import com.libroclases.bff.dto.AsignaturaDTO;
import com.libroclases.bff.dto.AsignaturaRequest;
import com.libroclases.bff.dto.CursoDTO;
import com.libroclases.bff.dto.CursoRequest;
import com.libroclases.bff.dto.EvaluacionDTO;
import com.libroclases.bff.dto.EvaluacionRequest;
import com.libroclases.bff.dto.RendimientoDTO;
import com.libroclases.bff.service.academica.AcademicaCommandService;
import com.libroclases.bff.service.academica.AcademicaQueryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AcademicaController {

    private final AcademicaQueryService queryService;
    private final AcademicaCommandService commandService;

    @GetMapping("/api/asignaturas")
    public List<AsignaturaDTO> listAsignaturas() {
        return queryService.listAsignaturas();
    }

    @GetMapping("/api/asignaturas/{id}")
    public AsignaturaDTO getAsignatura(@PathVariable Long id) {
        return queryService.getAsignatura(id);
    }

    @PostMapping("/api/asignaturas")
    public ResponseEntity<AsignaturaDTO> crearAsignatura(
            @Valid @RequestBody AsignaturaRequest req,
            @RequestHeader(value = "X-User-Name", defaultValue = "anonymous") String docenteNombre) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(commandService.crearAsignatura(req, docenteNombre));
    }

    @GetMapping("/api/asignaturas/{id}/rendimiento")
    public RendimientoDTO rendimiento(@PathVariable Long id) {
        return queryService.rendimiento(id);
    }

    @GetMapping("/api/cursos")
    public List<CursoDTO> listCursos() {
        return queryService.listCursos();
    }

    @PostMapping("/api/cursos")
    public ResponseEntity<CursoDTO> crearCurso(@Valid @RequestBody CursoRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(commandService.crearCurso(req.getNombre(), req.getNivel()));
    }

    @PostMapping("/api/notas")
    public ResponseEntity<EvaluacionDTO> registrarNota(
            @Valid @RequestBody EvaluacionRequest req,
            @RequestHeader(value = "X-User-Id", defaultValue = "0") Long userId,
            @RequestHeader(value = "X-User-Name", defaultValue = "anonymous") String userName) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(commandService.registrarNota(req, userId, userName));
    }

    @GetMapping("/api/notas/alumno/{alumnoId}")
    public List<EvaluacionDTO> notasAlumno(@PathVariable Long alumnoId) {
        return queryService.notasAlumno(alumnoId);
    }
}
