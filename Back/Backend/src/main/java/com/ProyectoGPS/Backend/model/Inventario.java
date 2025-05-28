package com.ProyectoGPS.Backend.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "inventario")
public class Inventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "producto_codigo", nullable = false)
    private String productoCodigo;

    @Column(name = "cantidad_inicial", nullable = false)
    private Integer cantidadInicial;

    @Column(name = "fecha_registro", nullable = false)
    private LocalDateTime fechaRegistro;

    // Getters y setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getProductoCodigo() {
        return productoCodigo;
    }
    public void setProductoCodigo(String productoCodigo) {
        this.productoCodigo = productoCodigo;
    }
    public Integer getCantidadInicial() {
        return cantidadInicial;
    }
    public void setCantidadInicial(Integer cantidadInicial) {
        this.cantidadInicial = cantidadInicial;
    }
    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }
    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
}
