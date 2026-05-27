package com.libroclases.bff.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RawAsignaturaRequest {
    private String nombre;
    private Long cursoId;
    private Long docenteId;
    private String docenteNombre;
}
