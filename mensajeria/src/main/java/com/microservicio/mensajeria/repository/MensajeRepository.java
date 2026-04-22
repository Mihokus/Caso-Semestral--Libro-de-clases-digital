package com.microservicio.mensajeria.repository;

import com.microservicio.mensajeria.model.Mensaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MensajeRepository extends JpaRepository<Mensaje, Long> {

    List<Mensaje> findByDestinatarioId(Long destinatarioId);

    List<Mensaje> findByRemitenteId(Long remitenteId);

}