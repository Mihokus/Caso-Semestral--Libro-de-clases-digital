package cl.duoc.academic.model;
import java.lang.annotation.Inherited;

import javax.annotation.processing.Generated;

import jakarta.persistence.*;
import lombok.Data;
@Entity
@Data
public class Curso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
}
