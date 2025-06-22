package com.ProyectoGPS.Backend;

import com.ProyectoGPS.Backend.model.Paciente;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class PacienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void registrarPaciente_correcto() throws Exception {
        Paciente paciente = new Paciente();
        paciente.setNombre("Matias");
        paciente.setApellido("Urrutia");
        paciente.setDni("20539061-8");
        paciente.setFechaNacimiento(LocalDate.of(2025, 6, 4));
        paciente.setDireccion("chillan viejo");
        paciente.setTelefono("12312312312");

        mockMvc.perform(post("/api/pacientes/upload")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(List.of(paciente))))
                .andExpect(status().isOk());
    }
}
