package com.ProyectoGPS.Backend.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ProyectoGPS.Backend.dto.PrecioUploadRequest;
import com.ProyectoGPS.Backend.model.Precio;
import com.ProyectoGPS.Backend.repository.PrecioRepository;

import jakarta.transaction.Transactional;

@Service
public class PrecioService {

    @Autowired
    private PrecioRepository precioRepository;
    @Transactional
    public void guardarLista(List<PrecioUploadRequest> lista) {
        List<Precio> entidades = lista.stream().map(req -> {
            Precio entidad = new Precio();
            entidad.setProductoCodigo(req.getProductoCodigo());
            entidad.setPrecioUnitario(req.getPrecioUnitario());
            entidad.setFechaActualizacion(LocalDateTime.now());
            return entidad;
        }).collect(Collectors.toList());

        precioRepository.saveAll(entidades);
    }

}