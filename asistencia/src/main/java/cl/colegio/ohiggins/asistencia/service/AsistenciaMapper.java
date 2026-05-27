package cl.colegio.ohiggins.asistencia.service;

import cl.colegio.ohiggins.asistencia.dto.AlumnoDTO;
import cl.colegio.ohiggins.asistencia.dto.AnotacionDTO;
import cl.colegio.ohiggins.asistencia.dto.ApoderadoInfo;
import cl.colegio.ohiggins.asistencia.dto.AsistenciaDTO;
import cl.colegio.ohiggins.asistencia.dto.RegistradoPor;
import cl.colegio.ohiggins.asistencia.entity.Alumno;
import cl.colegio.ohiggins.asistencia.entity.Apoderado;
import cl.colegio.ohiggins.asistencia.model.Anotacion;
import cl.colegio.ohiggins.asistencia.model.Asistencia;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class AsistenciaMapper {

    public AlumnoDTO toAlumnoDTO(Alumno a) {
        return new AlumnoDTO(
                a.getId(),
                a.getNombre(),
                a.getRut(),
                a.getCursoId(),
                a.getCursoNombre(),
                toApoderadoInfoList(a.getApoderados())
        );
    }

    public List<ApoderadoInfo> toApoderadoInfoList(Set<Apoderado> apoderados) {
        List<ApoderadoInfo> out = new ArrayList<>();
        if (apoderados == null) return out;
        for (Apoderado ap : apoderados) {
            out.add(new ApoderadoInfo(ap.getId(), ap.getNombre(), ap.getEmail()));
        }
        return out;
    }

    public AsistenciaDTO toAsistenciaDTO(Asistencia a) {
        RegistradoPor rp = new RegistradoPor(a.getRegistradoPorId(), a.getRegistradoPorNombre());
        return new AsistenciaDTO(
                a.getId(),
                a.getAlumnoId(),
                a.getAlumnoNombre(),
                a.getCursoId(),
                a.getCursoNombre(),
                a.getFecha(),
                a.getEstado(),
                rp);
    }

    public AnotacionDTO toAnotacionDTO(Anotacion an) {
        RegistradoPor rp = new RegistradoPor(an.getRegistradoPorId(), an.getRegistradoPorNombre());
        return new AnotacionDTO(
                an.getId(),
                an.getAlumnoId(),
                an.getAlumnoNombre(),
                an.getTipo(),
                an.getDescripcion(),
                an.getFecha(),
                rp);
    }
}
