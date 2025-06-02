package com.ProyectoGPS.Backend.controller;

import com.ProyectoGPS.Backend.model.Paciente;
import com.ProyectoGPS.Backend.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pacientes")
public class PacienteController {

    @Autowired
    private PacienteRepository pacienteRepository;

    @PostMapping
    public ResponseEntity<Paciente> crearPaciente(@RequestBody Paciente paciente) {
        Paciente guardado = pacienteRepository.save(paciente);
        return ResponseEntity.ok(guardado);
    }
}