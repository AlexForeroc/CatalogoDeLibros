package com.alexfc.catalogodelibros.model;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(
        ignoreUnknown = true
)
public record DatosLibros(String titulo, List<DatosAutor> autor, List<String> idiomas, Double numeroDescargas) {
    public DatosLibros(@JsonAlias({"title"}) String titulo, @JsonAlias({"authors"}) List<DatosAutor> autor, @JsonAlias({"languages"}) List<String> idiomas, @JsonAlias({"download_count"}) Double numeroDescargas) {
        this.titulo = titulo;
        this.autor = autor;
        this.idiomas = idiomas;
        this.numeroDescargas = numeroDescargas;
    }

    @JsonAlias({"title"})
    public String titulo() {
        return this.titulo;
    }

    @JsonAlias({"authors"})
    public List<DatosAutor> autor() {
        return this.autor;
    }

    @JsonAlias({"languages"})
    public List<String> idiomas() {
        return this.idiomas;
    }

    @JsonAlias({"download_count"})
    public Double numeroDescargas() {
        return this.numeroDescargas;
    }
}
