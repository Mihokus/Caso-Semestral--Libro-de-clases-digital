package com.microservicio.mensajeria.service;

import com.microservicio.mensajeria.dto.MensajeResponse;
import com.microservicio.mensajeria.exception.ResourceNotFoundException;
import com.microservicio.mensajeria.repository.MensajeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MensajeriaQueryService {

    private final MensajeRepository mensajeRepository;

    public MensajeriaQueryService(MensajeRepository mensajeRepository) {
        this.mensajeRepository = mensajeRepository;
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

    public List<MensajeResponse> obtenerInbox(Long userId, Long cursoId) {
        return mensajeRepository.obtenerInboxUsuario(userId, cursoId);
    }
}