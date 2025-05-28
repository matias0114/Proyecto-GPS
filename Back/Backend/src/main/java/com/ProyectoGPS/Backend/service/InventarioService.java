package com.ProyectoGPS.Backend.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ProyectoGPS.Backend.dto.InventarioUploadRequest;
import com.ProyectoGPS.Backend.model.Inventario;
import com.ProyectoGPS.Backend.repository.InventarioRepository;

import jakarta.transaction.Transactional;

@Service
public class InventarioService {

    @Autowired
    private InventarioRepository inventarioRepository;

    @Transactional
    public void guardarInventarios(List<InventarioUploadRequest> lista) {
        for (InventarioUploadRequest req : lista) {
            Inventario entidad = new Inventario();
            entidad.setProductoCodigo(req.getProductoCodigo());
            entidad.setCantidadInicial(req.getCantidadInicial());
            entidad.setFechaRegistro(LocalDateTime.now());
            inventarioRepository.save(entidad);
        }
    }
}