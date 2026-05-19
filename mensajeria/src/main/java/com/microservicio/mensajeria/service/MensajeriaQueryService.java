package com.microservicio.mensajeria.service;

import com.microservicio.mensajeria.dto.MensajeResponse;
import com.microservicio.mensajeria.exception.ResourceNotFoundException;
import com.microservicio.mensajeria.model.Mensaje;
import com.microservicio.mensajeria.model.TipoMensaje;
import com.microservicio.mensajeria.repository.MensajeRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MensajeriaQueryService {

    private final MensajeRepository mensajeRepository;
    private final MensajeMapper mapper;

    public MensajeriaQueryService(MensajeRepository mensajeRepository, MensajeMapper mapper) {
        this.mensajeRepository = mensajeRepository;
        this.mapper = mapper;
    }

    public MensajeResponse obtenerPorId(Long id) {
        Mensaje m = mensajeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mensaje no encontrado con id: " + id));
        return mapper.toResponse(m);
    }

    public List<MensajeResponse> obtenerPorRemitente(Long remitenteId) {
        return map(mensajeRepository.findByRemitenteIdOrderByFechaEnvioDesc(remitenteId));
    }

    public List<MensajeResponse> obtenerInbox(Long userId) {
        return map(mensajeRepository
                .findByDestinatarioIdOrTipoMensajeOrderByFechaEnvioDesc(userId, TipoMensaje.COMUNICADO));
    }

    private List<MensajeResponse> map(List<Mensaje> list) {
        List<MensajeResponse> out = new ArrayList<>(list.size());
        for (Mensaje m : list) out.add(mapper.toResponse(m));
        return out;
    }
}
