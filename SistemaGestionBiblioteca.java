import java.util.*;

// Clase Libro (se mantiene igual)
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

// Clase Usuario (se mantiene igual)
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

// NUEVA CLASE: NodoArbol
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
    
    public String getIsbn() { return isbn; }
    public Libro getLibro() { return libro; }
    public NodoArbol getIzquierdo() { return izquierdo; }
    public NodoArbol getDerecho() { return derecho; }
    public void setIzquierdo(NodoArbol izquierdo) { this.izquierdo = izquierdo; }
    public void setDerecho(NodoArbol derecho) { this.derecho = derecho; }
}

// NUEVA CLASE: ArbolBinarioBusqueda
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
        
        int comparacion = isbn.compareTo(nodo.getIsbn());
        
        if (comparacion < 0) {
            nodo.setIzquierdo(insertarRecursivo(nodo.getIzquierdo(), isbn, libro));
        } else if (comparacion > 0) {
            nodo.setDerecho(insertarRecursivo(nodo.getDerecho(), isbn, libro));
        }
        
        return nodo;
    }
    
    public Libro buscar(String isbn) {
        return buscarRecursivo(raiz, isbn);
    }
    
    private Libro buscarRecursivo(NodoArbol nodo, String isbn) {
        if (nodo == null) {
            return null;
        }
        
        int comparacion = isbn.compareTo(nodo.getIsbn());
        
        if (comparacion == 0) {
            return nodo.getLibro();
        } else if (comparacion < 0) {
            return buscarRecursivo(nodo.getIzquierdo(), isbn);
        } else {
            return buscarRecursivo(nodo.getDerecho(), isbn);
        }
    }
    
    public void recorrerEnOrden() {
        recorrerEnOrdenRecursivo(raiz);
    }
    
    private void recorrerEnOrdenRecursivo(NodoArbol nodo) {
        if (nodo != null) {
            recorrerEnOrdenRecursivo(nodo.getIzquierdo());
            System.out.println(nodo.getLibro());
            recorrerEnOrdenRecursivo(nodo.getDerecho());
        }
    }
    
    public boolean estaVacio() {
        return raiz == null;
    }
    
    public int contarLibros() {
        return contarRecursivo(raiz);
    }
    
    private int contarRecursivo(NodoArbol nodo) {
        if (nodo == null) {
            return 0;
        }
        return 1 + contarRecursivo(nodo.getIzquierdo()) + contarRecursivo(nodo.getDerecho());
    }
}

// CLASE PRINCIPAL MODIFICADA
public class SistemaGestionBiblioteca {
    private ArbolBinarioBusqueda catalogoArbol; // ← REEMPLAZADO: Lista por Árbol
    private Queue<Usuario> colaReservas;
    private Stack<String> historialAcciones;
    
    public SistemaGestionBiblioteca() {
        catalogoArbol = new ArbolBinarioBusqueda(); // ← INICIALIZACIÓN DEL ÁRBOL
        colaReservas = new LinkedList<>();
        historialAcciones = new Stack<>();
    }
    
