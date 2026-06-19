package com.libroclases.bff.client;

import com.libroclases.bff.client.dto.RawAsignaturaRequest;
import com.libroclases.bff.client.dto.RawCursoRequest;
import com.libroclases.bff.client.dto.RawEvaluacionRequest;
import com.libroclases.bff.dto.AsignaturaDTO;
import com.libroclases.bff.dto.CursoDTO;
import com.libroclases.bff.dto.EvaluacionDTO;
import com.libroclases.bff.dto.RendimientoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "academica", url = "${academica.url:http://academic-service:8084}")
public interface AcademicaClient {

    @GetMapping("/api/academica/asignaturas")
    List<AsignaturaDTO> listAsignaturas();

    @GetMapping("/api/academica/asignaturas/{id}")
    AsignaturaDTO getAsignatura(@PathVariable("id") Long id);

    @PostMapping("/api/academica/asignaturas")
    AsignaturaDTO crearAsignatura(@RequestBody RawAsignaturaRequest req);

    @GetMapping("/api/academica/rendimiento/{asignaturaId}")
    RendimientoDTO rendimiento(@PathVariable("asignaturaId") Long asignaturaId);

    @GetMapping("/api/academica/cursos")
    List<CursoDTO> listCursos();

    @PostMapping("/api/academica/cursos")
    CursoDTO crearCurso(@RequestBody RawCursoRequest req);

    @PostMapping("/api/academica/notas")
    EvaluacionDTO registrarNota(@RequestBody RawEvaluacionRequest req);

    @GetMapping("/api/academica/notas/alumno/{alumnoId}")
    List<EvaluacionDTO> notasAlumno(@PathVariable("alumnoId") Long alumnoId);
}
