package com.ProyectoGPS.Backend.service;

import com.ProyectoGPS.Backend.dto.HistorialClinicoDTO;
import com.ProyectoGPS.Backend.model.HistorialClinico;
import com.ProyectoGPS.Backend.model.Paciente;
import com.ProyectoGPS.Backend.repository.HistorialClinicoRepository;
import com.ProyectoGPS.Backend.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HistorialClinicoService {

    @Autowired
    private HistorialClinicoRepository historialRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    public List<HistorialClinicoDTO> findByPacienteId(Long pacienteId) {
        return historialRepository.findByPacienteId(pacienteId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<HistorialClinicoDTO> findById(Long id) {
        return historialRepository.findById(id).map(this::toDTO);
    }

    @Transactional
    public HistorialClinicoDTO save(HistorialClinicoDTO dto) {
        Optional<Paciente> paciente = pacienteRepository.findById(dto.getPacienteId());
        if (!paciente.isPresent()) {
            throw new RuntimeException("Paciente no encontrado con ID: " + dto.getPacienteId());
        }

        HistorialClinico historial = toEntity(dto);
        historial.setPaciente(paciente.get());
        return toDTO(historialRepository.save(historial));
    }

    @Transactional
    public Optional<HistorialClinicoDTO> update(Long id, HistorialClinicoDTO dto) {
        return historialRepository.findById(id)
                .map(historial -> {
                    historial.setFecha(dto.getFecha());
                    historial.setDiagnostico(dto.getDiagnostico());
                    historial.setTratamiento(dto.getTratamiento());
                    historial.setObservaciones(dto.getObservaciones());
                    historial.setMedicamentosRecetados(dto.getMedicamentosRecetados());
                    return toDTO(historialRepository.save(historial));
                });
    }

    public void deleteById(Long id) {
        historialRepository.deleteById(id);
    }

    private HistorialClinicoDTO toDTO(HistorialClinico historial) {
        HistorialClinicoDTO dto = new HistorialClinicoDTO();
        dto.setId(historial.getId());
        dto.setPacienteId(historial.getPaciente().getId());
        dto.setFecha(historial.getFecha());
        dto.setDiagnostico(historial.getDiagnostico());
        dto.setTratamiento(historial.getTratamiento());
        dto.setObservaciones(historial.getObservaciones());
        dto.setMedicamentosRecetados(historial.getMedicamentosRecetados());
        return dto;
    }

    private HistorialClinico toEntity(HistorialClinicoDTO dto) {
        HistorialClinico historial = new HistorialClinico();
        historial.setId(dto.getId());
        historial.setFecha(dto.getFecha());
        historial.setDiagnostico(dto.getDiagnostico());
        historial.setTratamiento(dto.getTratamiento());
        historial.setObservaciones(dto.getObservaciones());
        historial.setMedicamentosRecetados(dto.getMedicamentosRecetados());
        return historial;
    }
}
