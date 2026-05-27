package com.libroclases.bff.service.dashboard;

import com.libroclases.bff.dto.AlumnoDTO;
import com.libroclases.bff.dto.AsignaturaDTO;
import com.libroclases.bff.dto.dashboard.ApoderadoDashboardDTO;
import com.libroclases.bff.dto.dashboard.UserSummary;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class DashboardContext {
    private UserSummary user;
    private List<AsignaturaDTO> asignaturas;
    private Integer alumnosACargo;
    private Integer mensajesNoLeidos;
    private Integer evaluacionesPendientes;
    private List<ApoderadoDashboardDTO.PupiloResumen> pupilos;
    private AlumnoDTO alumno;
    private Double asistenciaPorcentaje;
    private Double promedioGeneral;
    private Integer totalUsuarios;
    private Integer totalCursos;
    private Integer totalAsignaturas;
}
