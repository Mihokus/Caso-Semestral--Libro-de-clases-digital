package com.libroclases.bff.dto.dashboard;

import com.libroclases.bff.dto.AlumnoDTO;
import com.libroclases.bff.dto.AnotacionDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApoderadoDashboardDTO implements DashboardDTO {
    private String rol;
    private UserSummary user;
    private List<PupiloResumen> pupilos;
    private Integer mensajesNoLeidos;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PupiloResumen {
        private AlumnoDTO alumno;
        private Double asistenciaPorcentaje;
        private Double promedioGeneral;
        private List<AnotacionDTO> ultimasAnotaciones;
    }
}
