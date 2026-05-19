package com.libroclases.gateway.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Set;

@Data
public class MenuRequest {

    @NotBlank
    private String label;

    @NotBlank
    private String path;

    private Long parentId;

    private Integer orden;

    private Set<Long> roleIds;
}
