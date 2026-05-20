package cl.colegio.ohiggins.asistencia.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "alumnos")
@Data
public class Alumno {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String rut;

    @Column(nullable = false)
    private String nombre;

    private Long cursoId;
    private String cursoNombre;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "alumno_apoderado",
            joinColumns = @JoinColumn(name = "alumno_id"),
            inverseJoinColumns = @JoinColumn(name = "apoderado_id")
    )
    private Set<Apoderado> apoderados = new HashSet<>();
}
