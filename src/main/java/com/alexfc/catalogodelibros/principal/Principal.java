package com.alexfc.catalogodelibros.principal;

import com.alexfc.catalogodelibros.dto.XautorDTO;
import com.alexfc.catalogodelibros.dto.XlibroDTO;
import com.alexfc.catalogodelibros.model.*;
import com.alexfc.catalogodelibros.repository.XautorRepository;
import com.alexfc.catalogodelibros.repository.XlibroRepository;
import com.alexfc.catalogodelibros.service.ConsumoAPI;
import com.alexfc.catalogodelibros.service.ConvierteDatos;

import java.time.Year;
import java.util.*;
import java.util.stream.Collectors;

public class Principal {

    private Scanner sc = new Scanner(System.in);
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private static final String URL_BASE = "https://gutendex.com/books/";
    private ConvierteDatos conversor = new ConvierteDatos();
    private XlibroRepository xlibroRepository;
    private XautorRepository xautorRepository;
    private List<Libro> libros;
    private List<Autor> autores;

    public Principal(XlibroRepository libroRepository, XautorRepository autorRepository) {
        this.xlibroRepository = libroRepository;
        this.xautorRepository = autorRepository;
    }

    public void muestraElMenu() {
        var opcion = -1;

        while (opcion != 0) {
            var menu = """
                    
                    ***************************************************
                    *     ~ BIENVENIDO AL CATALOGO DE LIBROS ~        *
                    ***************************************************
                    *                MENU PRINCIPAL                   *
                    ***************************************************
                    1 - Buscar Libro por Titulo                       *
                    2 - Listar Libros Registrados                     *
                    3 - Listar Autores Registrados                    *
                    4 - Listar Autores vivos en un determinado año    *
                    5 - Listar Libros por Idioma                      *
                    0 - Salir                                         *                  
                    ***************************************************
                    *   ~ CONOCIMIENTO ADQUIRIDO CON ORACOL ALURA ~   *
                    ***************************************************
                    
                    """;
            System.out.println(menu);
            System.out.print("Indique la opcion de su interes: ");
            String opcionMenu = sc.nextLine();
            try {
                opcion = Integer.parseInt(opcionMenu);
            } catch (NumberFormatException e) {
                System.out.println("Por favor ingrese una Opción válida [numero entero].");
                continue;
            }

            switch (opcion) {
                case 1:
                    buscarLibroPorTitulo();
                    break;
                case 2:
                    listarLibrosRegistrados();
                    break;
                case 3:
                    listarAutoresRegistrados();
                    break;
                case 4:
                    listarAutoresVivosEnAnio();
                    break;
                case 5:
                    listarLibrosPorIdioma();
                    break;
                case 0:
                    System.out.println("""
                    
                    **************************************************
                    Cerrando la aplicación...
                    **************************************************
                    """);
                    break;
                default:
                    System.out.println("Opción inválida");
            }

        }
    }


    // Encuentra el primer título con el que se busca
    private void buscarLibroPorTitulo() {
        DatosLibros datosLibros = getDatosLibros();

        if (datosLibros == null) {
            System.out.println("""
                    
                    **************************************************
                    Libro no Encontrado.
                    **************************************************""");
            pausa();
            return;
        }

        // Verificar si el libro ya existe en la base de datos
        Optional<Libro> libroExistente = xlibroRepository.findByTitulo(datosLibros.titulo());
        if (libroExistente.isPresent()) {
            System.out.println("""
                    
                    **************************************************
                    El libro ya esta registrado.
                    **************************************************""");

            // El libro existe en la base de datos y lo muestro:
            // Crear y mostrar DTO del libro guardado
            XlibroDTO libroDTO = new XlibroDTO(
                    libroExistente.get().getId(),
                    libroExistente.get().getTitulo(),
                    libroExistente.get().getAutores().stream().map(autor -> new XautorDTO(autor.getId(), autor.getNombre(), autor.getFechaNacimiento(), autor.getFechaFallecimiento()))
                            .collect(Collectors.toList()),
                    String.join(", ", libroExistente.get().getIdiomas()),
                    libroExistente.get().getNumeroDeDescargas()
            );

            // Imprimir detalles del libro ya registrado
            System.out.printf(
                    """
                            
                            **************************************************
                            *                      LIBRO                     *
                            **************************************************
                            Título: %s
                            Autor: %s
                            Idioma: %s
                            N° Descargas: %.2f%n
                            **************************************************
                            """, libroDTO.titulo(),

                    libroDTO.autores().stream().map(XautorDTO::nombre).collect(Collectors.joining(", ")),
                    libroDTO.idiomas(),
                    libroDTO.numeroDeDescargas()
            );
            System.out.println("--------------------------------------------------");
            pausa();
            return;
        }

        // Si el libro existe procesamos los autores
        List<Autor> autores = datosLibros.autor().stream()
                .map(datosAutor -> xautorRepository.findByNombre(datosAutor.nombre())
                        .orElseGet(() -> {
                            // Crear y guardar nuevo autor
                            Autor nuevoAutor = new Autor();
                            nuevoAutor.setNombre(datosAutor.nombre());
                            nuevoAutor.setFechaNacimiento(datosAutor.fechaNacimiento());
                            nuevoAutor.setFechaFallecimiento(datosAutor.fechaFallecimiento());
                            xautorRepository.save(nuevoAutor);
                            return nuevoAutor;
                        })
                ).collect(Collectors.toList());

        // Crear el libro con los datos de datosLibros y añadir los autores
        Libro libro = new Libro(datosLibros);
        libro.setAutores(autores);
        // Guardar el libro junto con sus autores en la base de datos
        xlibroRepository.save(libro);

        // Crear y mostrar DTO del libro guardado
        XlibroDTO libroDTO = new XlibroDTO(
                libro.getId(),
                libro.getTitulo(),
                libro.getAutores().stream().map(autor -> new XautorDTO(autor.getId(), autor.getNombre(), autor.getFechaNacimiento(), autor.getFechaFallecimiento()))
                        .collect(Collectors.toList()),
                String.join(", ", libro.getIdiomas()),
                libro.getNumeroDeDescargas()
        );

        // Imprimir detalles del libro registrado
        System.out.printf(
                """
                        
                        **************************************************
                        *                      LIBRO                     *
                        **************************************************
                        Título: %s
                        Autor: %s
                        Idioma: %s
                        N° Descargas: %.2f%n
                        **************************************************
                        """, libroDTO.titulo(),
                libroDTO.autores().stream().map(XautorDTO::nombre).collect(Collectors.joining(", ")),
                libroDTO.idiomas(),
                libroDTO.numeroDeDescargas()
        );
        System.out.println("--------------------------------------------------");
        pausa();
    }

