package com.ProyectoGPS.Backend.controller;

import com.ProyectoGPS.Backend.service.PacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/pacientes")
@CrossOrigin(origins = "*")
public class BeneficiosController {

    @Autowired
    private PacienteService pacienteService;

    /**
     * Endpoint para obtener información de beneficios de un paciente por RUT
     * Este endpoint es consumido por el microservicio de ventas/stock
     */
    @GetMapping("/{rut}/beneficios")
    public ResponseEntity<Map<String, Object>> obtenerBeneficiosPorRut(@PathVariable String rut) {
        try {
            var paciente = pacienteService.findByRut(rut);
            
            if (paciente.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            var pacienteDTO = paciente.get();
            Map<String, Object> benefitInfo = new HashMap<>();
            
            // Información básica del beneficiario
            benefitInfo.put("patientRut", rut);
            benefitInfo.put("isBeneficiary", pacienteDTO.getEsBeneficiario() != null ? pacienteDTO.getEsBeneficiario() : false);
            benefitInfo.put("benefitType", pacienteDTO.getTipoBeneficio());
            
            // Calcular porcentaje de descuento según el tipo de beneficio definido en el frontend
            int discountPercentage = 0;
            boolean hasApplicableBenefits = false;
            
            if (Boolean.TRUE.equals(pacienteDTO.getEsBeneficiario()) && pacienteDTO.getTipoBeneficio() != null) {
                hasApplicableBenefits = true;
                switch (pacienteDTO.getTipoBeneficio().toUpperCase()) {
                    case "ADULTO_MAYOR":
                        discountPercentage = 20; // 20% de descuento para adultos mayores
                        break;
                    case "CRONICO":
                        discountPercentage = 25; // 25% de descuento para pacientes crónicos
                        break;
                    default:
                        discountPercentage = 10; // Descuento por defecto para otros tipos
                        break;
                }
            }
            
            benefitInfo.put("discountPercentage", discountPercentage);
            benefitInfo.put("hasApplicableBenefits", hasApplicableBenefits);
            
            // Información adicional para compatibilidad con el microservicio de ventas
            benefitInfo.put("isElderly", "ADULTO_MAYOR".equals(pacienteDTO.getTipoBeneficio()));
            benefitInfo.put("hasMedicalBenefits", "CRONICO".equals(pacienteDTO.getTipoBeneficio()));
            benefitInfo.put("hasSocialBenefits", false); // Se puede expandir según necesidad
            
            return ResponseEntity.ok(benefitInfo);
            
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Error obteniendo información de beneficios");
            error.put("message", e.getMessage());
            error.put("timestamp", System.currentTimeMillis());
            return ResponseEntity.status(500).body(error);
        }
    }

    /**
     * Endpoint para obtener los tipos de beneficios disponibles
     */
    @GetMapping("/beneficios/tipos")
    public ResponseEntity<Map<String, Object>> obtenerTiposBeneficios() {
        Map<String, Object> tipos = new HashMap<>();
        
        // Tipos definidos en el frontend
        Map<String, Object> tiposDisponibles = new HashMap<>();
        tiposDisponibles.put("ADULTO_MAYOR", Map.of(
            "name", "Adulto Mayor",
            "description", "Descuento para pacientes de tercera edad",
            "discountPercentage", 20
        ));
        tiposDisponibles.put("CRONICO", Map.of(
            "name", "Paciente Crónico",
            "description", "Descuento para pacientes con enfermedades crónicas",
            "discountPercentage", 25
        ));
        
        tipos.put("availableTypes", tiposDisponibles);
        tipos.put("timestamp", System.currentTimeMillis());
        
        return ResponseEntity.ok(tipos);
    }

    /**
     * Endpoint para actualizar el beneficio de un paciente por RUT
     */
    @PatchMapping("/{rut}/beneficiario")
    public ResponseEntity<Map<String, Object>> actualizarBeneficioPorRut(
            @PathVariable String rut,
            @RequestBody Map<String, Object> benefitData) {
        
        try {
            var pacienteOpt = pacienteService.findByRut(rut);
            if (pacienteOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            
            var pacienteDTO = pacienteOpt.get();
            
            // Actualizar beneficiario
            if (benefitData.containsKey("esBeneficiario")) {
                pacienteDTO.setEsBeneficiario((Boolean) benefitData.get("esBeneficiario"));
            }
            
            if (benefitData.containsKey("tipoBeneficio")) {
                pacienteDTO.setTipoBeneficio((String) benefitData.get("tipoBeneficio"));
            }
            
            // Si no es beneficiario, limpiar el tipo de beneficio
            if (Boolean.FALSE.equals(pacienteDTO.getEsBeneficiario())) {
                pacienteDTO.setTipoBeneficio(null);
            }
            
            var pacienteActualizado = pacienteService.save(pacienteDTO);
            
            Map<String, Object> result = new HashMap<>();
            result.put("updated", true);
            result.put("patientRut", rut);
            result.put("isBeneficiary", pacienteActualizado.getEsBeneficiario());
            result.put("benefitType", pacienteActualizado.getTipoBeneficio());
            result.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.ok(result);
            
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Error actualizando beneficio");
            error.put("message", e.getMessage());
            error.put("timestamp", System.currentTimeMillis());
            return ResponseEntity.status(500).body(error);
        }
    }

    /**
     * Endpoint para verificar beneficios automáticos por edad
     */
    @GetMapping("/{rut}/beneficios/automaticos")
    public ResponseEntity<Map<String, Object>> verificarBeneficiosAutomaticos(@PathVariable String rut) {
        try {
            var pacienteOpt = pacienteService.findByRut(rut);
            if (pacienteOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            
            var pacienteDTO = pacienteOpt.get();
            Map<String, Object> result = new HashMap<>();
            
            // Calcular edad si es posible
            boolean qualifiesForAgeBenefit = false;
            String suggestedBenefitType = null;
            
            if (pacienteDTO.getFechaNacimiento() != null) {
                var edad = java.time.Period.between(pacienteDTO.getFechaNacimiento(), java.time.LocalDate.now()).getYears();
                
                if (edad >= 65) {
                    qualifiesForAgeBenefit = true;
                    suggestedBenefitType = "ADULTO_MAYOR";
                }
                
                result.put("age", edad);
            }
            
            result.put("patientRut", rut);
            result.put("qualifiesForAgeBenefit", qualifiesForAgeBenefit);
            result.put("suggestedBenefitType", suggestedBenefitType);
            result.put("currentBenefitType", pacienteDTO.getTipoBeneficio());
            result.put("isBeneficiary", pacienteDTO.getEsBeneficiario());
            result.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.ok(result);
            
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Error verificando beneficios automáticos");
            error.put("message", e.getMessage());
            error.put("timestamp", System.currentTimeMillis());
            return ResponseEntity.status(500).body(error);
        }
    }
}
