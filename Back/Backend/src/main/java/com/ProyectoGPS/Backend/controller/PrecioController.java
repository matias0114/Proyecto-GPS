package com.ProyectoGPS.Backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ProyectoGPS.Backend.dto.PrecioUploadRequest;
import com.ProyectoGPS.Backend.model.Precio;
import com.ProyectoGPS.Backend.repository.PrecioRepository;

import java.util.List;

@RestController
@RequestMapping("/api/precios")
public class PrecioController {

    @Autowired
    private PrecioRepository precioRepository;

    @PostMapping
    public ResponseEntity<PrecioUploadRequest> crearPrecio(@RequestBody PrecioUploadRequest dto) {

        Precio precio = new Precio();
        precio.setId(dto.getId());
        precio.setProductoCodigo(dto.getProductoCodigo());
        precio.setPrecioUnitario(dto.getPrecioUnitario());
        precio.setFechaActualizacion(dto.getFechaActualizacion());

        Precio savedPrecio = precioRepository.save(precio);
        if (savedPrecio == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        PrecioUploadRequest savedDto = new PrecioUploadRequest();
        savedDto.setId(savedPrecio.getId());
        savedDto.setProductoCodigo(savedPrecio.getProductoCodigo());
        savedDto.setPrecioUnitario(savedPrecio.getPrecioUnitario());
        savedDto.setFechaActualizacion(savedPrecio.getFechaActualizacion());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedDto);

    }

    @GetMapping
    public ResponseEntity<List<PrecioUploadRequest>> obtenerPrecios() {
        List<Precio> precios = precioRepository.findAll();
        List<PrecioUploadRequest> dtos = precios.stream().map(precio -> {
            PrecioUploadRequest dto = new PrecioUploadRequest();
            dto.setId(precio.getId());
            dto.setProductoCodigo(precio.getProductoCodigo());
            dto.setPrecioUnitario(precio.getPrecioUnitario());
            dto.setFechaActualizacion(precio.getFechaActualizacion());
            return dto;
        }).toList();
        return ResponseEntity.ok(dtos);
    }
}
