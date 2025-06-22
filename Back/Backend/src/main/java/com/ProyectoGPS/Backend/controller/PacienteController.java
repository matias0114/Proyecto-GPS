package com.ProyectoGPS.Backend.controller;

import com.ProyectoGPS.Backend.dto.PacienteDTO;
import com.ProyectoGPS.Backend.service.PacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/pacientes")
public class PacienteController {

    @Autowired
    private PacienteService pacienteService;

    // 1. Registrar nuevo paciente (CREATE)
    @PostMapping
    public ResponseEntity<PacienteDTO> crearPaciente(@RequestBody PacienteDTO pacienteDTO) {
        if (pacienteService.findByDni(pacienteDTO.getDni()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        PacienteDTO guardado = pacienteService.save(pacienteDTO);
        return ResponseEntity.ok(guardado);
    }

    // 2. Listar todos los pacientes (READ - collection)
    @GetMapping
    public List<PacienteDTO> listarPacientes() {
        return pacienteService.findAll();
    }

    // 3. Buscar paciente por DNI (READ - individual)
    @GetMapping("/{dni}")
    public ResponseEntity<PacienteDTO> obtenerPacientePorDni(@PathVariable String dni) {
        Optional<PacienteDTO> pacienteOpt = pacienteService.findByDni(dni);
        return pacienteOpt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