    private DatosLibros getDatosLibros() {
        System.out.print("Ingresa el nombre del libro que deseas buscar: ");
        var nombreLibro = sc.nextLine();
        // Buscar libro en la API
        String json = consumoAPI.obtenerDatosLibros(URL_BASE + "?search=" + nombreLibro.replace(" ", "+")); // me trae un json
        // Convierto json a un objeto Java
        var datosBusqueda = conversor.obtenerDatos(json, Datos.class);

        // Encontrar el primer libro coincidente en la lista de resultados
        return datosBusqueda.listaResultados().stream()
                .filter(datosLibros -> datosLibros.titulo().toUpperCase().contains(nombreLibro.toUpperCase()))
                .findFirst()
                .orElse(null); // Devolver null si no se encuentra el libro
    }

    private void listarLibrosRegistrados() {
        libros = xlibroRepository.findAllWithAutores(); // uso una de las dos formas del LibroRepository
//        libros = libroRepository.findAll();

        if (libros.isEmpty()) {
            System.out.println("""
                    
                    **************************************************
                    No hay Libros registrados en el sistema.
                    **************************************************""");
            pausa();
            return;
        }
        System.out.printf("""
                
                **************************************************
                *            %d LIBROS REGISTRADOS               *
                **************************************************%n""", libros.size());
        mostrarLibros(libros);
        pausa();
    }

    private void listarAutoresRegistrados() {
        autores = xautorRepository.findAllWithLibros();
//        autores = autorRepository.findAll();

        if (autores.isEmpty()) {
            System.out.println("""
                    
                    **************************************************
                    No hay Autores registrados en el sistema.
                    **************************************************""");
            pausa();
            return;
        }
        System.out.printf("""
                
                **************************************************
                *            %d AUTORES REGISTRADOS              *
                **************************************************%n""", autores.size());
        mostrarAutores(autores);
        pausa();
    }

