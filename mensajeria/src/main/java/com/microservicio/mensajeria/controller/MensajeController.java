package com.microservicio.mensajeria.controller;

import com.microservicio.mensajeria.dto.MensajeRequest;
import com.microservicio.mensajeria.dto.MensajeResponse;
import com.microservicio.mensajeria.model.TipoMensaje;
import com.microservicio.mensajeria.service.MensajeriaCommandService;
import com.microservicio.mensajeria.service.MensajeriaQueryService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mensajes")
@CrossOrigin(origins = "*")
public class MensajeController {

    private final MensajeriaCommandService commandService;
    private final MensajeriaQueryService queryService;

    public MensajeController(MensajeriaCommandService commandService,
                             MensajeriaQueryService queryService) {
        this.commandService = commandService;
        this.queryService = queryService;
    }

    @PostMapping("/comunicado")
    @ResponseStatus(HttpStatus.CREATED)
    public MensajeResponse crearComunicado(@RequestBody MensajeRequest request) {
        return commandService.crearMensaje(request, TipoMensaje.COMUNICADO);
    }

    @PostMapping("/directo")
    @ResponseStatus(HttpStatus.CREATED)
    public MensajeResponse crearMensajeDirecto(@RequestBody MensajeRequest request) {
        return commandService.crearMensaje(request, TipoMensaje.DIRECTO);
    }

    @GetMapping("/{id}")
    public MensajeResponse obtenerPorId(@PathVariable Long id) {
        return queryService.obtenerPorId(id);
    }

    @GetMapping("/remitente/{remitenteId}")
    public List<MensajeResponse> obtenerPorRemitente(@PathVariable Long remitenteId) {
        return queryService.obtenerPorRemitente(remitenteId);
    }

    @GetMapping("/inbox/{userId}")
    public List<MensajeResponse> obtenerInbox(@PathVariable Long userId) {
        return queryService.obtenerInbox(userId);
    }

    @PutMapping("/{id}/leido")
    public MensajeResponse marcarComoLeido(@PathVariable Long id) {
        return commandService.marcarComoLeido(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarMensaje(@PathVariable Long id) {
        commandService.eliminarMensaje(id);
    }
}