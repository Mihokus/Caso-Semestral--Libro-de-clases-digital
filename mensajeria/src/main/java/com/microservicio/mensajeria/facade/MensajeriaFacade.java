package com.microservicio.mensajeria.facade;

import com.microservicio.mensajeria.dto.MensajeRequest;
import com.microservicio.mensajeria.dto.MensajeResponse;
import com.microservicio.mensajeria.model.TipoMensaje;
import com.microservicio.mensajeria.service.MensajeriaCommandService;
import com.microservicio.mensajeria.service.MensajeriaQueryService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MensajeriaFacade {

    private final MensajeriaCommandService mensajeriaCommandService;
    private final MensajeriaQueryService mensajeriaQueryService;

    public MensajeriaFacade(MensajeriaCommandService mensajeriaCommandService,
                            MensajeriaQueryService mensajeriaQueryService) {
        this.mensajeriaCommandService = mensajeriaCommandService;
        this.mensajeriaQueryService = mensajeriaQueryService;
    }

    public MensajeResponse publicarComunicado(MensajeRequest request) {
        validarBase(request);
        if (request.getCursoId() == null) {
            throw new IllegalArgumentException("El cursoId es obligatorio para un comunicado");
        }
        request.setDestinatarioId(null);
        request.setDestinatarioNombre(null);
        request.setDestinatarioRol(null);
        return mensajeriaCommandService.crearMensaje(request, TipoMensaje.COMUNICADO);
    }

    public MensajeResponse enviarDirecto(MensajeRequest request) {
        validarBase(request);
        if (request.getDestinatarioId() == null) {
            throw new IllegalArgumentException("El mensaje directo requiere destinatarioId");
        }
        return mensajeriaCommandService.crearMensaje(request, TipoMensaje.DIRECTO);
    }

    public List<MensajeResponse> obtenerInbox(Long userId) {
        validarId(userId, "userId");
        return mensajeriaQueryService.obtenerInbox(userId);
    }

    public List<MensajeResponse> obtenerEnviadosPorRemitente(Long usuarioId) {
        validarId(usuarioId, "usuarioId");
        return mensajeriaQueryService.obtenerPorRemitente(usuarioId);
    }

    public MensajeResponse obtenerDetalleMensaje(Long mensajeId) {
        validarId(mensajeId, "mensajeId");
        return mensajeriaQueryService.obtenerPorId(mensajeId);
    }

    public MensajeResponse registrarLecturaMensaje(Long mensajeId) {
        validarId(mensajeId, "mensajeId");
        return mensajeriaCommandService.marcarComoLeido(mensajeId);
    }

    public void eliminarMensaje(Long mensajeId) {
        validarId(mensajeId, "mensajeId");
        mensajeriaCommandService.eliminarMensaje(mensajeId);
    }

    private void validarBase(MensajeRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("El mensaje no puede ser nulo");
        }
        if (request.getTitulo() == null || request.getTitulo().isBlank()) {
            throw new IllegalArgumentException("El título del mensaje es obligatorio");
        }
        if (request.getContenido() == null || request.getContenido().isBlank()) {
            throw new IllegalArgumentException("El contenido del mensaje es obligatorio");
        }
        if (request.getRemitenteId() == null) {
            throw new IllegalArgumentException("El remitenteId es obligatorio");
        }
        if (request.getRemitenteNombre() == null || request.getRemitenteNombre().isBlank()) {
            throw new IllegalArgumentException("El remitenteNombre es obligatorio");
        }
    }

    private void validarId(Long id, String nombreCampo) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El campo " + nombreCampo + " debe ser válido");
        }
    }
}
