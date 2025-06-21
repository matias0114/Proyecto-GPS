package com.ProyectoGPS.Backend.controller;

import com.ProyectoGPS.Backend.model.Paciente;
import com.ProyectoGPS.Backend.repository.PacienteRepository;

import java.util.List;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pacientes")
public class PacienteController {

    @Autowired
    private PacienteRepository pacienteRepository;

    // 1. Registrar nuevo paciente (CREATE)
    @PostMapping
    public ResponseEntity<Paciente> crearPaciente(@RequestBody Paciente paciente) {
        // Verificar si el DNI ya existe (opcional, para mejorar la respuesta)
        if (pacienteRepository.findByDni(paciente.getDni()).isPresent()) {
            // Retornar código 409 Conflict si el DNI ya está registrado
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        Paciente guardado = pacienteRepository.save(paciente);
        return ResponseEntity.ok(guardado);  // Retorna 200 OK con el paciente guardado en el cuerpo
    }

    // 2. Listar todos los pacientes (READ - collection)
    @GetMapping
    public List<Paciente> listarPacientes() {
        return pacienteRepository.findAll();
    }

    // 3. Buscar paciente por DNI (READ - individual)
    @GetMapping("/{dni}")
    public ResponseEntity<Paciente> obtenerPacientePorDni(@PathVariable String dni) {
        Optional<Paciente> pacienteOpt = pacienteRepository.findByDni(dni);
        if (pacienteOpt.isPresent()) {
            return ResponseEntity.ok(pacienteOpt.get());
        } else {
            return ResponseEntity.notFound().build();  // Retorna 404 si no se encuentra el DNI
        }
    }
}
