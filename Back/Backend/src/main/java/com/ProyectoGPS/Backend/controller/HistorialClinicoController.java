package com.ProyectoGPS.Backend.controller;

import com.ProyectoGPS.Backend.dto.HistorialClinicoDTO;
import com.ProyectoGPS.Backend.service.HistorialClinicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class HistorialClinicoController {

    @Autowired
    private HistorialClinicoService historialService;

    @GetMapping("/pacientes/{idPaciente}/historiales")
    public ResponseEntity<List<HistorialClinicoDTO>> listarHistorialesPorPaciente(@PathVariable Long idPaciente) {
        return ResponseEntity.ok(historialService.findByPacienteId(idPaciente));
    }

    @GetMapping("/historiales/{id}")
    public ResponseEntity<HistorialClinicoDTO> obtenerHistorial(@PathVariable Long id) {
        return historialService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/pacientes/{idPaciente}/historiales")
    public ResponseEntity<HistorialClinicoDTO> crearHistorial(
            @PathVariable Long idPaciente,
            @RequestBody HistorialClinicoDTO historialDTO) {
        historialDTO.setPacienteId(idPaciente);
        return ResponseEntity.ok(historialService.save(historialDTO));
    }

    @PutMapping("/historiales/{id}")
    public ResponseEntity<HistorialClinicoDTO> actualizarHistorial(
            @PathVariable Long id,
            @RequestBody HistorialClinicoDTO historialDTO) {
        return historialService.update(id, historialDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/historiales/{id}")
    public ResponseEntity<Void> eliminarHistorial(@PathVariable Long id) {
        historialService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
