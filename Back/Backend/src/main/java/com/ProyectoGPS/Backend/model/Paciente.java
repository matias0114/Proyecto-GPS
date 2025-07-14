package com.ProyectoGPS.Backend.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "paciente")
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_paciente")
    private Long id;

    private String nombre;
    private String apellido;

    @Column(unique = true)
    private String rut;

    private LocalDate fechaNacimiento;

    private String direccion;
    private String telefono;

    // --- NUEVOS CAMPOS PARA BENEFICIARIOS ---
    @Column(name = "es_beneficiario", nullable = false)
    private Boolean esBeneficiario = false;

    @Column(name = "tipo_beneficio")
    private String tipoBeneficio;

    @OneToMany(mappedBy = "paciente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HistorialClinico> historiales = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    // getter y setter de esBeneficiario
    public Boolean getEsBeneficiario() {
        return esBeneficiario;
    }
    
    public void setEsBeneficiario(Boolean esBeneficiario) {
        this.esBeneficiario = esBeneficiario;
    }

    // getter y setter de tipoBeneficio
    public String getTipoBeneficio() {
        return tipoBeneficio;
    }
    
    public void setTipoBeneficio(String tipoBeneficio) {
        this.tipoBeneficio = tipoBeneficio;
    }

    public List<HistorialClinico> getHistoriales() {
        return historiales;
    }

    public void setHistoriales(List<HistorialClinico> historiales) {
        this.historiales = historiales;
    }

}
