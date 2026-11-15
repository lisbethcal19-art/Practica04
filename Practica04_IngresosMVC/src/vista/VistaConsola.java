package vista;

import java.util.ArrayList;
import modelo.Aportador;

// RF-04, RF-05: presenta opciones y resultados en la consola.
public class VistaConsola {
    public void mostrarMenu() {
        System.out.println();
        System.out.println("=== REGISTRO DE INGRESOS ===");
        System.out.println("1. Leer y analizar el archivo");
        System.out.println("2. Mostrar registros válidos");
        System.out.println("3. Mostrar registros con errores");
        System.out.println("4. Mostrar resumen");
        System.out.println("5. Buscar una cédula y cambiarla en una copia");
        System.out.println("6. Salir");
        System.out.print("Elija una opción: ");
    }

    public void mostrarLista(ArrayList<Aportador> lista) {
        if (lista.size() == 0) {
            System.out.println("No hay registros para mostrar.");
            return;
        }
        System.out.println("CÉDULA | CORREO | MONTO | VALIDACIÓN");
        for (int i = 0; i < lista.size(); i++) {
            Aportador a = lista.get(i);
            System.out.println(a.getCedula() + " | " + a.getCorreo() + " | $"
                    + String.format("%.2f", a.getMonto()) + " | " + a.getErrores());
        }
    }

    public void mostrarResumen(int cantidad, int erroresCedula, int erroresCorreo,
            int erroresMonto, double totalValido) {
        System.out.println();
        System.out.println("=== RESUMEN ===");
        System.out.println("Registros leídos: " + cantidad);
        System.out.println("Cédulas incorrectas: " + erroresCedula);
        System.out.println("Correos incorrectos: " + erroresCorreo);
        System.out.println("Montos incorrectos: " + erroresMonto);
        System.out.println("Total de ingresos válidos: $" + String.format("%.2f", totalValido));
    }

    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }
}

