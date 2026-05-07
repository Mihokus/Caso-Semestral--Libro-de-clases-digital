package com.microservicio.mensajeria.dto;

import com.microservicio.mensajeria.model.DestinatarioTipo;
import com.microservicio.mensajeria.model.EstadoMensaje;
import com.microservicio.mensajeria.model.TipoMensaje;

import java.time.LocalDateTime;

public class MensajeResponse {

    private Long id;
    private String titulo;
    private String contenido;
    private String remitenteNombre;
    private String destinatarioNombre;
    private TipoMensaje tipoMensaje;
    private DestinatarioTipo destinatarioTipo;
    private EstadoMensaje estado;
    private LocalDateTime fechaEnvio;

    public MensajeResponse(Long id, String titulo, String contenido,
                           String remitenteNombre, String destinatarioNombre,
                           TipoMensaje tipoMensaje, DestinatarioTipo destinatarioTipo,
                           EstadoMensaje estado, LocalDateTime fechaEnvio) {
        this.id = id;
        this.titulo = titulo;
        this.contenido = contenido;
        this.remitenteNombre = remitenteNombre;
        this.destinatarioNombre = destinatarioNombre;
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

    public String getRemitenteNombre() {
        return remitenteNombre;
    }

    public String getDestinatarioNombre() {
        return destinatarioNombre;
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