package com.microservicio.mensajeria.facade;

import com.microservicio.mensajeria.dto.MensajeRequest;
import com.microservicio.mensajeria.dto.MensajeResponse;
import com.microservicio.mensajeria.service.MensajeService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MensajeriaFacade {

    private final MensajeService mensajeService;

    public MensajeriaFacade(MensajeService mensajeService) {
        this.mensajeService = mensajeService;
    }

    public MensajeResponse publicarComunicado(MensajeRequest request) {
        return mensajeService.crearMensaje(request);
    }

    public MensajeResponse enviarMensajeDirecto(MensajeRequest request) {
        return mensajeService.crearMensaje(request);
    }

    public List<MensajeResponse> listarTodos() {
        return mensajeService.listarMensajes();
    }

    public MensajeResponse obtenerMensaje(Long id) {
        return mensajeService.obtenerPorId(id);
    }

    public List<MensajeResponse> obtenerMensajesPorDestinatario(Long destinatarioId) {
        return mensajeService.obtenerPorDestinatario(destinatarioId);
    }

    public List<MensajeResponse> obtenerMensajesPorRemitente(Long remitenteId) {
        return mensajeService.obtenerPorRemitente(remitenteId);
    }

    public MensajeResponse marcarMensajeComoLeido(Long id) {
        return mensajeService.marcarComoLeido(id);
    }

    public void eliminarMensaje(Long id) {
        mensajeService.eliminarMensaje(id);
    }
}