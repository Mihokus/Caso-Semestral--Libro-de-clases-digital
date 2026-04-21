package cl.colegio.ohiggins.asistencia.controller;

import cl.colegio.ohiggins.asistencia.facade.AsistenciaFacade;
import cl.colegio.ohiggins.asistencia.model.Asistencia;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/asistencia")
public class AsistenciaController {

    @Autowired
    private AsistenciaFacade facade;

    @PostMapping("/registrar")
    public ResponseEntity<Asistencia> registrar(@RequestBody Asistencia asistencia) {
        return ResponseEntity.ok(facade.ejecutarRegistro(asistencia));
    }

    @GetMapping("/historial")
    public ResponseEntity<List<Asistencia>> listar() {
        return ResponseEntity.ok(facade.historial());
    }
}