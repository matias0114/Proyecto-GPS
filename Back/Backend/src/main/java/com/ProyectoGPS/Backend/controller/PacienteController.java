package com.ProyectoGPS.Backend.controller;

import com.ProyectoGPS.Backend.dto.PacienteDTO;
import com.ProyectoGPS.Backend.service.PacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;            // <— Asegúrate de esto
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/pacientes")
public class PacienteController {

    @Autowired
    private PacienteService pacienteService;

    // dentro de PacienteController
@PostMapping
    public ResponseEntity<PacienteDTO> crearPaciente(@RequestBody PacienteDTO pacienteDTO) {
        if (pacienteService.findByRut(pacienteDTO.getRut()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        return ResponseEntity.ok(pacienteService.save(pacienteDTO));
    }

    // 2. Listar todos los pacientes (READ - collection)
    @GetMapping
    public List<PacienteDTO> listarPacientes() {
        return pacienteService.findAll();
    }


    // Mantener el endpoint genérico por compatibilidad
    @GetMapping("/{rut}")
    public ResponseEntity<PacienteDTO> obtenerPacientePorRut(@PathVariable String rut) {
        return pacienteService.findByRut(rut)
                              .map(ResponseEntity::ok)
                              .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 4. Bulk upload de pacientes (JSON array)
    @PostMapping("/upload")
    public ResponseEntity<List<PacienteDTO>> uploadPacientes(
            @RequestBody List<PacienteDTO> pacientesDto) {

        List<PacienteDTO> guardados = pacientesDto.stream()
                                                  .map(pacienteService::save)
                                                  .collect(Collectors.toList());

        return ResponseEntity.ok(guardados);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PacienteDTO> actualizarPaciente(
        @PathVariable Long id,
        @RequestBody PacienteDTO pacienteDTO
    ) {
        return pacienteService.findById(id)
            .map(existing -> {
                pacienteDTO.setId(id);
                PacienteDTO actualizado = pacienteService.save(pacienteDTO);
                return ResponseEntity.ok(actualizado);
            })
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/beneficiarios")
    public List<PacienteDTO> listarSoloBeneficiarios() {
        return pacienteService.findBeneficiarios();
    }

    @PatchMapping("/{id}/beneficiario")
    public ResponseEntity<PacienteDTO> toggleBeneficiario(
        @PathVariable Long id,
        @RequestBody Map<String, String> body
    ) {
        Boolean esBen = Boolean.valueOf(body.get("esBeneficiario"));
        String tipo = body.get("tipoBeneficio");
        try {
            PacienteDTO actualizado = pacienteService.actualizarBeneficio(id, esBen, tipo);
            return ResponseEntity.ok(actualizado);
        } catch (jakarta.persistence.EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
