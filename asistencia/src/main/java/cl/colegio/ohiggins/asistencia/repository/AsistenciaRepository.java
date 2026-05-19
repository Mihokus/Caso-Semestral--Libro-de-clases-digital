package cl.colegio.ohiggins.asistencia.repository;

import cl.colegio.ohiggins.asistencia.model.Asistencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AsistenciaRepository extends JpaRepository<Asistencia, Long> {
    List<Asistencia> findByAlumnoIdOrderByFechaDesc(Long alumnoId);
    List<Asistencia> findByCursoIdAndFecha(Long cursoId, LocalDate fecha);
}
