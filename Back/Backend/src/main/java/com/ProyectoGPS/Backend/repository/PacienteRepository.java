package com.ProyectoGPS.Backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ProyectoGPS.Backend.model.Paciente;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    Optional<Paciente> findByRut(String rut);

    // NUEVO: todos los pacientes marcados como beneficiarios
    List<Paciente> findByEsBeneficiarioTrue();
}

