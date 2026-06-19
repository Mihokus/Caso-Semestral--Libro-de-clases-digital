package cl.colegio.ohiggins.asistencia.repository;

import cl.colegio.ohiggins.asistencia.model.Anotacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnotacionRepository extends JpaRepository<Anotacion, Long> {

    List<Anotacion> findByAlumnoIdOrderByFechaDesc(Long alumnoId);
}