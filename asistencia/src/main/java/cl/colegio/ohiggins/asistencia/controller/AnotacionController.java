package cl.colegio.ohiggins.asistencia.controller;

import cl.colegio.ohiggins.asistencia.facade.AsistenciaFacade;
import cl.colegio.ohiggins.asistencia.model.Anotacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/anotaciones")
public class AnotacionController {

    @Autowired
    private AsistenciaFacade facade;

    @PostMapping("/registrar")
    public ResponseEntity<Anotacion> registrarAnotacion(@RequestBody Anotacion anotacion) {
        try {
            // Quitamos el 'null', ahora solo pasamos la anotación
            Anotacion nuevaAnotacion = facade.registrarEventoDiario(anotacion);

            // Devolvemos el objeto creado con un estado 201 (Created)
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaAnotacion);
        } catch (Exception e) {
            // Si algo falla, devolvemos un error 500
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}