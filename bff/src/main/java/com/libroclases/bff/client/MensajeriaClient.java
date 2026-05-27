package com.libroclases.bff.client;

import com.libroclases.bff.client.dto.RawMensajeComunicadoRequest;
import com.libroclases.bff.client.dto.RawMensajeDirectoRequest;
import com.libroclases.bff.dto.MensajeDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "mensajeria", url = "${mensajeria.url:http://mensajeria-app:8083}")
public interface MensajeriaClient {

    @PostMapping("/api/mensajes/directo")
    MensajeDTO enviarDirecto(@RequestBody RawMensajeDirectoRequest req);

    @PostMapping("/api/mensajes/comunicado")
    MensajeDTO enviarComunicado(@RequestBody RawMensajeComunicadoRequest req);

    @GetMapping("/api/mensajes/inbox/{userId}")
    List<MensajeDTO> inbox(@PathVariable("userId") Long userId);

    @GetMapping("/api/mensajes/remitente/{userId}")
    List<MensajeDTO> enviados(@PathVariable("userId") Long userId);

    @GetMapping("/api/mensajes/{id}")
    MensajeDTO byId(@PathVariable("id") Long id);

    @PutMapping("/api/mensajes/{id}/leido")
    MensajeDTO marcarLeido(@PathVariable("id") Long id);

    @DeleteMapping("/api/mensajes/{id}")
    void eliminar(@PathVariable("id") Long id);
}
