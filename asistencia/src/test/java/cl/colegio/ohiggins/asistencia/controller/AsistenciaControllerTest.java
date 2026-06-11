package cl.colegio.ohiggins.asistencia.controller;

import cl.colegio.ohiggins.asistencia.dto.AsistenciaDTO;
import cl.colegio.ohiggins.asistencia.strategy.asistencia.AsistenciaStrategy;
import cl.colegio.ohiggins.asistencia.strategy.asistencia.AsistenciaStrategyFactory;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AsistenciaController.class)
class AsistenciaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AsistenciaStrategyFactory factory;

    @MockBean
    private AsistenciaStrategy strategy;

    @Test
    void registrarAsistencia() throws Exception {

        AsistenciaDTO dto = new AsistenciaDTO();
        dto.setAlumnoId(1L);

        Mockito.when(factory.get(ArgumentMatchers.any()))
                .thenReturn(strategy);

        Mockito.when(strategy.ejecutar(ArgumentMatchers.any()))
                .thenReturn(dto);

        String json = """
                {
                  "cursoId":1,
                  "fecha":"2026-06-03",
                  "registradoPorId":1,
                  "registradoPorNombre":"Profesor",
                  "asistencias":[
                    {
                      "alumnoId":1,
                      "estado":"PRESENTE"
                    }
                  ]
                }
                """;

        mockMvc.perform(post("/api/asistencia")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());
    }
}