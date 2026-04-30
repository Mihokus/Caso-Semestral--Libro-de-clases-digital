package com.libroclases.bff.service.asistencia;

import com.libroclases.bff.client.AsistenciaClient;
import com.libroclases.bff.dto.AlumnoDTO;
import com.libroclases.bff.dto.AnotacionDTO;
import com.libroclases.bff.dto.AsistenciaDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AsistenciaQueryService {

    private final AsistenciaClient client;

    public List<AlumnoDTO> listAlumnos() {
        return client.listAlumnos();
    }

    public AlumnoDTO getAlumno(Long id) {
        return client.getAlumno(id);
    }

    public List<AlumnoDTO> listByCurso(Long cursoId) {
        return client.listByCurso(cursoId);
    }

    public List<AsistenciaDTO> historialAlumno(Long alumnoId) {
        return client.historialAlumno(alumnoId);
    }

    public List<AsistenciaDTO> porCursoYFecha(Long cursoId, LocalDate fecha) {
        return client.porCursoYFecha(cursoId, fecha);
    }

    public List<AnotacionDTO> anotacionesAlumno(Long alumnoId) {
        return client.anotacionesAlumno(alumnoId);
    }
}
