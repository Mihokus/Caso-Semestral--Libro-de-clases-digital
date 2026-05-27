package com.microservicio.mensajeria.repository;

import com.microservicio.mensajeria.model.Mensaje;
import com.microservicio.mensajeria.model.TipoMensaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MensajeRepository extends JpaRepository<Mensaje, Long> {

    List<Mensaje> findByDestinatarioIdOrderByFechaEnvioDesc(Long destinatarioId);

    List<Mensaje> findByRemitenteIdOrderByFechaEnvioDesc(Long remitenteId);

    List<Mensaje> findByDestinatarioIdOrTipoMensajeOrderByFechaEnvioDesc(Long destinatarioId, TipoMensaje tipoMensaje);
}
