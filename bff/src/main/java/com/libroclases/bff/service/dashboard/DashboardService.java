package com.libroclases.bff.service.dashboard;

import com.libroclases.bff.dto.dashboard.DashboardDTO;
import com.libroclases.bff.dto.dashboard.UserSummary;
import com.libroclases.bff.service.academica.AcademicaQueryService;
import com.libroclases.bff.service.asistencia.AsistenciaQueryService;
import com.libroclases.bff.service.mensajeria.MensajeriaQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

@Slf4j
@Service
@RequiredArgsConstructor
public class DashboardService {

    private final AsistenciaQueryService asistenciaQuery;
    private final MensajeriaQueryService mensajeriaQuery;
    private final AcademicaQueryService academicaQuery;
    private final DashboardDTOFactory factory;

    public DashboardDTO build(Long userId, String userName, String email, String role, Long entityId) {
        UserSummary user = UserSummary.builder()
                .id(userId)
                .email(email)
                .nombre(userName)
                .build();

        DashboardContext.DashboardContextBuilder ctx = DashboardContext.builder()
                .user(user)
                .mensajesNoLeidos(safeUnreadCount(userId));

        String upper = role == null ? "" : role.toUpperCase();
        switch (upper) {
            case "DOCENTE" -> ctx.asignaturas(safeList(academicaQuery::listAsignaturas))
                    .alumnosACargo(0)
                    .evaluacionesPendientes(0);
            case "APODERADO" -> ctx.pupilos(List.of());
            case "ESTUDIANTE" -> {
                if (entityId != null && entityId > 0) {
                    try {
                        ctx.alumno(asistenciaQuery.getAlumno(entityId));
                    } catch (Exception e) {
                        log.warn("Failed to fetch alumno for entityId={}: {}", entityId, e.getMessage());
                    }
                }
            }
            case "ADMIN" -> ctx.totalAsignaturas(safeCount(academicaQuery::listAsignaturas))
                    .totalCursos(safeCount(academicaQuery::listCursos))
                    .totalUsuarios(0);
            default -> throw new IllegalArgumentException("Unknown role: " + role);
        }

        return factory.create(upper, ctx.build());
    }

    private <T> List<T> safeList(Supplier<List<T>> sup) {
        try {
            return sup.get();
        } catch (Exception e) {
            log.warn("safeList failed: {}", e.getMessage());
            return List.of();
        }
    }

    private Integer safeCount(Supplier<? extends Collection<?>> sup) {
        try {
            return sup.get().size();
        } catch (Exception e) {
            log.warn("safeCount failed: {}", e.getMessage());
            return 0;
        }
    }

    private Integer safeUnreadCount(Long userId) {
        try {
            return (int) mensajeriaQuery.inbox(userId).stream().filter(m -> !m.isLeido()).count();
        } catch (Exception e) {
            log.warn("safeUnreadCount failed: {}", e.getMessage());
            return 0;
        }
    }
}
