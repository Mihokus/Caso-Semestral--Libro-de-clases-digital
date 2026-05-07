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

    // Caso de uso: publicar mensajes masivos a la comunidad educativa
    @PostMapping("/difusion")
    @ResponseStatus(HttpStatus.CREATED)
    public MensajeResponse publicarMensajeDifusion(@RequestBody MensajeRequest request) {
        return mensajeriaFacade.publicarMensajeDifusion(request);
    }

    // Caso de uso: enviar comunicación directa entre usuarios
    @PostMapping("/conversacion/directa")
    @ResponseStatus(HttpStatus.CREATED)
    public MensajeResponse enviarComunicacionDirecta(@RequestBody MensajeRequest request) {
        return mensajeriaFacade.enviarComunicacionDirecta(request);
    }

    // Caso de uso: bandeja general de un usuario
    @GetMapping("/bandeja/{usuarioId}")
    public List<MensajeResponse> obtenerBandejaUsuario(@PathVariable Long usuarioId) {
        return mensajeriaFacade.obtenerBandejaUsuario(usuarioId);
    }

    // Caso de uso: historial de mensajes enviados por un usuario
    @GetMapping("/historial/{usuarioId}")
    public List<MensajeResponse> obtenerHistorialUsuario(@PathVariable Long usuarioId) {
        return mensajeriaFacade.obtenerHistorialUsuario(usuarioId);
    }

    // Caso de uso: detalle de un mensaje específico
    @GetMapping("/detalle/{mensajeId}")
    public MensajeResponse obtenerDetalleMensaje(@PathVariable Long mensajeId) {
        return mensajeriaFacade.obtenerDetalleMensaje(mensajeId);
    }

    // Caso de uso: registrar lectura del mensaje
    @PutMapping("/{mensajeId}/registrar-lectura")
    public MensajeResponse registrarLecturaMensaje(@PathVariable Long mensajeId) {
        return mensajeriaFacade.registrarLecturaMensaje(mensajeId);
    }

    // Caso de uso: archivar o eliminar mensaje
    @DeleteMapping("/{mensajeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarMensaje(@PathVariable Long mensajeId) {
        mensajeriaFacade.eliminarMensaje(mensajeId);
    }
}