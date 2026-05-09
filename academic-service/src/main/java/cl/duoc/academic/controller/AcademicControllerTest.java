package cl.duoc.academic.controller;
import cl.duoc.academic.dto.EvaluacionDTO;
import cl.duoc.academic.dto.RendimientoDTO;
import cl.duoc.academic.facade.AcademicFacade;
import cl.duoc.academic.model.Evaluacion;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Collections;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AcademicController.class)
public class AcademicControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AcademicFacade academicFacade;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("POST /registrarNotas - Debe responder 200 OK")
    public void testRegistrarNota() throws Exception {
        EvaluacionDTO dto = new EvaluacionDTO();
        dto.setNota(6.5);
        
        Mockito.when(academicFacade.registrarNota(Mockito.any(EvaluacionDTO.class)))
               .thenReturn(new Evaluacion());

        mockMvc.perform(post("/api/academica/registrarNotas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /rendimiento/{id} - Debe retornar objeto rico (Punto 8)")
    public void testObtenerRendimiento() throws Exception {
        RendimientoDTO mockDto = new RendimientoDTO(6.0, 5, "Bueno");
        
        Mockito.when(academicFacade.obtenerRendimientoTotal(1L)).thenReturn(mockDto);

        mockMvc.perform(get("/api/academica/rendimiento/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.promedio").value(6.0));
    }
}
