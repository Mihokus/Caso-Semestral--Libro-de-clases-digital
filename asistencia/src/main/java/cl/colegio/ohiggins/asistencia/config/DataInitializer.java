package cl.colegio.ohiggins.asistencia.config;

import cl.colegio.ohiggins.asistencia.entity.Alumno;
import cl.colegio.ohiggins.asistencia.entity.Apoderado;
import cl.colegio.ohiggins.asistencia.repository.AlumnoRepository;
import cl.colegio.ohiggins.asistencia.repository.ApoderadoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashSet;
import java.util.Set;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner seedData(
            AlumnoRepository alumnoRepo,
            ApoderadoRepository apoderadoRepo
    ) {
        return args -> {

            // Evitar duplicados
            if (alumnoRepo.count() > 0) return;

            // =========================
            // APODERADOS
            // =========================

            Apoderado apoderado1 = apoderadoRepo.findByEmail("apoderado@colegio.cl")
                    .orElseGet(() -> {
                        Apoderado a = new Apoderado();
                        a.setNombre("Apoderado Demo");
                        a.setEmail("apoderado@colegio.cl");
                        return apoderadoRepo.save(a);
                    });

            Apoderado apoderado2 = apoderadoRepo.findByEmail("maria.soto@colegio.cl")
                    .orElseGet(() -> {
                        Apoderado a = new Apoderado();
                        a.setNombre("María Soto");
                        a.setEmail("maria.soto@colegio.cl");
                        return apoderadoRepo.save(a);
                    });

            // =========================
            // ALUMNOS
            // =========================

            Set<Apoderado> grupo1 = new HashSet<>();
            grupo1.add(apoderado1);

            Alumno alumno1 = new Alumno();
            alumno1.setNombre("Estudiante Demo");
            alumno1.setRut("20.000.001-K");
            alumno1.setCursoId(1L);
            alumno1.setCursoNombre("1º Medio A");
            alumno1.setApoderados(grupo1);
            alumnoRepo.save(alumno1);

            Set<Apoderado> grupo2 = new HashSet<>();
            grupo2.add(apoderado2);

            Alumno alumno2 = new Alumno();
            alumno2.setNombre("Carla González");
            alumno2.setRut("20.000.002-2");
            alumno2.setCursoId(1L);
            alumno2.setCursoNombre("1º Medio A");
            alumno2.setApoderados(grupo2);
            alumnoRepo.save(alumno2);

            Alumno alumno3 = new Alumno();
            alumno3.setNombre("Diego Pérez");
            alumno3.setRut("20.000.003-4");
            alumno3.setCursoId(1L);
            alumno3.setCursoNombre("1º Medio A");
            alumno3.setApoderados(grupo1);
            alumnoRepo.save(alumno3);

            Alumno alumno4 = new Alumno();
            alumno4.setNombre("Sofía Rojas");
            alumno4.setRut("20.000.004-6");
            alumno4.setCursoId(2L);
            alumno4.setCursoNombre("2º Medio B");
            alumnoRepo.save(alumno4);
        };
    }
}