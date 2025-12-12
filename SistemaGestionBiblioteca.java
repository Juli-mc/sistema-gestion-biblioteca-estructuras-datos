import java.util.*;

// === CLASE LIBRO ===
class Libro {
    String isbn;
    String titulo;
    String autor;
    int anioPublicacion;
    boolean disponible;

    public Libro(String isbn, String titulo, String autor, int anioPublicacion) {
        this.isbn = isbn;
        this.titulo = titulo;
        this.autor = autor;
        this.anioPublicacion = anioPublicacion;
        this.disponible = true;
    }

    @Override
    public String toString() {
        String estado = disponible ? "Disponible" : "Prestado";
        return "ISBN: " + isbn + " | Título: " + titulo + " | Autor: " + autor + " | Año: " + anioPublicacion + " | Estado: " + estado;
    }
}

// === CLASE USUARIO ===
class Usuario {
    String idUsuario;
    String nombre;
    String correo;

    public Usuario(String idUsuario, String nombre, String correo) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.correo = correo;
    }

    @Override
    public String toString() {
        return "ID: " + idUsuario + " | Nombre: " + nombre + " | Correo: " + correo;
    }
}

// === CLASE NODO ÁRBOL ===
class NodoArbol {
    String isbn;
    Libro libro;
    NodoArbol izquierdo;
    NodoArbol derecho;
    
    public NodoArbol(String isbn, Libro libro) {
        this.isbn = isbn;
        this.libro = libro;
        this.izquierdo = null;
        this.derecho = null;
    }
}

// === CLASE ÁRBOL BINARIO DE BÚSQUEDA ===
class ArbolBinarioBusqueda {
    private NodoArbol raiz;
    
    public ArbolBinarioBusqueda() {
        this.raiz = null;
    }
    
    public void insertar(String isbn, Libro libro) {
        raiz = insertarRecursivo(raiz, isbn, libro);
    }
    
    private NodoArbol insertarRecursivo(NodoArbol nodo, String isbn, Libro libro) {
        if (nodo == null) {
            return new NodoArbol(isbn, libro);
        }
        
        int comparacion = isbn.compareTo(nodo.isbn);
        
        if (comparacion < 0) {
            nodo.izquierdo = insertarRecursivo(nodo.izquierdo, isbn, libro);
        } else if (comparacion > 0) {
            nodo.derecho = insertarRecursivo(nodo.derecho, isbn, libro);
        }
        
        return nodo;
    }
    
    public Libro buscar(String isbn) {
        return buscarRecursivo(raiz, isbn);
    }
    
    private Libro buscarRecursivo(NodoArbol nodo, String isbn) {
        if (nodo == null) return null;
        
        int comparacion = isbn.compareTo(nodo.isbn);
        
        if (comparacion == 0) return nodo.libro;
        else if (comparacion < 0) return buscarRecursivo(nodo.izquierdo, isbn);
        else return buscarRecursivo(nodo.derecho, isbn);
    }
    
    public void recorrerEnOrden() {
        recorrerEnOrdenRecursivo(raiz);
    }
    
    private void recorrerEnOrdenRecursivo(NodoArbol nodo) {
        if (nodo != null) {
            recorrerEnOrdenRecursivo(nodo.izquierdo);
            System.out.println(nodo.libro);
            recorrerEnOrdenRecursivo(nodo.derecho);
        }
    }
    
    public boolean estaVacio() {
        return raiz == null;
    }
}

// === CLASE ARISTA DEL GRAFO ===
class AristaRecomendacion {
    String isbnLibroDestino;
    int peso;

    public AristaRecomendacion(String isbnLibroDestino) {
        this.isbnLibroDestino = isbnLibroDestino;
        this.peso = 1;
    }

    public void incrementarPeso() {
        this.peso++;
    }
}

// === CLASE GRAFO DE RECOMENDACIONES ===
class GrafoRecomendaciones {
    private Map<String, List<AristaRecomendacion>> listaAdyacencia;

    public GrafoRecomendaciones() {
        this.listaAdyacencia = new HashMap<>();
    }

    public void registrarPrestamoConjunto(String isbnLibro1, String isbnLibro2) {
        if (isbnLibro1.equals(isbnLibro2)) return;
        
        agregarArista(isbnLibro1, isbnLibro2);
        agregarArista(isbnLibro2, isbnLibro1);
    }

