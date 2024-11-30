package com.alexfc.catalogodelibros.dto;

public record XautorDTO(Long id, String nombre, String fechaNacimiento, String fechaFallecimiento) {
    public XautorDTO(Long id, String nombre, String fechaNacimiento, String fechaFallecimiento) {
        this.id = id;
        this.nombre = nombre;
        this.fechaNacimiento = fechaNacimiento;
        this.fechaFallecimiento = fechaFallecimiento;
    }

    public Long id() {
        return this.id;
    }

    public String nombre() {
        return this.nombre;
    }

    public String fechaNacimiento() {
        return this.fechaNacimiento;
    }

    public String fechaFallecimiento() {
        return this.fechaFallecimiento;
    }
}
