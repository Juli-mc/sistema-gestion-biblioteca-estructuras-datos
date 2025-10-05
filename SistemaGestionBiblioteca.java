import java.util.*;

// Clase Libro con la parametrización de datos definida
class Libro {
    String isbn;
    String titulo;
    String autor;
    int anioPublicacion;
    boolean disponible;

    // Constructor
    public Libro(String isbn, String titulo, String autor, int anioPublicacion) {
        this.isbn = isbn;
        this.titulo = titulo;
        this.autor = autor;
        this.anioPublicacion = anioPublicacion;
        this.disponible = true; // Por defecto, un libro está disponible al crearse
    }

    @Override
    public String toString() {
        String estado = disponible ? "Disponible" : "Prestado";
        return "ISBN: " + isbn + " | Título: " + titulo + " | Autor: " + autor + " | Año: " + anioPublicacion + " | Estado: " + estado;
    }
}

// Clase Usuario con la parametrización de datos definida
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

// Clase principal Biblioteca que orchesta el sistema
public class SistemaGestionBiblioteca {

    // Estructuras de datos lineales definidas en el diseño
    private List<Libro> catalogoLibros; // Lista para el catálogo
    private Queue<Usuario> colaReservas; // Cola para reservas (simplificada a una cola global)
    private Stack<String> historialAcciones; // Pila para el historial

    public SistemaGestionBiblioteca() {
        catalogoLibros = new ArrayList<>(); // Se elige ArrayList para el catálogo
        colaReservas = new LinkedList<>(); // LinkedList implementa la interfaz Queue
        historialAcciones = new Stack<>(); // Pila para el historial
    }

    // Método para agregar un libro al catálogo (Lista)
    public void agregarLibro(String isbn, String titulo, String autor, int anio) {
        Libro nuevoLibro = new Libro(isbn, titulo, autor, anio);
        catalogoLibros.add(nuevoLibro);
        String accion = "Libro agregado: " + titulo + " (" + isbn + ")";
        historialAcciones.push(accion); // Registro en el historial (Pila)
        System.out.println(accion);
    }

    // Método para buscar un libro por ISBN (Recorrido de Lista)
    public Libro buscarLibroPorIsbn(String isbn) {
        for (Libro libro : catalogoLibros) {
            if (libro.isbn.equals(isbn)) {
                return libro;
            }
        }
        return null; // No encontrado
    }

    // Método para listar todos los libros (Recorrido de Lista)
    public void listarLibros() {
        if (catalogoLibros.isEmpty()) {
            System.out.println("El catálogo está vacío.");
            return;
        }
        System.out.println("\n--- CATÁLOGO COMPLETO DE LIBROS ---");
        for (Libro libro : catalogoLibros) {
            System.out.println(libro);
        }
    }

    // Método para registrar un usuario
    public void registrarUsuario(String id, String nombre, String correo) {
        // En una versión más compleja, se guardaría en una lista de usuarios registrados
        String accion = "Usuario registrado: " + nombre + " (" + id + ")";
        historialAcciones.push(accion);
        System.out.println(accion);
    }

    // Método para prestar un libro
    public void prestarLibro(String isbn, String idUsuario) {
        Libro libro = buscarLibroPorIsbn(isbn);
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
            // En una cola por libro, se añadiría el usuario a la cola específica
            // Para este ejemplo, se añade a una cola global simplificada
            Usuario usuarioEnEspera = new Usuario(idUsuario, "Cliente " + idUsuario, "correo@ejemplo.com");
            colaReservas.add(usuarioEnEspera);
            System.out.println("Usuario " + idUsuario + " añadido a la lista de espera.");
        }
    }

    // Método para devolver un libro
    public void devolverLibro(String isbn) {
        Libro libro = buscarLibroPorIsbn(isbn);
        if (libro == null) {
            System.out.println("Error: El libro con ISBN " + isbn + " no existe en el catálogo.");
            return;
        }
        if (!libro.disponible) {
            libro.disponible = true;
            String accion = "Libro devuelto: " + libro.titulo;
            historialAcciones.push(accion);
            System.out.println(accion);

            // Notificar al siguiente usuario en la cola de reservas (si existe)
            if (!colaReservas.isEmpty()) {
                Usuario siguienteUsuario = colaReservas.poll(); // FIFO: Se extrae el primero
                System.out.println("¡Atención! Libro disponible para el usuario: " + siguienteUsuario.idUsuario);
            }
        } else {
            System.out.println("El libro ya estaba disponible.");
        }
    }

    // Método para mostrar el historial reciente (Pila)
    public void mostrarHistorialReciente() {
        if (historialAcciones.isEmpty()) {
            System.out.println("El historial está vacío.");
            return;
        }
        System.out.println("\n--- ÚLTIMAS 5 ACCIONES ---");
        // Se muestran las 5 acciones más recientes (LIFO)
        int count = 0;
        Stack<String> temp = new Stack<>();
        temp.addAll(historialAcciones); // Copia para no alterar la pila original

        while (!temp.isEmpty() && count < 5) {
            System.out.println("- " + temp.pop());
            count++;
        }
    }

    // Método para mostrar la cola de reservas
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

    // INTERFAZ DE USUARIO por consola
    public void mostrarMenu() {
        Scanner scanner = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("\n\n=== SISTEMA DE GESTIÓN DE BIBLIOTECA ===");
            System.out.println("1. Agregar nuevo libro");
            System.out.println("2. Listar todos los libros");
            System.out.println("3. Prestar un libro");
            System.out.println("4. Devolver un libro");
            System.out.println("5. Registrar nuevo usuario");
            System.out.println("6. Ver lista de espera (Reservas)");
            System.out.println("7. Ver historial reciente");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");

            opcion = scanner.nextInt();
            scanner.nextLine(); // Consumir el salto de línea

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
                case 0:
                    System.out.println("Saliendo del sistema...");
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        } while (opcion != 0);
        scanner.close();
    }

    // Método principal para ejecutar el sistema
    public static void main(String[] args) {
        SistemaGestionBiblioteca biblioteca = new SistemaGestionBiblioteca();
        biblioteca.mostrarMenu();
    }
}