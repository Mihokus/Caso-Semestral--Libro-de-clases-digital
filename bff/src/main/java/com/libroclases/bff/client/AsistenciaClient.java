package com.libroclases.bff.client;

import com.libroclases.bff.client.dto.RawAnotacionRequest;
import com.libroclases.bff.client.dto.RawAsistenciaBulkRequest;
import com.libroclases.bff.dto.AlumnoDTO;
import com.libroclases.bff.dto.AnotacionDTO;
import com.libroclases.bff.dto.ApoderadoInfo;
import com.libroclases.bff.dto.AsistenciaDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDate;
import java.util.List;

@FeignClient(name = "asistencia", url = "${asistencia.url:http://asistencia-app:8081}")
public interface AsistenciaClient {

    @GetMapping("/api/alumnos")
    List<AlumnoDTO> listAlumnos();

    @GetMapping("/api/alumnos/{id}")
    AlumnoDTO getAlumno(@PathVariable("id") Long id);

    @GetMapping("/api/alumnos/{id}/apoderados")
    List<ApoderadoInfo> getApoderadosOf(@PathVariable("id") Long id);

    @GetMapping("/api/cursos/{cursoId}/alumnos")
    List<AlumnoDTO> listByCurso(@PathVariable("cursoId") Long cursoId);

    @PostMapping("/api/asistencia")
    List<AsistenciaDTO> registrarBulk(@RequestBody RawAsistenciaBulkRequest req);

    @GetMapping("/api/asistencia/alumno/{alumnoId}")
    List<AsistenciaDTO> historialAlumno(@PathVariable("alumnoId") Long alumnoId);

    @GetMapping("/api/asistencia/curso/{cursoId}/{fecha}")
    List<AsistenciaDTO> porCursoYFecha(@PathVariable("cursoId") Long cursoId,
                                       @PathVariable("fecha") LocalDate fecha);

    @PostMapping("/api/anotaciones")
    AnotacionDTO registrarAnotacion(@RequestBody RawAnotacionRequest req);

    @GetMapping("/api/anotaciones/alumno/{alumnoId}")
    List<AnotacionDTO> anotacionesAlumno(@PathVariable("alumnoId") Long alumnoId);
}
