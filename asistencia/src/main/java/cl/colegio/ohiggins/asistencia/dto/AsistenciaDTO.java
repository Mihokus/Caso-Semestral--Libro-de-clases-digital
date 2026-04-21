package cl.colegio.ohiggins.asistencia.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AsistenciaDTO {
    private String rutEstudiante;
    private LocalDate fecha;
    private String estado; // Ejemplo: "PRESENTE", "AUSENTE"
}