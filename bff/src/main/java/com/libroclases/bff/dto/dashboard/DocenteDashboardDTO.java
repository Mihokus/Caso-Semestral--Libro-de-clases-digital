package com.libroclases.bff.dto.dashboard;

import com.libroclases.bff.dto.AsignaturaDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocenteDashboardDTO implements DashboardDTO {
    private String rol;
    private UserSummary user;
    private List<AsignaturaDTO> asignaturas;
    private Integer alumnosACargo;
    private Integer mensajesNoLeidos;
    private Integer evaluacionesPendientes;
}
