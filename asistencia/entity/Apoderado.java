package cl.colegio.ohiggins.asistencia.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "apoderados")
@Data
public class Apoderado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(unique = true)
    private String email;
}
