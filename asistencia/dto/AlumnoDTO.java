package cl.colegio.ohiggins.asistencia.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlumnoDTO {
    private Long id;
    private String nombre;
    private String rut;
    private Long cursoId;
    private String cursoNombre;
    private List<ApoderadoInfo> apoderados;
}
