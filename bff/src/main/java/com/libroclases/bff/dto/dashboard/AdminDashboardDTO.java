package com.libroclases.bff.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminDashboardDTO implements DashboardDTO {
    private String rol;
    private UserSummary user;
    private Integer totalUsuarios;
    private Integer totalCursos;
    private Integer totalAsignaturas;
    private Integer mensajesNoLeidos;
}
