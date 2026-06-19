package com.libroclases.bff.dto.dashboard;

import com.libroclases.bff.dto.AlumnoDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EstudianteDashboardDTO implements DashboardDTO {
    private String rol;
    private UserSummary user;
    private AlumnoDTO alumno;
    private Double asistenciaPorcentaje;
    private Double promedioGeneral;
    private Integer mensajesNoLeidos;
}
