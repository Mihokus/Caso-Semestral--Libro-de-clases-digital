package com.microservicio.mensajeria.facade;

import com.microservicio.mensajeria.dto.MensajeRequest;
import com.microservicio.mensajeria.dto.MensajeResponse;
import com.microservicio.mensajeria.model.DestinatarioTipo;
import com.microservicio.mensajeria.model.TipoMensaje;
import com.microservicio.mensajeria.service.MensajeService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MensajeriaFacade {

    private final MensajeService mensajeService;

    public MensajeriaFacade(MensajeService mensajeService) {
        this.mensajeService = mensajeService;
    }

    public MensajeResponse publicarMensajeDifusion(MensajeRequest request) {
        validarMensajeBase(request);

        if (request.getTipoMensaje() == TipoMensaje.MENSAJE_DIRECTO) {
            throw new IllegalArgumentException("Un mensaje de difusión no puede ser de tipo MENSAJE_DIRECTO");
        }

        if (request.getDestinatarioTipo() == DestinatarioTipo.INDIVIDUAL) {
            throw new IllegalArgumentException("Un mensaje de difusión no puede tener destinatario INDIVIDUAL");
        }

        request.setDestinatarioId(null);
        request.setDestinatarioNombre(null);

        return mensajeService.crearMensaje(request);
    }

    public MensajeResponse enviarComunicacionDirecta(MensajeRequest request) {
        validarMensajeBase(request);

        if (request.getDestinatarioId() == null) {
            throw new IllegalArgumentException("El mensaje directo requiere destinatarioId");
        }

        if (request.getDestinatarioNombre() == null || request.getDestinatarioNombre().isBlank()) {
            throw new IllegalArgumentException("El mensaje directo requiere destinatarioNombre");
        }

        request.setTipoMensaje(TipoMensaje.MENSAJE_DIRECTO);
        request.setDestinatarioTipo(DestinatarioTipo.INDIVIDUAL);

        return mensajeService.crearMensaje(request);
    }

    public List<MensajeResponse> obtenerBandejaUsuario(Long usuarioId) {
        validarId(usuarioId, "usuarioId");
        return mensajeService.obtenerPorDestinatario(usuarioId);
    }

    public List<MensajeResponse> obtenerHistorialUsuario(Long usuarioId) {
        validarId(usuarioId, "usuarioId");
        return mensajeService.obtenerPorRemitente(usuarioId);
    }

    public MensajeResponse obtenerDetalleMensaje(Long mensajeId) {
        validarId(mensajeId, "mensajeId");
        return mensajeService.obtenerPorId(mensajeId);
    }

    public MensajeResponse registrarLecturaMensaje(Long mensajeId) {
        validarId(mensajeId, "mensajeId");
        return mensajeService.marcarComoLeido(mensajeId);
    }

    public void eliminarMensaje(Long mensajeId) {
        validarId(mensajeId, "mensajeId");
        mensajeService.eliminarMensaje(mensajeId);
    }

    private void validarMensajeBase(MensajeRequest request) {
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

        if (request.getTipoMensaje() == null) {
            throw new IllegalArgumentException("El tipoMensaje es obligatorio");
        }

        if (request.getDestinatarioTipo() == null) {
            throw new IllegalArgumentException("El destinatarioTipo es obligatorio");
        }
    }

    private void validarId(Long id, String nombreCampo) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El campo " + nombreCampo + " debe ser válido");
        }
    }
}