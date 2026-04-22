package com.microservicio.mensajeria.service;

import com.microservicio.mensajeria.dto.MensajeRequest;
import com.microservicio.mensajeria.dto.MensajeResponse;
import com.microservicio.mensajeria.exception.ResourceNotFoundException;
import com.microservicio.mensajeria.model.EstadoMensaje;
import com.microservicio.mensajeria.model.Mensaje;
import com.microservicio.mensajeria.repository.MensajeRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MensajeService {

    private final MensajeRepository mensajeRepository;

    public MensajeService(MensajeRepository mensajeRepository) {
        this.mensajeRepository = mensajeRepository;
    }

    public MensajeResponse crearMensaje(MensajeRequest request) {
        Mensaje mensaje = Mensaje.builder()
                .titulo(request.getTitulo())
                .contenido(request.getContenido())
                .remitenteId(request.getRemitenteId())
                .remitenteNombre(request.getRemitenteNombre())
                .destinatarioId(request.getDestinatarioId())
                .destinatarioNombre(request.getDestinatarioNombre())
                .tipoMensaje(request.getTipoMensaje())
                .destinatarioTipo(request.getDestinatarioTipo())
                .estado(EstadoMensaje.ENVIADO)
                .fechaEnvio(LocalDateTime.now())
                .build();

        Mensaje guardado = mensajeRepository.save(mensaje);
        return mapToResponse(guardado);
    }

    public List<MensajeResponse> listarMensajes() {
        return mensajeRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public MensajeResponse obtenerPorId(Long id) {
        Mensaje mensaje = mensajeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mensaje no encontrado con id: " + id));
        return mapToResponse(mensaje);
    }

    public List<MensajeResponse> obtenerPorDestinatario(Long destinatarioId) {
        return mensajeRepository.findByDestinatarioId(destinatarioId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<MensajeResponse> obtenerPorRemitente(Long remitenteId) {
        return mensajeRepository.findByRemitenteId(remitenteId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public MensajeResponse marcarComoLeido(Long id) {
        Mensaje mensaje = mensajeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mensaje no encontrado con id: " + id));

        mensaje.setEstado(EstadoMensaje.LEIDO);
        Mensaje actualizado = mensajeRepository.save(mensaje);

        return mapToResponse(actualizado);
    }

    public void eliminarMensaje(Long id) {
        if (!mensajeRepository.existsById(id)) {
    throw new ResourceNotFoundException("Mensaje no encontrado con id: " + id);
}
        mensajeRepository.deleteById(id);
    }

    private MensajeResponse mapToResponse(Mensaje mensaje) {
        return MensajeResponse.builder()
                .id(mensaje.getId())
                .titulo(mensaje.getTitulo())
                .contenido(mensaje.getContenido())
                .remitenteNombre(mensaje.getRemitenteNombre())
                .destinatarioNombre(mensaje.getDestinatarioNombre())
                .tipoMensaje(mensaje.getTipoMensaje())
                .destinatarioTipo(mensaje.getDestinatarioTipo())
                .estado(mensaje.getEstado())
                .fechaEnvio(mensaje.getFechaEnvio())
                .build();
    }
}