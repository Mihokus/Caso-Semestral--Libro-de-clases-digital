package cl.duoc.academic.controller;
import cl.duoc.academic.dto.EvaluacionDTO;
import cl.duoc.academic.facade.AcademicFacade;
import cl.duoc.academic.model.Evaluacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/academica")
public class AcademicController{

    @Autowired
    private AcademicFacade academicFacade;

    @PostMapping("/registrarNotas")
    public ResponseEntity<Evaluacion> registrarNota(@RequestBody EvaluacionDTO dto){
        Evaluacion nuevaNota = academicFacade.registrarNota(
            dto.getAsignaturaId(),
            dto.getNombre(),
            dto.getNota()
        );
        return ResponseEntity.ok(nuevaNota);
    }

    @GetMapping("/rendimiento/{asignaturaId}")
    public ResponseEntity<Double> obtenerRendimiento(@PathVariable Long asignaturaId) {
        Double promedio = academicFacade.obtenerPromedioRendimiento(asignaturaId);
        return ResponseEntity.ok(promedio);
    }
}