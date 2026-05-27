package com.microservicio.mensajeria.service;

import com.microservicio.mensajeria.dto.MensajeRequest;
import com.microservicio.mensajeria.dto.MensajeResponse;
import com.microservicio.mensajeria.exception.ResourceNotFoundException;
import com.microservicio.mensajeria.model.DestinatarioTipo;
import com.microservicio.mensajeria.model.EstadoMensaje;
import com.microservicio.mensajeria.model.Mensaje;
import com.microservicio.mensajeria.model.TipoMensaje;
import com.microservicio.mensajeria.repository.MensajeRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class MensajeriaCommandService {

    private final MensajeRepository mensajeRepository;
    private final MensajeMapper mapper;

    public MensajeriaCommandService(MensajeRepository mensajeRepository, MensajeMapper mapper) {
        this.mensajeRepository = mensajeRepository;
        this.mapper = mapper;
    }

    public MensajeResponse crearMensaje(MensajeRequest request, TipoMensaje tipo) {
        DestinatarioTipo destinatarioTipo = tipo == TipoMensaje.DIRECTO
                ? DestinatarioTipo.INDIVIDUAL
                : DestinatarioTipo.COMUNIDAD;

        Mensaje mensaje = Mensaje.builder()
                .titulo(request.getTitulo())
                .contenido(request.getContenido())
                .cursoId(request.getCursoId())
                .remitenteId(request.getRemitenteId())
                .remitenteNombre(request.getRemitenteNombre())
                .remitenteRol(request.getRemitenteRol())
                .destinatarioId(request.getDestinatarioId())
                .destinatarioNombre(request.getDestinatarioNombre())
                .destinatarioRol(request.getDestinatarioRol())
                .tipoMensaje(tipo)
                .destinatarioTipo(destinatarioTipo)
                .estado(EstadoMensaje.ENVIADO)
                .fechaEnvio(LocalDateTime.now())
                .build();

        return mapper.toResponse(mensajeRepository.save(mensaje));
    }

    public MensajeResponse marcarComoLeido(Long id) {
        Mensaje mensaje = mensajeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mensaje no encontrado con id: " + id));
        mensaje.setEstado(EstadoMensaje.LEIDO);
        return mapper.toResponse(mensajeRepository.save(mensaje));
    }

    public void eliminarMensaje(Long id) {
        if (!mensajeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Mensaje no encontrado con id: " + id);
        }
        mensajeRepository.deleteById(id);
    }
}
