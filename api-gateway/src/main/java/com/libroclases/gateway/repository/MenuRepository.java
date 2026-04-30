package com.libroclases.gateway.repository;

import com.libroclases.gateway.model.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {

    /**
     * Devuelve los menús accesibles por al menos uno de los roles dados.
     */
    List<Menu> findDistinctByRoles_NombreIn(Set<String> roleNombres);
}
