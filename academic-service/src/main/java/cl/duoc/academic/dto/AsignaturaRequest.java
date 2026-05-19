package cl.duoc.academic.dto;

import lombok.Data;

@Data
public class AsignaturaRequest {
    private String nombre;
    private Long cursoId;
    private Long docenteId;
    private String docenteNombre;
}
