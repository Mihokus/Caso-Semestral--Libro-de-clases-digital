package cl.colegio.ohiggins.asistencia.repository;

import cl.colegio.ohiggins.asistencia.entity.Alumno;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AlumnoRepository extends JpaRepository<Alumno, Long> {
    Optional<Alumno> findByRut(String rut);
}