package com.libroclases.gateway.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class InviteStudentRequest {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String nombre;

    private Long alumnoId;
}
