# Sistema de Gestión de Biblioteca - Estructuras de Datos Lineales

Prototipo funcional de un sistema de gestión para una biblioteca, desarrollado en Java como proyecto académico. Su objetivo es demostrar la aplicación práctica de las **estructuras de datos lineales** (Listas, Pilas y Colas) en un contexto real.

## 🏗️ Estructuras Implementadas

*   **Lista (`ArrayList<Libro>`):** Para gestionar el catálogo dinámico de libros.
*   **Cola (`LinkedList<Usuario>`):** Para administrar una lista de espera justa (FIFO) para las reservas de libros.
*   **Pila (`Stack<String>`):** Para mantener un historial de las acciones más recientes (LIFO).

## ⚙️ Funcionalidades

*   Agregar y listar libros.
*   Registrar usuarios.
*   Prestar y devolver libros.
*   Gestionar una cola de reservas.
*   Visualizar un historial de acciones recientes.

## 🚀 Cómo Ejecutar

1.  Asegúrate de tener Java instalado.
2.  Clona el repositorio o descarga el archivo `SistemaGestionBiblioteca.java`.
3.  Compila el código:
    ```bash
    javac SistemaGestionBiblioteca.java
    ```
4.  Ejecuta el programa:
    ```bash
    java SistemaGestionBiblioteca
    ```

## 📁 Estructura del Proyecto

El proyecto consiste en un único archivo que contiene las clases:
- `Libro`: Modela los datos de un libro.
- `Usuario`: Modela los datos de un usuario.
- `SistemaGestionBiblioteca`: Clase principal que contiene la lógica del negocio y la interfaz de consola.

## 📚 Contexto Académico

Este proyecto corresponde a la primera etapa del curso de Estructuras de Datos, centrándose en la correcta selección, implementación y comunicación del uso de estructuras lineales congruentemente con los requisitos de un problema dado.

---
*Desarrollado como parte del programa de [Nombre de tu Carrera] en [Nombre de tu Universidad].*