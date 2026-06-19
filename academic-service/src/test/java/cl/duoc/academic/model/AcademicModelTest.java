package cl.duoc.academic.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class AcademicModelTest {
    @Test
    void curso_gettersYSetters() {
        Curso curso = new Curso();
        curso.setId(1L);
        curso.setNombre("1º Medio A");
        curso.setNivel("Educación Media");

        assertEquals(1L, curso.getId());
        assertEquals("1º Medio A", curso.getNombre());
        assertEquals("Educación Media", curso.getNivel());
    }

    @Test
    void curso_equalsHashCodeToString() {
        Curso c1 = new Curso();
        c1.setId(1L);
        c1.setNombre("1º Medio A");

        Curso c2 = new Curso();
        c2.setId(1L);
        c2.setNombre("1º Medio A");

        assertEquals(c1, c2);
        assertEquals(c1.hashCode(), c2.hashCode());
        assertNotNull(c1.toString());
    }

    @Test
    void asignatura_gettersYSetters() {
        Curso curso = new Curso();
        curso.setId(10L);
        curso.setNombre("2º Medio B");

        Asignatura asignatura = new Asignatura();
        asignatura.setId(5L);
        asignatura.setNombreAsignatura("Matemáticas");
        asignatura.setDocenteId(99L);
        asignatura.setDocenteNombre("Prof. García");
        asignatura.setCurso(curso);

        assertEquals(5L, asignatura.getId());
        assertEquals("Matemáticas", asignatura.getNombreAsignatura());
        assertEquals(99L, asignatura.getDocenteId());
        assertEquals("Prof. García", asignatura.getDocenteNombre());
        assertEquals(curso, asignatura.getCurso());
    }

    @Test
    void asignatura_equalsHashCodeToString() {
        Asignatura a1 = new Asignatura();
        a1.setId(1L);
        a1.setNombreAsignatura("Historia");

        Asignatura a2 = new Asignatura();
        a2.setId(1L);
        a2.setNombreAsignatura("Historia");

        assertEquals(a1, a2);
        assertEquals(a1.hashCode(), a2.hashCode());
        assertNotNull(a1.toString());
    }


    @Test
    void evaluacion_gettersYSetters() {
        Asignatura asignatura = new Asignatura();
        asignatura.setId(2L);
        asignatura.setNombreAsignatura("Lenguaje");

        Evaluacion evaluacion = new Evaluacion();
        evaluacion.setId(100L);
        evaluacion.setAlumnoId(7L);
        evaluacion.setAlumnoNombre("Juan Pérez");
        evaluacion.setNombre("Prueba 1");
        evaluacion.setNota(6.5);
        evaluacion.setPonderacion(0.3);
        evaluacion.setAsignaturaNombre("Lenguaje");
        evaluacion.setFecha(LocalDate.of(2025, 6, 1));
        evaluacion.setRegistradoPorId(1L);
        evaluacion.setRegistradoPorNombre("Admin");
        evaluacion.setAsignatura(asignatura);

        assertEquals(100L, evaluacion.getId());
        assertEquals(7L, evaluacion.getAlumnoId());
        assertEquals("Juan Pérez", evaluacion.getAlumnoNombre());
        assertEquals("Prueba 1", evaluacion.getNombre());
        assertEquals(6.5, evaluacion.getNota());
        assertEquals(0.3, evaluacion.getPonderacion());
        assertEquals("Lenguaje", evaluacion.getAsignaturaNombre());
        assertEquals(LocalDate.of(2025, 6, 1), evaluacion.getFecha());
        assertEquals(1L, evaluacion.getRegistradoPorId());
        assertEquals("Admin", evaluacion.getRegistradoPorNombre());
        assertEquals(asignatura, evaluacion.getAsignatura());
    }

    @Test
    void evaluacion_equalsHashCodeToString() {
        Evaluacion e1 = new Evaluacion();
        e1.setId(1L);
        e1.setNota(5.0);

        Evaluacion e2 = new Evaluacion();
        e2.setId(1L);
        e2.setNota(5.0);

        assertEquals(e1, e2);
        assertEquals(e1.hashCode(), e2.hashCode());
        assertNotNull(e1.toString());
    }
}