    // MÉTODOS MODIFICADOS PARA USAR EL ÁRBOL
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
        System.out.println("\n--- CATÁLOGO COMPLETO DE LIBROS (Ordenado por ISBN) ---");
        catalogoArbol.recorrerEnOrden();
    }
    
    // MÉTODO NUEVO: Estadísticas del árbol
    public void mostrarEstadisticas() {
        System.out.println("\n--- ESTADÍSTICAS DEL CATÁLOGO ---");
        System.out.println("Total de libros en el sistema: " + catalogoArbol.contarLibros());
    }
    
    // LOS SIGUIENTES MÉTODOS SE MANTIENEN IGUAL (pero ahora son más eficientes)
    public void registrarUsuario(String id, String nombre, String correo) {
        String accion = "Usuario registrado: " + nombre + " (" + id + ")";
        historialAcciones.push(accion);
        System.out.println(accion);
    }
    
    public void prestarLibro(String isbn, String idUsuario) {
        Libro libro = buscarLibroPorIsbn(isbn); // ← Ahora usa búsqueda en árbol O(log n)
        if (libro == null) {
            System.out.println("Error: El libro con ISBN " + isbn + " no existe en el catálogo.");
            return;
        }
        if (libro.disponible) {
            libro.disponible = false;
            String accion = "Libro prestado: " + libro.titulo + " a usuario " + idUsuario;
            historialAcciones.push(accion);
            System.out.println(accion);
        } else {
            System.out.println("El libro no está disponible. Se le notificará cuando esté libre.");
            Usuario usuarioEnEspera = new Usuario(idUsuario, "Cliente " + idUsuario, "correo@ejemplo.com");
            colaReservas.add(usuarioEnEspera);
            System.out.println("Usuario " + idUsuario + " añadido a la lista de espera.");
        }
    }
    
    public void devolverLibro(String isbn) {
        Libro libro = buscarLibroPorIsbn(isbn); // ← Ahora usa búsqueda en árbol O(log n)
        if (libro == null) {
            System.out.println("Error: El libro con ISBN " + isbn + " no existe en el catálogo.");
            return;
        }
        if (!libro.disponible) {
            libro.disponible = true;
            String accion = "Libro devuelto: " + libro.titulo;
            historialAcciones.push(accion);
            System.out.println(accion);

            if (!colaReservas.isEmpty()) {
                Usuario siguienteUsuario = colaReservas.poll();
                System.out.println("¡Atención! Libro disponible para el usuario: " + siguienteUsuario.idUsuario);
            }
        } else {
            System.out.println("El libro ya estaba disponible.");
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
        System.out.println("\n--- USUARIOS EN LISTA DE ESPERA (por orden de llegada) ---");
        for (Usuario usuario : colaReservas) {
            System.out.println(usuario);
        }
    }
    
    // INTERFAZ DE USUARIO ACTUALIZADA
    public void mostrarMenu() {
        Scanner scanner = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("\n\n=== SISTEMA DE GESTIÓN DE BIBLIOTECA (CON ÁRBOL BST) ===");
            System.out.println("1. Agregar nuevo libro");
            System.out.println("2. Listar todos los libros (Ordenado por ISBN)");
            System.out.println("3. Prestar un libro");
            System.out.println("4. Devolver un libro");
            System.out.println("5. Registrar nuevo usuario");
            System.out.println("6. Ver lista de espera (Reservas)");
            System.out.println("7. Ver historial reciente");
            System.out.println("8. Mostrar estadísticas del catálogo"); // ← NUEVA OPCIÓN
            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");

            opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1:
                    System.out.print("Ingrese ISBN: ");
                    String isbn = scanner.nextLine();
                    System.out.print("Ingrese Título: ");
                    String titulo = scanner.nextLine();
                    System.out.print("Ingrese Autor: ");
                    String autor = scanner.nextLine();
                    System.out.print("Ingrese Año: ");
                    int anio = scanner.nextInt();
                    agregarLibro(isbn, titulo, autor, anio);
                    break;
                case 2:
                    listarLibros();
                    break;
                case 3:
                    System.out.print("Ingrese ISBN del libro a prestar: ");
                    String isbnPrestamo = scanner.nextLine();
                    System.out.print("Ingrese su ID de usuario: ");
                    String idUsuarioPrestamo = scanner.nextLine();
                    prestarLibro(isbnPrestamo, idUsuarioPrestamo);
                    break;
                case 4:
                    System.out.print("Ingrese ISBN del libro a devolver: ");
                    String isbnDevolucion = scanner.nextLine();
                    devolverLibro(isbnDevolucion);
                    break;
                case 5:
                    System.out.print("Ingrese ID de usuario: ");
                    String id = scanner.nextLine();
                    System.out.print("Ingrese Nombre: ");
                    String nombre = scanner.nextLine();
                    System.out.print("Ingrese Correo: ");
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
                    mostrarEstadisticas();
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

    public static void main(String[] args) {
        SistemaGestionBiblioteca biblioteca = new SistemaGestionBiblioteca();
        biblioteca.mostrarMenu();
    }
}