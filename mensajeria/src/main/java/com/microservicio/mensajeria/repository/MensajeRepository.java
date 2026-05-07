package com.microservicio.mensajeria.repository;

import com.microservicio.mensajeria.dto.MensajeResponse;
import com.microservicio.mensajeria.model.Mensaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MensajeRepository extends JpaRepository<Mensaje, Long> {

    @Query("""
        SELECT new com.microservicio.mensajeria.dto.MensajeResponse(
            m.id,
            m.titulo,
            m.contenido,
            m.cursoId,
            m.remitenteId,
            m.remitenteNombre,
            m.remitenteRol,
            m.destinatarioId,
            m.destinatarioNombre,
            m.destinatarioRol,
            m.tipoMensaje,
            m.destinatarioTipo,
            m.estado,
            m.fechaEnvio
        )
        FROM Mensaje m
        WHERE m.id = :id
    """)
    MensajeResponse findMensajeDTOById(Long id);

    @Query("""
        SELECT new com.microservicio.mensajeria.dto.MensajeResponse(
            m.id,
            m.titulo,
            m.contenido,
            m.cursoId,
            m.remitenteId,
            m.remitenteNombre,
            m.remitenteRol,
            m.destinatarioId,
            m.destinatarioNombre,
            m.destinatarioRol,
            m.tipoMensaje,
            m.destinatarioTipo,
            m.estado,
            m.fechaEnvio
        )
        FROM Mensaje m
        WHERE m.destinatarioId = :destinatarioId
    """)
    List<MensajeResponse> findMensajesDTOByDestinatarioId(Long destinatarioId);

    @Query("""
        SELECT new com.microservicio.mensajeria.dto.MensajeResponse(
            m.id,
            m.titulo,
            m.contenido,
            m.cursoId,
            m.remitenteId,
            m.remitenteNombre,
            m.remitenteRol,
            m.destinatarioId,
            m.destinatarioNombre,
            m.destinatarioRol,
            m.tipoMensaje,
            m.destinatarioTipo,
            m.estado,
            m.fechaEnvio
        )
        FROM Mensaje m
        WHERE m.remitenteId = :remitenteId
    """)
    List<MensajeResponse> findMensajesDTOByRemitenteId(Long remitenteId);
}