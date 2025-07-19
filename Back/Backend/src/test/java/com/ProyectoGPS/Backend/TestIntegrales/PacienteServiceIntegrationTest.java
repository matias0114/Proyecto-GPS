package com.ProyectoGPS.Backend.TestIntegrales;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.ProyectoGPS.Backend.dto.PacienteDTO;
import com.ProyectoGPS.Backend.model.Paciente;
import com.ProyectoGPS.Backend.repository.PacienteRepository;
import com.ProyectoGPS.Backend.service.PacienteService;
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class PacienteServiceIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PacienteRepository pacienteRepository;

    private PacienteService pacienteService;


    @BeforeEach
    public void setUp() {
        pacienteRepository.deleteAll();
    }

    @Test
    public void testCrearPaciente() throws Exception {
        String pacienteJson = "{ \"nombre\": \"Juan\", \"apellido\": \"Perez\", \"rut\": \"12345678-9\", \"fechaNacimiento\": \"1990-01-01\", \"direccion\": \"Calle Falsa 123\", \"telefono\": \"123456789\", \"esBeneficiario\": true, \"tipoBeneficio\": \"Salud\" }";

        mockMvc.perform(post("/api/pacientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(pacienteJson))
                .andExpect(MockMvcResultMatchers.status().isOk());

        List<PacienteDTO> pacientesDTO = pacienteRepository.findAll()
                                                        .stream()
                                                        .map(paciente -> {
                                                            PacienteDTO dto = new PacienteDTO();
                                                            dto.setId(paciente.getId());
                                                            dto.setNombre(paciente.getNombre());
                                                            dto.setApellido(paciente.getApellido());
                                                            dto.setRut(paciente.getRut());
                                                            dto.setFechaNacimiento(paciente.getFechaNacimiento());
                                                            dto.setDireccion(paciente.getDireccion());
                                                            dto.setTelefono(paciente.getTelefono());
                                                            dto.setEsBeneficiario(paciente.getEsBeneficiario());
                                                            dto.setTipoBeneficio(paciente.getTipoBeneficio());
                                                            return dto;
                                                        })
                                                        .collect(Collectors.toList());

        assert pacientesDTO.size() > 0;
    }




    @Test
    public void testObtenerPacientePorRut() throws Exception {
        Paciente paciente = new Paciente();
        paciente.setRut("98765432-1");
        paciente.setNombre("Carlos");
        pacienteRepository.save(paciente);

        mockMvc.perform(get("/api/pacientes/{rut}", "98765432-1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.rut").value("98765432-1"));
    }


    @Test
    public void testActualizarPaciente() throws Exception {
        Paciente paciente = new Paciente();
        paciente.setRut("11111111-1");
        paciente.setNombre("Ana");
        paciente.setFechaNacimiento(LocalDate.of(1992, 5, 20));
        paciente.setDireccion("Calle Ejemplo 123");
        paciente.setTelefono("987654321");
        pacienteRepository.save(paciente);

        mockMvc.perform(put("/api/pacientes/{id}", paciente.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"nombre\": \"Ana\", \"edad\": 30, \"rut\": \"11111111-1\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.nombre").value("Ana"));
    }


    @Test
    public void testSubirPacientes() throws Exception {
        String pacientesJson = "[{\"nombre\": \"Pedro\", \"edad\": 40, \"rut\": \"22222222-2\", \"direccion\": \"Av. Siempre Viva 456\", \"telefono\": \"987654321\"}," +
                               "{\"nombre\": \"Maria\", \"edad\": 50, \"rut\": \"33333333-3\", \"direccion\": \"Calle Sol 789\", \"telefono\": \"112233445\"}]";

        mockMvc.perform(post("/api/pacientes/upload")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(pacientesJson))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}