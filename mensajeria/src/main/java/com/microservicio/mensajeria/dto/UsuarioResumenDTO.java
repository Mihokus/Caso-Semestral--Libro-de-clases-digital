package com.microservicio.mensajeria.dto;

public class UsuarioResumenDTO {

    private Long id;
    private String nombre;
    private String rol;

    public UsuarioResumenDTO(Long id, String nombre, String rol) {
        this.id = id;
        this.nombre = nombre;
        this.rol = rol;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getRol() {
        return rol;
    }
}