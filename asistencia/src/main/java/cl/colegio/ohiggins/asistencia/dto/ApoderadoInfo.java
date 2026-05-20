package cl.colegio.ohiggins.asistencia.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApoderadoInfo {
    private Long id;
    private String nombre;
    private String email;
}
