package com.microservicio.mensajeria.service;

import com.microservicio.mensajeria.dto.MensajeRequest;
import com.microservicio.mensajeria.dto.MensajeResponse;
import com.microservicio.mensajeria.exception.ResourceNotFoundException;
import com.microservicio.mensajeria.model.EstadoMensaje;
import com.microservicio.mensajeria.model.Mensaje;
import com.microservicio.mensajeria.model.TipoMensaje;
import com.microservicio.mensajeria.repository.MensajeRepository;
import com.microservicio.mensajeria.strategy.MensajeStrategy;
import com.microservicio.mensajeria.strategy.MensajeStrategyFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class MensajeriaCommandService {

    private final MensajeRepository mensajeRepository;
    private final MensajeMapper mapper;
    private final MensajeStrategyFactory strategyFactory;

    public MensajeriaCommandService(MensajeRepository mensajeRepository,
                                    MensajeMapper mapper,
                                    MensajeStrategyFactory strategyFactory) {
        this.mensajeRepository = mensajeRepository;
        this.mapper = mapper;
        this.strategyFactory = strategyFactory;
    }

    public MensajeResponse crearMensaje(MensajeRequest request, TipoMensaje tipoMensaje) {
        validarDatosBase(request, tipoMensaje);

        MensajeStrategy strategy = strategyFactory.obtenerStrategy(tipoMensaje);
        strategy.validar(request);
        strategy.preparar(request);

        Mensaje mensaje = Mensaje.builder()
                .tipoMensaje(tipoMensaje)
                .titulo(request.getTitulo())
                .contenido(request.getContenido())
                .remitenteId(request.getRemitenteId())
                .remitenteNombre(request.getRemitenteNombre())
                .remitenteRol(request.getRemitenteRol())
                .destinatarioId(request.getDestinatarioId())
                .destinatarioNombre(request.getDestinatarioNombre())
                .destinatarioRol(request.getDestinatarioRol())
                .cursoId(request.getCursoId())
                .fechaEnvio(LocalDateTime.now())
                .estado(EstadoMensaje.ENVIADO)
                .build();

        Mensaje guardado = mensajeRepository.save(mensaje);
        return mapper.toResponse(guardado);
    }

    public MensajeResponse marcarComoLeido(Long id) {
        Mensaje mensaje = mensajeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mensaje no encontrado con id: " + id));

        mensaje.setEstado(EstadoMensaje.LEIDO);
        Mensaje guardado = mensajeRepository.save(mensaje);

        return mapper.toResponse(guardado);
    }

    public void eliminarMensaje(Long id) {
        if (!mensajeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Mensaje no encontrado con id: " + id);
        }

        mensajeRepository.deleteById(id);
    }

    private void validarDatosBase(MensajeRequest request, TipoMensaje tipoMensaje) {
        if (request == null) {
            throw new IllegalArgumentException("El mensaje no puede ser nulo");
        }

        if (tipoMensaje == null) {
            throw new IllegalArgumentException("El tipo de mensaje es obligatorio");
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

        if (request.getRemitenteRol() == null || request.getRemitenteRol().isBlank()) {
            throw new IllegalArgumentException("El remitenteRol es obligatorio");
        }
    }
}