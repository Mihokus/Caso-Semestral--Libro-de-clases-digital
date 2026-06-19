package cl.colegio.ohiggins.asistencia.service.mapper; // <--- CORREGIDO: Ahora coincide con la carpeta real

import cl.colegio.ohiggins.asistencia.dto.*;
import cl.colegio.ohiggins.asistencia.entity.Alumno;
import cl.colegio.ohiggins.asistencia.entity.Apoderado;
import cl.colegio.ohiggins.asistencia.model.Anotacion;
import cl.colegio.ohiggins.asistencia.model.Asistencia;
import cl.colegio.ohiggins.asistencia.model.EstadoAsistencia;
import cl.colegio.ohiggins.asistencia.model.TipoAnotacion;
import cl.colegio.ohiggins.asistencia.service.AsistenciaMapper; // <--- CORREGIDO: Importamos la clase real
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class AsistenciaMapperTest {

    private final AsistenciaMapper mapper = new AsistenciaMapper();

    // ==========================================
    // TESTS ALUMNO & APODERADOS
    // ==========================================
    @Test
    void debeMapearAlumnoADTOConApoderados() {
        Alumno alumno = new Alumno();
        alumno.setId(1L);
        alumno.setNombre("Juan Perez");
        alumno.setRut("12345678-9");
        alumno.setCursoId(10L);
        alumno.setCursoNombre("1° Medio A");

        Apoderado apoderado = new Apoderado();
        apoderado.setId(2L);
        apoderado.setNombre("Pedro Perez");
        apoderado.setEmail("pedro@mail.com");
        alumno.setApoderados(new HashSet<>(Set.of(apoderado)));

        AlumnoDTO dto = mapper.toAlumnoDTO(alumno);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("Juan Perez", dto.getNombre());
        assertEquals(1, dto.getApoderados().size());
        assertEquals("Pedro Perez", dto.getApoderados().get(0).getNombre());
    }

    @Test
    void debeRetornarNullCuandoAlumnoEsNull() {
        assertNull(mapper.toAlumnoDTO(null));
    }

    @Test
    void debeRetornarListaVaciaCuandoApoderadosEsNull() {
        List<ApoderadoInfo> resultado = mapper.toApoderadoInfoList(null);
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    // ==========================================
    // TESTS ASISTENCIA
    // ==========================================
    @Test
    void debeMapearAsistenciaADTO() {
        Asistencia asistencia = new Asistencia();
        asistencia.setId(100L);
        asistencia.setAlumnoId(5L);
        asistencia.setAlumnoNombre("Diego");
        asistencia.setCursoId(20L);
        asistencia.setCursoNombre("2° Medio B");
        asistencia.setFecha(LocalDate.now());
        asistencia.setEstado(EstadoAsistencia.PRESENTE);
        asistencia.setRegistradoPorId(50L);
        asistencia.setRegistradoPorNombre("Profesor");

        AsistenciaDTO dto = mapper.toAsistenciaDTO(asistencia);

        assertNotNull(dto);
        assertEquals(100L, dto.getId());
        assertEquals(5L, dto.getAlumnoId());
        assertNotNull(dto.getRegistradoPor());
        assertEquals("Profesor", dto.getRegistradoPor().getNombre());
    }

    @Test
    void debeRetornarNullCuandoAsistenciaEsNull() {
        assertNull(mapper.toAsistenciaDTO(null));
    }

    // ==========================================
    // TESTS ANOTACION
    // ==========================================
    @Test
    void debeMapearAnotacionADTO() {
        Anotacion anotacion = new Anotacion();
        anotacion.setId(200L);
        anotacion.setAlumnoId(8L);
        anotacion.setAlumnoNombre("Camila");
        anotacion.setTipo(TipoAnotacion.POSITIVA);
        anotacion.setDescripcion("Destacada participación");
        anotacion.setFecha(Instant.now());
        anotacion.setRegistradoPorId(60L);
        anotacion.setRegistradoPorNombre("Inspectora");

        AnotacionDTO dto = mapper.toAnotacionDTO(anotacion);

        assertNotNull(dto);
        assertEquals(200L, dto.getId());
        assertEquals(TipoAnotacion.POSITIVA, dto.getTipo());
        assertNotNull(dto.getRegistradoPor());
        assertEquals("Inspectora", dto.getRegistradoPor().getNombre());
    }

    @Test
    void debeRetornarNullCuandoAnotacionEsNull() {
        assertNull(mapper.toAnotacionDTO(null));
    }
}