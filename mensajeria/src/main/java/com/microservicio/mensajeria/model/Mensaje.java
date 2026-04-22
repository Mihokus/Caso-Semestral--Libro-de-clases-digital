package com.microservicio.mensajeria.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "mensajes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Mensaje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;

    @Column(length = 1000)
    private String contenido;

    private Long remitenteId;
    private String remitenteNombre;

    private Long destinatarioId; // puede ser null si es comunicado general
    private String destinatarioNombre;

    @Enumerated(EnumType.STRING)
    private TipoMensaje tipoMensaje;

    @Enumerated(EnumType.STRING)
    private DestinatarioTipo destinatarioTipo;

    @Enumerated(EnumType.STRING)
    private EstadoMensaje estado;

    private LocalDateTime fechaEnvio;
}