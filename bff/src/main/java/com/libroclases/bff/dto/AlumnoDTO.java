package com.libroclases.bff.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlumnoDTO {
    private Long id;
    private String nombre;
    private String rut;
    private Long cursoId;
    private String cursoNombre;
    private List<ApoderadoInfo> apoderados;
}
