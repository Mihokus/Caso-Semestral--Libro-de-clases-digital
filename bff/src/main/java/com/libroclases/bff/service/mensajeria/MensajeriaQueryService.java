package com.libroclases.bff.service.mensajeria;

import com.libroclases.bff.client.MensajeriaClient;
import com.libroclases.bff.dto.MensajeDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MensajeriaQueryService {

    private final MensajeriaClient client;

    public List<MensajeDTO> inbox(Long userId) {
        return client.inbox(userId);
    }

    public List<MensajeDTO> enviados(Long userId) {
        return client.enviados(userId);
    }

    public MensajeDTO byId(Long id) {
        return client.byId(id);
    }
}
