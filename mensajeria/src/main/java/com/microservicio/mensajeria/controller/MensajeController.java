package com.microservicio.mensajeria.controller;

import com.microservicio.mensajeria.dto.MensajeRequest;
import com.microservicio.mensajeria.dto.MensajeResponse;
import com.microservicio.mensajeria.facade.MensajeriaFacade;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mensajes")
@CrossOrigin(origins = "*")
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
    public MensajeResponse enviarMensajeDirecto(@RequestBody MensajeRequest request) {
        return mensajeriaFacade.enviarMensajeDirecto(request);
    }

    @GetMapping
    public List<MensajeResponse> listarMensajes() {
        return mensajeriaFacade.listarTodos();
    }

    @GetMapping("/{id}")
    public MensajeResponse obtenerMensaje(@PathVariable Long id) {
        return mensajeriaFacade.obtenerMensaje(id);
    }

    @GetMapping("/destinatario/{destinatarioId}")
    public List<MensajeResponse> obtenerPorDestinatario(@PathVariable Long destinatarioId) {
        return mensajeriaFacade.obtenerMensajesPorDestinatario(destinatarioId);
    }

    @GetMapping("/remitente/{remitenteId}")
    public List<MensajeResponse> obtenerPorRemitente(@PathVariable Long remitenteId) {
        return mensajeriaFacade.obtenerMensajesPorRemitente(remitenteId);
    }

    @PutMapping("/{id}/leido")
    public MensajeResponse marcarComoLeido(@PathVariable Long id) {
        return mensajeriaFacade.marcarMensajeComoLeido(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarMensaje(@PathVariable Long id) {
        mensajeriaFacade.eliminarMensaje(id);
    }
}