package cl.duoc.academic.controller;
import cl.duoc.academic.dto.EvaluacionDTO;
import cl.duoc.academic.facade.AcademicFacade;
import cl.duoc.academic.model.Evaluacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import cl.duoc.academic.model.Asignatura;
import cl.duoc.academic.dto.AsignaturaDTO;
import java.util.List;

@RestController
@RequestMapping("/api/academica")
public class AcademicController{

    @Autowired
    private AcademicFacade academicFacade;

    @PostMapping("/registrarNotas")
    public ResponseEntity<Evaluacion> registrarNota(@RequestBody EvaluacionDTO dto){
        Evaluacion nuevaNota = academicFacade.registrarNota(
            dto.getAsignaturaId(),
            dto.getAlumnoId(),
            dto.getNombre(),
            dto.getNota(),
            dto.getPonderacion()
        );
        return ResponseEntity.ok(nuevaNota);
    }

    @GetMapping("/rendimiento/{asignaturaId}")
    public ResponseEntity<Double> obtenerRendimiento(@PathVariable Long asignaturaId) {
        Double promedio = academicFacade.obtenerPromedioRendimiento(asignaturaId);
        return ResponseEntity.ok(promedio);
    }

    @PostMapping("/asignaturas")
    public ResponseEntity<Asignatura> crearAsignatura(@RequestBody AsignaturaDTO dto) {
    Asignatura entidad = new Asignatura();
    entidad.setNombreAsignatura(dto.getNombre());
    entidad.setCurso(dto.getCurso());
    Asignatura guardada = academicFacade.guardarAsignatura(entidad);
    return ResponseEntity.ok(guardada);
    }

    @GetMapping("/asignaturas")
    public ResponseEntity<List<Asignatura>  > listarAsignaturas() {
    return ResponseEntity.ok(academicFacade.listarAsignaturas());
    }
}