package com.microservicio.mensajeria.controller;

import com.microservicio.mensajeria.dto.MensajeRequest;
import com.microservicio.mensajeria.dto.MensajeResponse;
import com.microservicio.mensajeria.facade.MensajeriaFacade;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/mensajes")
public class MensajeController {

    private final MensajeriaFacade mensajeriaFacade;

    public MensajeController(MensajeriaFacade mensajeriaFacade) {
        this.mensajeriaFacade = mensajeriaFacade;
    }

    @PostMapping("/comunicado")
    @ResponseStatus(HttpStatus.CREATED)
    public MensajeResponse publicarComunicado(@RequestBody MensajeRequest request) {
        return mensajeriaFacade.publicarComunicado(request);
    }

    @PostMapping("/directo")
    @ResponseStatus(HttpStatus.CREATED)
    public MensajeResponse enviarDirecto(@RequestBody MensajeRequest request) {
        return mensajeriaFacade.enviarDirecto(request);
    }

    @GetMapping("/inbox/{userId}")
    public List<MensajeResponse> obtenerInbox(@PathVariable Long userId) {
        return mensajeriaFacade.obtenerInbox(userId);
    }

    @GetMapping("/remitente/{userId}")
    public List<MensajeResponse> obtenerEnviadosPorRemitente(@PathVariable Long userId) {
        return mensajeriaFacade.obtenerEnviadosPorRemitente(userId);
    }

    @GetMapping("/{id}")
    public MensajeResponse obtenerDetalleMensaje(@PathVariable Long id) {
        return mensajeriaFacade.obtenerDetalleMensaje(id);
    }

    @PutMapping("/{id}/leido")
    public MensajeResponse registrarLecturaMensaje(@PathVariable Long id) {
        return mensajeriaFacade.registrarLecturaMensaje(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarMensaje(@PathVariable Long id) {
        mensajeriaFacade.eliminarMensaje(id);
    }
}
