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
        MensajeResponse response = mensajeRepository.findMensajeDTOById(guardado.getId());

        if (response == null) {
            throw new ResourceNotFoundException("Mensaje no encontrado con id: " + guardado.getId());
        }

        return response;
    }

    public List<MensajeResponse> listarMensajes() {
        return mensajeRepository.findAllMensajesDTO();
    }

    public MensajeResponse obtenerPorId(Long id) {
        MensajeResponse response = mensajeRepository.findMensajeDTOById(id);
        if (response == null) {
            throw new ResourceNotFoundException("Mensaje no encontrado con id: " + id);
        }
        return response;
    }

    public List<MensajeResponse> obtenerPorDestinatario(Long destinatarioId) {
        return mensajeRepository.findMensajesDTOByDestinatarioId(destinatarioId);
    }

    public List<MensajeResponse> obtenerPorRemitente(Long remitenteId) {
        return mensajeRepository.findMensajesDTOByRemitenteId(remitenteId);
    }

    public MensajeResponse marcarComoLeido(Long id) {
        Mensaje mensaje = mensajeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mensaje no encontrado con id: " + id));

        mensaje.setEstado(EstadoMensaje.LEIDO);
        mensajeRepository.save(mensaje);

        MensajeResponse response = mensajeRepository.findMensajeDTOById(id);
        if (response == null) {
            throw new ResourceNotFoundException("Mensaje no encontrado con id: " + id);
        }

        return response;
    }

    public void eliminarMensaje(Long id) {
        if (!mensajeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Mensaje no encontrado con id: " + id);
        }
        mensajeRepository.deleteById(id);
    }
}