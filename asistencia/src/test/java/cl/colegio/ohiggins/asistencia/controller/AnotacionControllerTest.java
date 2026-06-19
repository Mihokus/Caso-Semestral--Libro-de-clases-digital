package cl.colegio.ohiggins.asistencia.controller;

import cl.colegio.ohiggins.asistencia.dto.AnotacionDTO;
import cl.colegio.ohiggins.asistencia.service.query.AnotacionQueryService;
import cl.colegio.ohiggins.asistencia.strategy.anotacion.AnotacionStrategy;
import cl.colegio.ohiggins.asistencia.strategy.anotacion.AnotacionStrategyFactory;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AnotacionController.class)
class AnotacionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AnotacionStrategyFactory factory;

    @MockBean
    private AnotacionQueryService queryService;

    @MockBean
    private AnotacionStrategy strategy;

    @Test
    void registrarAnotacion() throws Exception {

        AnotacionDTO dto = new AnotacionDTO();
        dto.setId(1L);

        Mockito.when(factory.get(ArgumentMatchers.any()))
                .thenReturn(strategy);

        Mockito.when(strategy.ejecutar(ArgumentMatchers.any()))
                .thenReturn(dto);

        String json = """
                {
                  "alumnoId":1,
                  "tipo":"POSITIVA",
                  "descripcion":"Participacion destacada"
                }
                """;

        mockMvc.perform(post("/api/anotaciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());
    }
}