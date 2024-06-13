package com.alura.literalura.repository;

import com.alura.literalura.model.Autor;
import com.alura.literalura.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ILibroRepository extends IRepository<Libro, Integer> {

    @Query("SELECT a FROM Autor a")
    List<Autor> findAllAutores();

    @Query("SELECT l FROM Libro l JOIN l.idiomas idioma WHERE idioma = :idiomaSeleccionado")
    List<Libro> listaDelIdioma(String idiomaSeleccionado);
}
