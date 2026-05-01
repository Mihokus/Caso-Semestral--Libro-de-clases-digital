package com.libroclases.bff.service.dashboard;

import com.libroclases.bff.dto.dashboard.AdminDashboardDTO;
import com.libroclases.bff.dto.dashboard.ApoderadoDashboardDTO;
import com.libroclases.bff.dto.dashboard.DashboardDTO;
import com.libroclases.bff.dto.dashboard.DocenteDashboardDTO;
import com.libroclases.bff.dto.dashboard.EstudianteDashboardDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DashboardDTOFactory {

    public DashboardDTO create(String role, DashboardContext ctx) {
        return switch (role == null ? "" : role.toUpperCase()) {
            case "DOCENTE" -> docente(ctx);
            case "APODERADO" -> apoderado(ctx);
            case "ESTUDIANTE" -> estudiante(ctx);
            case "ADMIN" -> admin(ctx);
            default -> throw new IllegalArgumentException("Unknown role: " + role);
        };
    }

    private DocenteDashboardDTO docente(DashboardContext ctx) {
        return DocenteDashboardDTO.builder()
                .rol("DOCENTE")
                .user(ctx.getUser())
                .asignaturas(ctx.getAsignaturas() == null ? List.of() : ctx.getAsignaturas())
                .alumnosACargo(ctx.getAlumnosACargo() == null ? 0 : ctx.getAlumnosACargo())
                .mensajesNoLeidos(ctx.getMensajesNoLeidos() == null ? 0 : ctx.getMensajesNoLeidos())
                .evaluacionesPendientes(ctx.getEvaluacionesPendientes() == null ? 0 : ctx.getEvaluacionesPendientes())
                .build();
    }

    private ApoderadoDashboardDTO apoderado(DashboardContext ctx) {
        return ApoderadoDashboardDTO.builder()
                .rol("APODERADO")
                .user(ctx.getUser())
                .pupilos(ctx.getPupilos() == null ? List.of() : ctx.getPupilos())
                .mensajesNoLeidos(ctx.getMensajesNoLeidos() == null ? 0 : ctx.getMensajesNoLeidos())
                .build();
    }

    private EstudianteDashboardDTO estudiante(DashboardContext ctx) {
        return EstudianteDashboardDTO.builder()
                .rol("ESTUDIANTE")
                .user(ctx.getUser())
                .alumno(ctx.getAlumno())
                .asistenciaPorcentaje(ctx.getAsistenciaPorcentaje())
                .promedioGeneral(ctx.getPromedioGeneral())
                .mensajesNoLeidos(ctx.getMensajesNoLeidos() == null ? 0 : ctx.getMensajesNoLeidos())
                .build();
    }

    private AdminDashboardDTO admin(DashboardContext ctx) {
        return AdminDashboardDTO.builder()
                .rol("ADMIN")
                .user(ctx.getUser())
                .totalUsuarios(ctx.getTotalUsuarios() == null ? 0 : ctx.getTotalUsuarios())
                .totalCursos(ctx.getTotalCursos() == null ? 0 : ctx.getTotalCursos())
                .totalAsignaturas(ctx.getTotalAsignaturas() == null ? 0 : ctx.getTotalAsignaturas())
                .mensajesNoLeidos(ctx.getMensajesNoLeidos() == null ? 0 : ctx.getMensajesNoLeidos())
                .build();
    }
}
