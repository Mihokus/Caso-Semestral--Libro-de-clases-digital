package com.libroclases.gateway.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuDTO {

    private Long id;
    private String label;
    private String path;
    private Long parentId;
    private Integer orden;
    private Set<String> roles;
}