    private void agregarArista(String origen, String destino) {
        listaAdyacencia.putIfAbsent(origen, new ArrayList<>());
        List<AristaRecomendacion> conexiones = listaAdyacencia.get(origen);

        for (AristaRecomendacion arista : conexiones) {
            if (arista.isbnLibroDestino.equals(destino)) {
                arista.incrementarPeso();
                return;
            }
        }
        conexiones.add(new AristaRecomendacion(destino));
    }

    public List<String> obtenerRecomendaciones(String isbnLibroBase) {
        List<String> recomendaciones = new ArrayList<>();

        if (!listaAdyacencia.containsKey(isbnLibroBase)) {
            return recomendaciones;
        }

        List<AristaRecomendacion> conexiones = listaAdyacencia.get(isbnLibroBase);
        conexiones.sort((a1, a2) -> Integer.compare(a2.peso, a1.peso));

        for (AristaRecomendacion arista : conexiones) {
            recomendaciones.add(arista.isbnLibroDestino);
        }

        return recomendaciones;
    }
}

// === CLASE PRINCIPAL DEL SISTEMA ===
public class SistemaGestionBiblioteca {
    private ArbolBinarioBusqueda catalogoArbol;
    private Queue<Usuario> colaReservas;
    private Stack<String> historialAcciones;
    private GrafoRecomendaciones grafoRecomendaciones;
    private Map<String, List<String>> prestamosPorUsuario;

    public SistemaGestionBiblioteca() {
        catalogoArbol = new ArbolBinarioBusqueda();
        colaReservas = new LinkedList<>();
        historialAcciones = new Stack<>();
        grafoRecomendaciones = new GrafoRecomendaciones();
        prestamosPorUsuario = new HashMap<>();
    }
    
    public void agregarLibro(String isbn, String titulo, String autor, int anio) {
        Libro nuevoLibro = new Libro(isbn, titulo, autor, anio);
        catalogoArbol.insertar(isbn, nuevoLibro);
        String accion = "Libro agregado: " + titulo + " (" + isbn + ")";
        historialAcciones.push(accion);
        System.out.println(accion);
    }
    
    public Libro buscarLibroPorIsbn(String isbn) {
        return catalogoArbol.buscar(isbn);
    }
    
    public void listarLibros() {
        if (catalogoArbol.estaVacio()) {
            System.out.println("El catálogo está vacío.");
            return;
        }
        System.out.println("\n--- CATÁLOGO COMPLETO (Ordenado por ISBN) ---");
        catalogoArbol.recorrerEnOrden();
    }
    
    public void registrarUsuario(String id, String nombre, String correo) {
        String accion = "Usuario registrado: " + nombre + " (" + id + ")";
        historialAcciones.push(accion);
        System.out.println(accion);
    }
    
    public void prestarLibro(String isbn, String idUsuario) {
        Libro libro = catalogoArbol.buscar(isbn);
        if (libro == null) {
            System.out.println("Error: Libro no encontrado.");
            return;
        }
        if (libro.disponible) {
            libro.disponible = false;
            
            // 1. Historial
            String accion = "Préstamo: " + libro.titulo + " -> " + idUsuario;
            historialAcciones.push(accion);
            
            // 2. Actualizar grafo
            actualizarGrafoRecomendaciones(isbn, idUsuario);
            
            System.out.println("Préstamo registrado: " + libro.titulo);
        } else {
            System.out.println("Libro no disponible. Usuario en cola de espera.");
            colaReservas.add(new Usuario(idUsuario, "Cliente", "correo@ejemplo.com"));
        }
    }
    
    private void actualizarGrafoRecomendaciones(String nuevoLibroIsbn, String idUsuario) {
        prestamosPorUsuario.putIfAbsent(idUsuario, new ArrayList<>());
        List<String> librosDelUsuario = prestamosPorUsuario.get(idUsuario);

        for (String isbnLibroAnterior : librosDelUsuario) {
            grafoRecomendaciones.registrarPrestamoConjunto(isbnLibroAnterior, nuevoLibroIsbn);
        }
        librosDelUsuario.add(nuevoLibroIsbn);
    }
    
    public void devolverLibro(String isbn) {
        Libro libro = catalogoArbol.buscar(isbn);
        if (libro == null) {
            System.out.println("Error: Libro no encontrado.");
            return;
        }
        if (!libro.disponible) {
            libro.disponible = true;
            String accion = "Libro devuelto: " + libro.titulo;
            historialAcciones.push(accion);
            System.out.println(accion);
        } else {
            System.out.println("El libro ya estaba disponible.");
        }
    }
    
