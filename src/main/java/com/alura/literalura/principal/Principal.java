package com.alura.literalura.principal;

import com.alura.literalura.model.Autor;
import com.alura.literalura.model.ListaDeLibros;
import com.alura.literalura.model.DatosLibros;
import com.alura.literalura.model.Libro;
import com.alura.literalura.repository.IAutorRepository;
import com.alura.literalura.repository.ILibroRepository;
import com.alura.literalura.service.ConsumoAPI;
import com.alura.literalura.service.ConvierteDatos;


import java.util.*;
import java.util.stream.Collectors;

public class Principal {

    private ConsumoAPI consumo = new ConsumoAPI();
    private ConvierteDatos conversor = new ConvierteDatos();
    private String URL_BASE = "https://gutendex.com/books/";
    public Scanner leer = new Scanner(System.in);
    private String json;
    private final ILibroRepository repoLibro;
    private final IAutorRepository repoAutor;


    public Principal(ILibroRepository  repoLibro, IAutorRepository repoAutor){
        this.repoLibro = repoLibro;
        this.repoAutor = repoAutor;
    }



    private void buscarLibroPorTitulo () {
        System.out.println("Ingrese titulo del libro a buscar: ");
        var tituloTeclado = leer.nextLine();
        json = consumo.obtenerDatos(URL_BASE+"?search="+tituloTeclado.replace(" ","+"));
        ListaDeLibros datosTitulo = conversor.obtenerDatos(json, ListaDeLibros.class);

        if (datosTitulo != null && datosTitulo.libros() != null){

            Optional<DatosLibros> resultadoLibro = datosTitulo.libros().stream()
                    .filter(t -> t.titulo() != null && t.titulo().toLowerCase().contains(tituloTeclado.toLowerCase()))
                    .findFirst();

            if (resultadoLibro.isPresent()){

                DatosLibros datosLibro = resultadoLibro.get();

                Libro libro = new Libro();
                libro.setTitulo(datosLibro.titulo());

                List<Autor> autores = datosLibro.arrayAutor().stream()
                        .map(da -> new Autor(da.nombre(),da.fecha_nacimiento(),da.fecha_fallecimiento()))
                        .collect(Collectors.toList());
                libro.setArrayAutor(autores);
                libro.setIdiomas(datosLibro.idiomas());
                libro.setTotalDescargas(datosLibro.totalDescargas());

                System.out.println("Titulo encontrado: " + libro.toString());
                repoLibro.save(libro);
                System.out.println("El titulo ha sido almacenado en la base de datos...");

            }else{
                System.out.println("El Titulo ingresado no fue encontrado");
            }

        }else{
            System.out.println("No se encontraron resultados");
        }
    }

    private void buscarLibroPorAutor() {
        System.out.println("Ingrese nombre del autor del libro a buscar: ");
        var autorTeclado = leer.nextLine();
        json = consumo.obtenerDatos(URL_BASE+"?search="+ autorTeclado.replace(" ","+"));
        ListaDeLibros datosTitulo = conversor.obtenerDatos(json, ListaDeLibros.class);

        Optional<DatosLibros> encontrado = datosTitulo.libros().stream().findFirst();

        if (encontrado.isPresent()){

            DatosLibros datosLibro = encontrado.get();

            List<Autor> autors = encontrado.get().arrayAutor().stream()
                    .map(da -> new Autor(da.nombre(),da.fecha_nacimiento(),da.fecha_fallecimiento()))
                    .collect(Collectors.toList());

            Libro libro = new Libro();
            libro.setTitulo(datosLibro.titulo());
            libro.setArrayAutor(autors);
            libro.setIdiomas(datosLibro.idiomas());
            libro.setTotalDescargas(datosLibro.totalDescargas());

            System.out.println("Titulo encontrado: " + libro);
            repoLibro.save(libro);
            System.out.println("El titulo ha sido almacenado en la base de datos...");
        }else{
            System.out.println("Autor no encontrado :(");
        }

    }

    private List<Libro> todosLosLibros() {
        List<Libro> libros = repoLibro.findAll();
        libros.forEach(System.out::println);
        return libros;
    }

    private List<Autor> todosLosAutores() {
        List<Autor> autores = repoAutor.findAll();
        autores.forEach(System.out::println);
        return autores;
    }

    private void autoresVivosEn() {

        System.out.println("Ingrese año hasta el cual el autor se encontraba con vida: ");
        int fecha = Integer.parseInt(leer.nextLine());

        List<Autor> listaAutoresVivos = repoAutor.consultarAutoresVivos(fecha);
        listaAutoresVivos.forEach(System.out::println);
    }

    private void librosPorIdioma() {
        System.out.println("Ingrese idioma por el cual desea filtrar: ");
        String idiomaSeleccionado = leer.nextLine();
        List<Libro> listado = repoLibro.listaDelIdioma(idiomaSeleccionado);
        listado.forEach(System.out::println);
    }

    public void mostrarMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
        MENU PRINCIPAL
        1 - Buscar libro por Titulo
        2 - Buscar libro por Autor
        3 - Libros de mi colección
        4 - Autores en mi coleccion
        5 - Autores vivos en un determinado año
        6 - Filtrar libros por idioma
        0 - Salir del programa
        
        Elija la opcion que quiera
        """;
            System.out.println(menu);
            opcion = leer.nextInt();
            leer.nextLine();

            switch (opcion) {
                case 1:
                    buscarLibroPorTitulo();
                    break;
                case 2:
                    buscarLibroPorAutor();
                    break;
                case 3:
                    todosLosLibros();
                    break;
                case 4:
                    todosLosAutores();
                    break;
                case 5:
                    autoresVivosEn();
                    break;
                case 6:
                    librosPorIdioma();
                    break;
                case 0:
                    System.out.println("Cerrando la aplicación...");
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        }
    }





}
