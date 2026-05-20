package cl.duoc.academic.config;

import cl.duoc.academic.model.Asignatura;
import cl.duoc.academic.model.Curso;
import cl.duoc.academic.repository.AsignaturaRepository;
import cl.duoc.academic.repository.CursoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner seedAcademic(CursoRepository cursoRepo, AsignaturaRepository asigRepo) {
        return args -> {
            if (cursoRepo.count() > 0) return;

            Curso primeroMedioA = new Curso();
            primeroMedioA.setNombre("1º Medio A");
            primeroMedioA.setNivel("Educación Media");
            cursoRepo.save(primeroMedioA);

            Curso segundoMedioB = new Curso();
            segundoMedioB.setNombre("2º Medio B");
            segundoMedioB.setNivel("Educación Media");
            cursoRepo.save(segundoMedioB);

            Asignatura matematicas = new Asignatura();
            matematicas.setNombreAsignatura("Matemáticas");
            matematicas.setDocenteId(1L);
            matematicas.setDocenteNombre("Docente Demo");
            matematicas.setCurso(primeroMedioA);
            asigRepo.save(matematicas);

            Asignatura lenguaje = new Asignatura();
            lenguaje.setNombreAsignatura("Lenguaje y Comunicación");
            lenguaje.setDocenteId(1L);
            lenguaje.setDocenteNombre("Docente Demo");
            lenguaje.setCurso(primeroMedioA);
            asigRepo.save(lenguaje);

            Asignatura historia = new Asignatura();
            historia.setNombreAsignatura("Historia y Ciencias Sociales");
            historia.setDocenteId(1L);
            historia.setDocenteNombre("Docente Demo");
            historia.setCurso(segundoMedioB);
            asigRepo.save(historia);
        };
    }
}