    public void mostrarRecomendaciones(String isbn) {
        Libro libroBase = catalogoArbol.buscar(isbn);
        if (libroBase == null) {
            System.out.println("Libro no encontrado.");
            return;
        }

        System.out.println("\n--- LIBROS RECOMENDADOS para: " + libroBase.titulo + " ---");
        List<String> isbnsRecomendados = grafoRecomendaciones.obtenerRecomendaciones(isbn);

        if (isbnsRecomendados.isEmpty()) {
            System.out.println("Aún no hay suficientes datos para recomendaciones.");
            return;
        }

        for (String isbnRecomendado : isbnsRecomendados) {
            Libro libroRec = catalogoArbol.buscar(isbnRecomendado);
            if (libroRec != null) {
                System.out.println("- " + libroRec.titulo + " (ISBN: " + libroRec.isbn + ")");
            }
        }
    }
    
    public void mostrarHistorialReciente() {
        if (historialAcciones.isEmpty()) {
            System.out.println("El historial está vacío.");
            return;
        }
        System.out.println("\n--- ÚLTIMAS 5 ACCIONES ---");
        int count = 0;
        Stack<String> temp = new Stack<>();
        temp.addAll(historialAcciones);

        while (!temp.isEmpty() && count < 5) {
            System.out.println("- " + temp.pop());
            count++;
        }
    }
    
    public void mostrarColaReservas() {
        if (colaReservas.isEmpty()) {
            System.out.println("No hay usuarios en espera.");
            return;
        }
        System.out.println("\n--- USUARIOS EN LISTA DE ESPERA ---");
        for (Usuario usuario : colaReservas) {
            System.out.println(usuario);
        }
    }
    
    public void mostrarMenu() {
        Scanner scanner = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("\n\n=== SISTEMA DE GESTIÓN DE BIBLIOTECA ===");
            System.out.println("1. Agregar nuevo libro");
            System.out.println("2. Listar todos los libros (Ordenado por ISBN)");
            System.out.println("3. Prestar un libro");
            System.out.println("4. Devolver un libro");
            System.out.println("5. Registrar nuevo usuario");
            System.out.println("6. Ver lista de espera (Reservas)");
            System.out.println("7. Ver historial reciente");
            System.out.println("8. Obtener recomendaciones de libros"); // NUEVA OPCIÓN
            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");

            opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1:
                    System.out.print("ISBN: ");
                    String isbn = scanner.nextLine();
                    System.out.print("Título: ");
                    String titulo = scanner.nextLine();
                    System.out.print("Autor: ");
                    String autor = scanner.nextLine();
                    System.out.print("Año: ");
                    int anio = scanner.nextInt();
                    agregarLibro(isbn, titulo, autor, anio);
                    break;
                case 2:
                    listarLibros();
                    break;
                case 3:
                    System.out.print("ISBN del libro a prestar: ");
                    String isbnPrestamo = scanner.nextLine();
                    System.out.print("ID de usuario: ");
                    String idUsuarioPrestamo = scanner.nextLine();
                    prestarLibro(isbnPrestamo, idUsuarioPrestamo);
                    break;
                case 4:
                    System.out.print("ISBN del libro a devolver: ");
                    String isbnDevolucion = scanner.nextLine();
                    devolverLibro(isbnDevolucion);
                    break;
                case 5:
                    System.out.print("ID de usuario: ");
                    String id = scanner.nextLine();
                    System.out.print("Nombre: ");
                    String nombre = scanner.nextLine();
                    System.out.print("Correo: ");
                    String correo = scanner.nextLine();
                    registrarUsuario(id, nombre, correo);
                    break;
                case 6:
                    mostrarColaReservas();
                    break;
                case 7:
                    mostrarHistorialReciente();
                    break;
                case 8:
                    System.out.print("ISBN del libro base: ");
                    String isbnRecomendacion = scanner.nextLine();
                    mostrarRecomendaciones(isbnRecomendacion);
                    break;
                case 0:
                    System.out.println("Saliendo del sistema...");
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        } while (opcion != 0);
        scanner.close();
    }

    // === MÉTODO MAIN CORRECTO ===
    public static void main(String[] args) {
        SistemaGestionBiblioteca biblioteca = new SistemaGestionBiblioteca();
        biblioteca.mostrarMenu();
    }
}