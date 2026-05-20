package com.microservicio.mensajeria.dto;

import com.microservicio.mensajeria.model.DestinatarioTipo;
import com.microservicio.mensajeria.model.EstadoMensaje;
import com.microservicio.mensajeria.model.TipoMensaje;

import java.time.LocalDateTime;

public class MensajeResponse {

    private Long id;
    private String titulo;
    private String contenido;
    private Long cursoId;

    private UsuarioResumenDTO remitente;
    private UsuarioResumenDTO destinatario;

    private TipoMensaje tipoMensaje;
    private DestinatarioTipo destinatarioTipo;
    private EstadoMensaje estado;
    private LocalDateTime fechaEnvio;

    public MensajeResponse(Long id,
                           String titulo,
                           String contenido,
                           Long cursoId,
                           Long remitenteId,
                           String remitenteNombre,
                           String remitenteRol,
                           Long destinatarioId,
                           String destinatarioNombre,
                           String destinatarioRol,
                           TipoMensaje tipoMensaje,
                           DestinatarioTipo destinatarioTipo,
                           EstadoMensaje estado,
                           LocalDateTime fechaEnvio) {

        this.id = id;
        this.titulo = titulo;
        this.contenido = contenido;
        this.cursoId = cursoId;

        this.remitente = new UsuarioResumenDTO(
                remitenteId,
                remitenteNombre,
                remitenteRol
        );

        this.destinatario = new UsuarioResumenDTO(
                destinatarioId,
                destinatarioNombre,
                destinatarioRol
        );

        this.tipoMensaje = tipoMensaje;
        this.destinatarioTipo = destinatarioTipo;
        this.estado = estado;
        this.fechaEnvio = fechaEnvio;
    }

    public Long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getContenido() {
        return contenido;
    }

    public Long getCursoId() {
        return cursoId;
    }

    public UsuarioResumenDTO getRemitente() {
        return remitente;
    }

    public UsuarioResumenDTO getDestinatario() {
        return destinatario;
    }

    public TipoMensaje getTipoMensaje() {
        return tipoMensaje;
    }

    public DestinatarioTipo getDestinatarioTipo() {
        return destinatarioTipo;
    }

    public EstadoMensaje getEstado() {
        return estado;
    }

    public LocalDateTime getFechaEnvio() {
        return fechaEnvio;
    }
}