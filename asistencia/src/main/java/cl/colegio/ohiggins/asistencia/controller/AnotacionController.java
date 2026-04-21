package cl.colegio.ohiggins.asistencia.controller;

import cl.colegio.ohiggins.asistencia.facade.AsistenciaFacade;
import cl.colegio.ohiggins.asistencia.model.Anotacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/anotaciones")
public class AnotacionController {

    @Autowired
    private AsistenciaFacade facade;

    @PostMapping("/registrar")
    public ResponseEntity<String> registrarAnotacion(@RequestBody Anotacion anotacion) {
        // La fachada se encarga de llamar al servicio de anotaciones
        facade.registrarEventoDiario(null, anotacion);
        return ResponseEntity.ok("Anotación registrada correctamente");
    }
}