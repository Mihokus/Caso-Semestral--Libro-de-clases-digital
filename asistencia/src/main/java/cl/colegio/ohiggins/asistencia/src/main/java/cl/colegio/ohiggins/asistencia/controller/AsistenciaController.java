package cl.colegio.ohiggins.asistencia.controller;

import cl.colegio.ohiggins.asistencia.facade.AsistenciaFacade;
import cl.colegio.ohiggins.asistencia.model.Asistencia;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/asistencia")
public class AsistenciaController {

    @Autowired
    private AsistenciaFacade facade;

    // Registrar una nueva asistencia
    @PostMapping("/registrar")
    public ResponseEntity<Asistencia> registrar(@RequestBody Asistencia asistencia) {
        try {
            Asistencia nuevaAsistencia = facade.ejecutarRegistro(asistencia);
            // Usamos .status().body() para evitar el error de ambigüedad
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaAsistencia);
        } catch (Exception e) {
            // Si hay error, enviamos el estado 500 de forma clara
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Listar todo el historial
    @GetMapping("/historial")
    public ResponseEntity<List<Asistencia>> listar() {
        List<Asistencia> historial = facade.historial();
        if (historial.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(historial);
    }
}