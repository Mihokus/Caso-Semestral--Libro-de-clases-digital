package cl.colegio.ohiggins.asistencia.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlumnoRequest {
    private String nombre;
    private String rut;
    private Long cursoId;
    private String cursoNombre;
}
