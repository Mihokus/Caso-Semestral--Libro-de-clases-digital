package com.libroclases.bff.controller;

import com.libroclases.bff.dto.MensajeComunicadoRequest;
import com.libroclases.bff.dto.MensajeDTO;
import com.libroclases.bff.dto.MensajeDirectoRequest;
import com.libroclases.bff.service.mensajeria.MensajeriaCommandService;
import com.libroclases.bff.service.mensajeria.MensajeriaQueryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/mensajes")
@RequiredArgsConstructor
public class MensajeriaController {

    private final MensajeriaQueryService queryService;
    private final MensajeriaCommandService commandService;

    @GetMapping
    public List<MensajeDTO> inbox(@RequestHeader(value = "X-User-Id", defaultValue = "0") Long userId) {
        return queryService.inbox(userId);
    }

    @GetMapping("/enviados")
    public List<MensajeDTO> enviados(@RequestHeader(value = "X-User-Id", defaultValue = "0") Long userId) {
        return queryService.enviados(userId);
    }

    @GetMapping("/{id}")
    public MensajeDTO byId(@PathVariable Long id) {
        return queryService.byId(id);
    }

    @PostMapping("/directo")
    public ResponseEntity<MensajeDTO> enviarDirecto(
            @Valid @RequestBody MensajeDirectoRequest req,
            @RequestHeader(value = "X-User-Id", defaultValue = "0") Long userId,
            @RequestHeader(value = "X-User-Name", defaultValue = "anonymous") String userName,
            @RequestHeader(value = "X-User-Role", defaultValue = "") String userRol) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(commandService.enviarDirecto(req, userId, userName, userRol));
    }

    @PostMapping("/comunicado")
    public ResponseEntity<MensajeDTO> enviarComunicado(
            @Valid @RequestBody MensajeComunicadoRequest req,
            @RequestHeader(value = "X-User-Id", defaultValue = "0") Long userId,
            @RequestHeader(value = "X-User-Name", defaultValue = "anonymous") String userName,
            @RequestHeader(value = "X-User-Role", defaultValue = "") String userRol) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(commandService.enviarComunicado(req, userId, userName, userRol));
    }

    @PutMapping("/{id}/leido")
    public MensajeDTO marcarLeido(@PathVariable Long id) {
        return commandService.marcarLeido(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        commandService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
