package com.libroclases.gateway.config;

import com.libroclases.gateway.model.Menu;
import com.libroclases.gateway.model.Role;
import com.libroclases.gateway.model.User;
import com.libroclases.gateway.repository.MenuRepository;
import com.libroclases.gateway.repository.RoleRepository;
import com.libroclases.gateway.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final MenuRepository menuRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        if (roleRepository.count() == 0) {
            seedRoles();
        }
        if (menuRepository.count() == 0) {
            seedMenus();
        }
        if (userRepository.count() == 0) {
            seedUsers();
        }
        log.info("DataInitializer done: {} roles, {} menus, {} users",
                roleRepository.count(), menuRepository.count(), userRepository.count());
    }

    private void seedRoles() {
        roleRepository.save(Role.builder().nombre("DOCENTE").descripcion("Profesor que toma asistencia y registra notas").build());
        roleRepository.save(Role.builder().nombre("APODERADO").descripcion("Padre/madre/tutor de uno o varios alumnos").build());
        roleRepository.save(Role.builder().nombre("ESTUDIANTE").descripcion("Alumno con acceso solo-lectura").build());
        roleRepository.save(Role.builder().nombre("ADMIN").descripcion("Administrador del sistema").build());
        log.info("Seeded 4 roles");
    }

    private void seedMenus() {
        Role docente = roleRepository.findByNombre("DOCENTE").orElseThrow();
        Role apoderado = roleRepository.findByNombre("APODERADO").orElseThrow();
        Role estudiante = roleRepository.findByNombre("ESTUDIANTE").orElseThrow();
        Role admin = roleRepository.findByNombre("ADMIN").orElseThrow();

        Set<Role> all = Set.of(docente, apoderado, estudiante, admin);

        // Menús comunes
        saveMenu("Dashboard", "/dashboard", null, 1, all);
        saveMenu("Mensajes", "/mensajes", null, 5, all);

        // Docente
        saveMenu("Mis cursos", "/asignaturas", null, 2, Set.of(docente));
        saveMenu("Tomar asistencia", "/asistencia/tomar", null, 3, Set.of(docente));
        saveMenu("Registrar notas", "/notas/registrar", null, 4, Set.of(docente));
        saveMenu("Rendimiento", "/rendimiento", null, 6, Set.of(docente, admin));

        // Apoderado
        saveMenu("Mis pupilos", "/pupilos", null, 2, Set.of(apoderado));
        saveMenu("Asistencia pupilo", "/pupilos/asistencia", null, 3, Set.of(apoderado));
        saveMenu("Notas pupilo", "/pupilos/notas", null, 4, Set.of(apoderado));

        // Estudiante
        saveMenu("Mi asistencia", "/mi-asistencia", null, 2, Set.of(estudiante));
        saveMenu("Mis notas", "/mis-notas", null, 3, Set.of(estudiante));

        // Admin
        saveMenu("Usuarios", "/admin/users", null, 10, Set.of(admin));
        saveMenu("Roles", "/admin/roles", null, 11, Set.of(admin));
        saveMenu("Menús", "/admin/menus", null, 12, Set.of(admin));

        log.info("Seeded menus");
    }

    private void saveMenu(String label, String path, Long parentId, Integer orden, Set<Role> roles) {
        menuRepository.save(Menu.builder()
                .label(label)
                .path(path)
                .parentId(parentId)
                .orden(orden)
                .roles(new HashSet<>(roles))
                .build());
    }

    private void seedUsers() {
        Map<String, String> demoUsers = new LinkedHashMap<>();
        demoUsers.put("admin@colegio.cl", "ADMIN");
        demoUsers.put("docente@colegio.cl", "DOCENTE");
        demoUsers.put("apoderado@colegio.cl", "APODERADO");
        demoUsers.put("estudiante@colegio.cl", "ESTUDIANTE");

        demoUsers.forEach((email, roleName) -> {
            Role role = roleRepository.findByNombre(roleName).orElseThrow();
            String rawPassword = roleName.toLowerCase() + "123";
            String nombre = capitalize(roleName.toLowerCase()) + " Demo";
            User user = User.builder()
                    .email(email)
                    .passwordHash(passwordEncoder.encode(rawPassword))
                    .nombre(nombre)
                    .roles(new HashSet<>(Set.of(role)))
                    .build();
            userRepository.save(user);
        });

        log.info("Seeded 4 demo users (admin/docente/apoderado/estudiante)");
    }

    private String capitalize(String s) {
        return s.isEmpty() ? s : Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }
}
