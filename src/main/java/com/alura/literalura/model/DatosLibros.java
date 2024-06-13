package com.alura.literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosLibros(
        @JsonAlias("id")
        int id,
        @JsonAlias("title")
        String titulo,
        @JsonAlias("authors")
        List<DatosAutor> arrayAutor,
        @JsonAlias("languages")
        List<String> idiomas,
        @JsonAlias("download_count")
        int totalDescargas
) {
}