    private void listarAutoresVivosEnAnio() {
        var valorValido = false;
        String anioEstaVivo;
        do {
            System.out.print("Ingresa el año para buscar autores vivos en ese período: ");
            anioEstaVivo = sc.nextLine();

            // Validar que el año ingresado tenga 4 dígitos numéricos
            if (!validarAnio4Digitos(anioEstaVivo)) {

                continue;
            }
            valorValido = true;
        } while (!valorValido);

        int anio = Integer.parseInt(anioEstaVivo);

        // Obtener autores vivos en el año especificado
        List<Autor> autoresVivos = xautorRepository.findByFechaNacimientoBeforeAndFechaFallecimientoAfterOrFechaFallecimientoIsNullAndFechaNacimientoIsNotNull(String.valueOf(anio), String.valueOf(anio));

        // Filtrar autores con fechaNacimiento mayor a 100 años desde el año actual si fechaFallecimiento es null
        int anioActual = Year.now().getValue();

        autoresVivos = autoresVivos.stream()
                .filter(autor -> {
                    if (autor.getFechaFallecimiento() == null) {
                        int anioNacimiento = Integer.parseInt(autor.getFechaNacimiento());
                        return anioActual - anioNacimiento <= 100;
                    }
                    return true;
                }).collect(Collectors.toList());

        if (autoresVivos.isEmpty()) {
            System.out.println("""
                    
                    **************************************************
                    No se encontraron autores vivos en el año buscado.
                    **************************************************""");
            pausa();
        } else {
            System.out.printf("""
                    
                    **************************************************
                    *            %d AUTORES VIVOS EN %d               *
                    **************************************************%n""", autoresVivos.size(), anio);
            mostrarAutores(autoresVivos);
            pausa();
        }
    }

    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>COPIAR AL TXT DESDE ACA 1/11/24<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    private void listarLibrosPorIdioma() {
        var menuIdiomas = """
                
                **************************************************
                *               LIBROS POR IDIOMA                *
                **************************************************
                es - Español                it - Italiano
                en - Inglés                 ja - Japonés
                fr - Francés                pt - Portugués
                ru - Ruso                   zh - Chino Mandarín
                de - Alemán                 ar - Árabe
                """;
        String idiomaLibro;
        do {
            System.out.println(menuIdiomas);
            System.out.print("Ingresa el código del idioma del Libro a buscar [2 letras, ej: es]: ");
            idiomaLibro = sc.nextLine().toLowerCase();

            // Validar que el idioma ingresado tenga dos letras y no incluya números
            if (!idiomaLibro.matches("^[a-z]{2}$")) {
                System.out.println("""
                        
                        **************************************************
                        Código de idioma no válido. Debe ser un código de 2 letras.
                        **************************************************""");
            }
        } while (!idiomaLibro.matches("^[a-z]{2}$"));

        // Lista de libros en idioma buscado
        List<Libro> librosPorIdioma = xlibroRepository.findByIdiomasContaining(idiomaLibro);

        if (librosPorIdioma.isEmpty()) {
            System.out.println("""
                    
                    **************************************************
                    No se encontraron Libros en el Idioma buscado.
                    **************************************************""");
            pausa();
        } else {
            if (librosPorIdioma.size() == 1) {
                System.out.printf("""
                        
                        **************************************************
                        *           %d LIBRO EN EL IDIOMA '%s'            *
                        **************************************************%n""", librosPorIdioma.size(), idiomaLibro.toUpperCase());
            } else {
                System.out.printf("""
                        
                        **************************************************
                        *           %d LIBROS EN EL IDIOMA '%s'           *
                        **************************************************%n""", librosPorIdioma.size(), idiomaLibro.toUpperCase());
            }
            mostrarLibros(librosPorIdioma);
            pausa();
        }
    }

    private void mostrarLibros(List<Libro> libroList) {
        for (Libro libro : libroList) {
            List<XautorDTO> autoresDTO = libro.getAutores().stream()
                    .map(autor -> new XautorDTO(autor.getId(), autor.getNombre(), autor.getFechaNacimiento(), autor.getFechaFallecimiento()))
                    .collect(Collectors.toList());

            // Crear el DTO para mostrar solo la información necesaria
            XlibroDTO libroDTO = new XlibroDTO(
                    libro.getId(),
                    libro.getTitulo(),
                    autoresDTO,
                    String.join(", ", libro.getIdiomas()),
                    libro.getNumeroDeDescargas()
            );

            System.out.printf(
                    """
                            
                            **************************************************
                            *                      LIBRO                     *
                            **************************************************
                            Título: %s
                            Autor: %s
                            Idioma: %s
                            N° Descargas: %.2f%n""", libroDTO.titulo(),
                    libroDTO.autores().stream().map(XautorDTO::nombre).collect(Collectors.joining(", ")),
                    String.join(", ", libro.getIdiomas()),
                    libroDTO.numeroDeDescargas()
            );
            System.out.println("--------------------------------------------------");

        }
    }

    private void mostrarAutores(List<Autor> autoresList) {
        for (Autor autor : autoresList) {
            List<String> librosDelAutor = autor.getLibrosDelAutor().stream()
                    .map(Libro::getTitulo)
                    .collect(Collectors.toList());

            XautorDTO autorDTO = new XautorDTO(
                    autor.getId(),
                    autor.getNombre(),
                    autor.getFechaNacimiento(),
                    autor.getFechaFallecimiento()
            );

            // Mostrar la información en el formato solicitado
            System.out.printf(
                    """
                            
                            **************************************************
                            *                      AUTOR                     *
                            **************************************************
                            Autor: %s
                            Fecha de Nacimiento: %s
                            Fecha de Fallecimiento: %s
                            Libros: %s%n""", autorDTO.nombre(),
                    autorDTO.fechaNacimiento() != null ? autorDTO.fechaNacimiento() : "N/A",
                    autorDTO.fechaFallecimiento() != null ? autorDTO.fechaFallecimiento() : "N/A",
                    librosDelAutor
            );
            System.out.println("--------------------------------------------------");
        }
    }

    private boolean validarAnio4Digitos(String anio) {
        // Validar que el año ingresado tenga 4 dígitos numéricos
        return anio.matches("\\d{4}");
    }

    private void pausa() {
        System.out.println("\nPresione 'Enter' para continuar...");
        sc.nextLine();
    }



}
