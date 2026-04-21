package cl.colegio.ohiggins.asistencia.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnotacionDTO {
    private String rutEstudiante;
    private String tipo; // "POSITIVA" o "NEGATIVA"
    private String descripcion;
    private LocalDate fecha;
}