package cl.colegio.ohiggins.asistencia.dto;

import lombok.Data;

@Data
public class AlumnoRequest {

    private String nombre;
    private String rut;
    private Long cursoId;
    private String cursoNombre;
}