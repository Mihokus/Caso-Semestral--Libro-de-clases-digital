package cl.colegio.ohiggins.asistencia.controller;

import cl.colegio.ohiggins.asistencia.dto.AlumnoDTO;
import cl.colegio.ohiggins.asistencia.dto.ApoderadoInfo;
import cl.colegio.ohiggins.asistencia.facade.AsistenciaFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AlumnoController {

    @Autowired private AsistenciaFacade facade;

    @GetMapping("/alumnos")
    public List<AlumnoDTO> listarAlumnos() {
        return facade.listarAlumnos();
    }

    @GetMapping("/alumnos/{id}")
    public ResponseEntity<AlumnoDTO> obtenerAlumno(@PathVariable Long id) {
        AlumnoDTO dto = facade.obtenerAlumno(id);
        return dto == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(dto);
    }

    @GetMapping("/alumnos/{id}/apoderados")
    public List<ApoderadoInfo> apoderadosDe(@PathVariable Long id) {
        return facade.apoderadosDe(id);
    }

    @GetMapping("/cursos/{cursoId}/alumnos")
    public List<AlumnoDTO> alumnosDeCurso(@PathVariable Long cursoId) {
        return facade.alumnosDeCurso(cursoId);
    }
}
