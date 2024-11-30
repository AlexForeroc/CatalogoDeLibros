package com.alexfc.catalogodelibros.dto;

import java.util.List;

public record XlibroDTO(Long id, String titulo, List<XautorDTO> autores, String idiomas, Double numeroDeDescargas) {
    public XlibroDTO(Long id, String titulo, List<XautorDTO> autores, String idiomas, Double numeroDeDescargas) {
        this.id = id;
        this.titulo = titulo;
        this.autores = autores;
        this.idiomas = idiomas;
        this.numeroDeDescargas = numeroDeDescargas;
    }

    public Long id() {
        return this.id;
    }

    public String titulo() {
        return this.titulo;
    }

    public List<XautorDTO> autores() {
        return this.autores;
    }

    public String idiomas() {
        return this.idiomas;
    }

    public Double numeroDeDescargas() {
        return this.numeroDeDescargas;
    }
}

