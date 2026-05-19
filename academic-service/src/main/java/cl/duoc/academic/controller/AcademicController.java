package cl.duoc.academic.controller;

import cl.duoc.academic.dto.AsignaturaDTO;
import cl.duoc.academic.dto.AsignaturaRequest;
import cl.duoc.academic.dto.CursoDTO;
import cl.duoc.academic.dto.CursoRequest;
import cl.duoc.academic.dto.EvaluacionDTO;
import cl.duoc.academic.dto.EvaluacionResponse;
import cl.duoc.academic.dto.RendimientoDTO;
import cl.duoc.academic.facade.AcademicFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/academica")
public class AcademicController {

    @Autowired
    private AcademicFacade academicFacade;

    @PostMapping("/notas")
    public ResponseEntity<EvaluacionResponse> registrarNota(@RequestBody EvaluacionDTO dto) {
        return ResponseEntity.ok(academicFacade.registrarNota(dto));
    }

    @GetMapping("/notas/alumno/{id}")
    public ResponseEntity<List<EvaluacionResponse>> listarNotasAlumno(@PathVariable Long id) {
        return ResponseEntity.ok(academicFacade.obtenerNotasAlumno(id));
    }

    @GetMapping("/rendimiento/{asignaturaId}")
    public ResponseEntity<RendimientoDTO> obtenerRendimiento(@PathVariable Long asignaturaId) {
        return ResponseEntity.ok(academicFacade.obtenerRendimientoTotal(asignaturaId));
    }

    @PostMapping("/asignaturas")
    public ResponseEntity<AsignaturaDTO> crearAsignatura(@RequestBody AsignaturaRequest req) {
        return ResponseEntity.ok(academicFacade.guardarAsignatura(req));
    }

    @GetMapping("/asignaturas")
    public ResponseEntity<List<AsignaturaDTO>> listarAsignaturas() {
        return ResponseEntity.ok(academicFacade.listarAsignaturas());
    }

    @GetMapping("/asignaturas/{id}")
    public ResponseEntity<AsignaturaDTO> obtenerAsignaturaPorId(@PathVariable Long id) {
        AsignaturaDTO dto = academicFacade.obtenerAsignaturaPorId(id);
        return dto == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(dto);
    }

    @PostMapping("/cursos")
    public ResponseEntity<CursoDTO> crearCurso(@RequestBody CursoRequest req) {
        return ResponseEntity.ok(academicFacade.guardarCurso(req));
    }

    @GetMapping("/cursos")
    public ResponseEntity<List<CursoDTO>> listarCursos() {
        return ResponseEntity.ok(academicFacade.listarCursos());
    }

    @GetMapping("/cursos/{id}")
    public ResponseEntity<CursoDTO> obtenerCursoPorId(@PathVariable Long id) {
        CursoDTO dto = academicFacade.obtenerCursoPorId(id);
        return dto == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(dto);
    }
}
