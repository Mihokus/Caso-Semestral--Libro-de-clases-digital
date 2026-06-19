package com.libroclases.bff.service.mensajeria;

import com.libroclases.bff.client.MensajeriaClient;
import com.libroclases.bff.client.dto.RawMensajeComunicadoRequest;
import com.libroclases.bff.client.dto.RawMensajeDirectoRequest;
import com.libroclases.bff.dto.MensajeComunicadoRequest;
import com.libroclases.bff.dto.MensajeDTO;
import com.libroclases.bff.dto.MensajeDirectoRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MensajeriaCommandService {

    private final MensajeriaClient client;

    public MensajeDTO enviarDirecto(MensajeDirectoRequest req, Long userId, String userName, String userRol) {
        RawMensajeDirectoRequest raw = RawMensajeDirectoRequest.builder()
                .destinatarioId(req.getDestinatarioId())
                .titulo(req.getTitulo())
                .contenido(req.getContenido())
                .remitenteId(userId)
                .remitenteNombre(userName)
                .remitenteRol(userRol)
                .build();
        return client.enviarDirecto(raw);
    }

    public MensajeDTO enviarComunicado(MensajeComunicadoRequest req, Long userId, String userName, String userRol) {
        RawMensajeComunicadoRequest raw = RawMensajeComunicadoRequest.builder()
                .cursoId(req.getCursoId())
                .titulo(req.getTitulo())
                .contenido(req.getContenido())
                .remitenteId(userId)
                .remitenteNombre(userName)
                .remitenteRol(userRol)
                .build();
        return client.enviarComunicado(raw);
    }

    public MensajeDTO marcarLeido(Long id) {
        return client.marcarLeido(id);
    }

    public void eliminar(Long id) {
        client.eliminar(id);
    }
}
