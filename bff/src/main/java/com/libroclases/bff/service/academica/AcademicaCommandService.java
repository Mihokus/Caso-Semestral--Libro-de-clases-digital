package com.libroclases.bff.service.academica;

import com.libroclases.bff.client.AcademicaClient;
import com.libroclases.bff.client.dto.RawAsignaturaRequest;
import com.libroclases.bff.client.dto.RawCursoRequest;
import com.libroclases.bff.client.dto.RawEvaluacionRequest;
import com.libroclases.bff.dto.AsignaturaDTO;
import com.libroclases.bff.dto.AsignaturaRequest;
import com.libroclases.bff.dto.CursoDTO;
import com.libroclases.bff.dto.EvaluacionDTO;
import com.libroclases.bff.dto.EvaluacionRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AcademicaCommandService {

    private final AcademicaClient client;

    public AsignaturaDTO crearAsignatura(AsignaturaRequest req, String docenteNombre) {
        RawAsignaturaRequest raw = RawAsignaturaRequest.builder()
                .nombre(req.getNombre())
                .cursoId(req.getCursoId())
                .docenteId(req.getDocenteId())
                .docenteNombre(docenteNombre)
                .build();
        return client.crearAsignatura(raw);
    }

    public CursoDTO crearCurso(String nombre, String nivel) {
        RawCursoRequest raw = RawCursoRequest.builder()
                .nombre(nombre)
                .nivel(nivel)
                .build();
        return client.crearCurso(raw);
    }

    public EvaluacionDTO registrarNota(EvaluacionRequest req, Long userId, String userName) {
        RawEvaluacionRequest raw = RawEvaluacionRequest.builder()
                .alumnoId(req.getAlumnoId())
                .asignaturaId(req.getAsignaturaId())
                .nombre(req.getNombre())
                .nota(req.getNota())
                .ponderacion(req.getPonderacion())
                .registradoPorId(userId)
                .registradoPorNombre(userName)
                .build();
        return client.registrarNota(raw);
    }
}
