package cl.colegio.ohiggins.asistencia.repository;

import cl.colegio.ohiggins.asistencia.model.Anotacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AnotacionRepository extends JpaRepository<Anotacion, Long> {

    // Método para filtrar anotaciones por estudiante
    List<Anotacion> findByRutEstudiante(String rutEstudiante);
}