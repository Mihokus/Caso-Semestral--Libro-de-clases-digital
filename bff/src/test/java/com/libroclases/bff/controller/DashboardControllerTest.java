package com.libroclases.bff.controller;

import com.libroclases.bff.dto.dashboard.DashboardDTO;
import com.libroclases.bff.service.dashboard.DashboardService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DashboardControllerTest {

    @Mock private DashboardService service;
    @InjectMocks private DashboardController controller;

    @Test
    void getDelegaEnElServicioConLosHeaders() {
        DashboardDTO dto = mock(DashboardDTO.class);
        when(service.build(1L, "Ana", "ana@colegio.cl", "DOCENTE", 5L)).thenReturn(dto);

        DashboardDTO result = controller.get(1L, "Ana", "ana@colegio.cl", "DOCENTE", 5L);

        assertThat(result).isSameAs(dto);
    }
}
