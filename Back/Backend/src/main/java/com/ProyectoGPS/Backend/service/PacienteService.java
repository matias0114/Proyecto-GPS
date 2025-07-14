package com.ProyectoGPS.Backend.service;

import com.ProyectoGPS.Backend.dto.PacienteDTO;
import com.ProyectoGPS.Backend.model.Paciente;
import com.ProyectoGPS.Backend.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PacienteService {
    @Autowired
    private PacienteRepository pacienteRepository;

    public List<PacienteDTO> findAll() {
        return pacienteRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<PacienteDTO> findById(Long id) {
        return pacienteRepository.findById(id).map(this::toDTO);
    }

    public Optional<PacienteDTO> findByRut(String rut) {
        return pacienteRepository.findByRut(rut).map(this::toDTO);
    }

    public PacienteDTO save(PacienteDTO dto) {
        Paciente paciente = toEntity(dto);
        Paciente saved = pacienteRepository.save(paciente);
        return toDTO(saved);
    }

    public void deleteById(Long id) {
        pacienteRepository.deleteById(id);
    }

    public List<PacienteDTO> findBeneficiarios() {
        return pacienteRepository
                .findByEsBeneficiarioTrue()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public PacienteDTO actualizarBeneficio(Long id, Boolean esBeneficiario, String tipoBeneficio) {
        PacienteDTO dto = findById(id)
            .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("Paciente no existe"));
        dto.setEsBeneficiario(esBeneficiario);
        dto.setTipoBeneficio(tipoBeneficio);
        return save(dto);
    }

    // Métodos de conversión
    private PacienteDTO toDTO(Paciente paciente) {
        PacienteDTO dto = new PacienteDTO();
        dto.setId(paciente.getId());
        dto.setNombre(paciente.getNombre());
        dto.setApellido(paciente.getApellido());
        dto.setRut(paciente.getRut());
        dto.setFechaNacimiento(paciente.getFechaNacimiento());
        dto.setDireccion(paciente.getDireccion());
        dto.setTelefono(paciente.getTelefono());
        // -- Mapeo de beneficiarios --
        dto.setEsBeneficiario(paciente.getEsBeneficiario());
        dto.setTipoBeneficio(paciente.getTipoBeneficio());
        return dto;
    }

    private Paciente toEntity(PacienteDTO dto) {
        Paciente paciente = new Paciente();
        paciente.setId(dto.getId());
        paciente.setNombre(dto.getNombre());
        paciente.setApellido(dto.getApellido());
        paciente.setRut(dto.getRut());
        paciente.setFechaNacimiento(dto.getFechaNacimiento());
        paciente.setDireccion(dto.getDireccion());
        paciente.setTelefono(dto.getTelefono());
        // -- Mapeo de beneficiarios --
        paciente.setEsBeneficiario(dto.getEsBeneficiario() != null ? dto.getEsBeneficiario() : false);
        // Solo establecer tipoBeneficio si esBeneficiario es true
        paciente.setTipoBeneficio(Boolean.TRUE.equals(dto.getEsBeneficiario()) ? dto.getTipoBeneficio() : null);
        return paciente;
    }
}
