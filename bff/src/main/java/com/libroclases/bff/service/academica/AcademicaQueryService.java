package com.libroclases.bff.service.academica;

import com.libroclases.bff.client.AcademicaClient;
import com.libroclases.bff.dto.AsignaturaDTO;
import com.libroclases.bff.dto.CursoDTO;
import com.libroclases.bff.dto.EvaluacionDTO;
import com.libroclases.bff.dto.RendimientoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AcademicaQueryService {

    private final AcademicaClient client;

    public List<AsignaturaDTO> listAsignaturas() {
        return client.listAsignaturas();
    }

    public AsignaturaDTO getAsignatura(Long id) {
        return client.getAsignatura(id);
    }

    public RendimientoDTO rendimiento(Long asignaturaId) {
        return client.rendimiento(asignaturaId);
    }

    public List<CursoDTO> listCursos() {
        return client.listCursos();
    }

    public List<EvaluacionDTO> notasAlumno(Long alumnoId) {
        return client.notasAlumno(alumnoId);
    }
}
