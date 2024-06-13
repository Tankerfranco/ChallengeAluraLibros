package com.alura.literalura.repository;

import com.alura.literalura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IAutorRepository extends IRepository<Autor, Long> {
    @Query("SELECT a FROM Autor a WHERE a.fecha_fallecimiento < :fecha")
    List<Autor> consultarAutoresVivos(int fecha);
}
