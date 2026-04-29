package cl.duoc.academic.repository;
import cl.duoc.academic.model.Evaluacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

public interface EvaluacionRepository  extends JpaRepository<Evaluacion, Long>{
    List<Evaluacion> findByAsignaturaId (Long asignaturaId);
}
