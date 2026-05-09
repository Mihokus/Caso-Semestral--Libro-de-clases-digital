package cl.duoc.academic.controller;
import cl.duoc.academic.dto.EvaluacionDTO;
import cl.duoc.academic.facade.AcademicFacade;
import cl.duoc.academic.model.Evaluacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import cl.duoc.academic.model.Asignatura;
import cl.duoc.academic.dto.AsignaturaDTO;
import cl.duoc.academic.dto.RendimientoDTO;
import cl.duoc.academic.model.Curso;
import java.util.List;

@RestController
@RequestMapping("/api/academica")
public class AcademicController{

    @Autowired
    private AcademicFacade academicFacade;

    @PostMapping("/registrarNotas")
    public ResponseEntity<Evaluacion> registrarNota(@RequestBody EvaluacionDTO dto){
        return ResponseEntity.ok(academicFacade.registrarNota(dto));
    }

    @GetMapping("/rendimiento/{asignaturaId}")
    public ResponseEntity<RendimientoDTO> obtenerRendimiento(@PathVariable Long asignaturaId) {
        return ResponseEntity.ok(academicFacade.obtenerRendimientoTotal(asignaturaId));
    }

    @GetMapping("/notas/alumno/{id}")
    public ResponseEntity<List<Evaluacion>> listarNotasAlumno(@PathVariable Long id) {
        return ResponseEntity.ok(academicFacade.obtenerNotasAlumno(id));
    }

    @PostMapping("/asignaturas")
    public ResponseEntity<Asignatura> crearAsignatura(@RequestBody AsignaturaDTO dto) {
        Asignatura entidad = new Asignatura();
        entidad.setNombreAsignatura(dto.getNombre());
        entidad.setDocenteNombre(dto.getDocenteNombre());
        return ResponseEntity.ok(academicFacade.guardarAsignatura(entidad));
    }

    @GetMapping("/asignaturas")
    public ResponseEntity<List<Asignatura>> listarAsignaturas() {
        return ResponseEntity.ok(academicFacade.listarAsignaturas());
    }

    @PostMapping("/cursos")
    public ResponseEntity<Curso> crearCurso(@RequestBody Curso curso) {
        return ResponseEntity.ok(academicFacade.guardarCurso(curso));
    }

    @GetMapping("/cursos")
    public ResponseEntity<List<Curso>> listarCursos() {
        return ResponseEntity.ok(academicFacade.listarCursos());
    }

    @GetMapping("/cursos/{id}")
    public ResponseEntity<Curso> obtenerCursoPorId(@PathVariable Long id) {
        return ResponseEntity.ok(academicFacade.obtenerCursoPorId(id));
    }

}