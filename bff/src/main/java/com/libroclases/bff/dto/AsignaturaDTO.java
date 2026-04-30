package com.libroclases.bff.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AsignaturaDTO {
    private Long id;
    private String nombre;
    private Long cursoId;
    private String cursoNombre;
    private Long docenteId;
    private String docenteNombre;
}
