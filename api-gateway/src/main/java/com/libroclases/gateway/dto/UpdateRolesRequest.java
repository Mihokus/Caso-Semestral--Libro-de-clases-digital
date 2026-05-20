package com.libroclases.gateway.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.Set;

@Data
public class UpdateRolesRequest {

    @NotEmpty
    private Set<Long> roleIds;
}
