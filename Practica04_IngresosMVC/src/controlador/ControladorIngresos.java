package controlador;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import modelo.Aportador;
import modelo.AnalizadorIngresos;
import vista.VistaConsola;

// Coordina el menú, el modelo y la vista.
public class ControladorIngresos {
    private final String archivo = "registro_ingresos.txt";
    private AnalizadorIngresos analizador = new AnalizadorIngresos();
    private VistaConsola vista = new VistaConsola();
    private ArrayList<Aportador> registros = new ArrayList<Aportador>();
    private Scanner teclado = new Scanner(System.in);

    public void iniciar() {
        cargarArchivo();
        String opcion = "";
        while (!opcion.equals("6")) {
            vista.mostrarMenu();
            opcion = teclado.nextLine();
            if (opcion.equals("1")) { cargarArchivo(); mostrarTodos(); mostrarResumen(); }
            else if (opcion.equals("2")) mostrarValidos();
            else if (opcion.equals("3")) mostrarErrores();
            else if (opcion.equals("4")) mostrarResumen();
            else if (opcion.equals("5")) reemplazarTexto();
            else if (!opcion.equals("6")) vista.mostrarMensaje("Opción no válida. Intente otra vez.");
        }
        vista.mostrarMensaje("Programa finalizado.");
    }

    private void cargarArchivo() {
        try {
            registros = analizador.leerArchivo(archivo);
            vista.mostrarMensaje("Se leyeron " + registros.size() + " registros del archivo.");
        } catch (FileNotFoundException error) {
            registros = new ArrayList<Aportador>();
            vista.mostrarMensaje("No se encontró " + archivo + ". Revise que esté en la carpeta del proyecto.");
        }
    }

    private void mostrarTodos() { vista.mostrarLista(registros); }

    private void mostrarValidos() {
        ArrayList<Aportador> validos = new ArrayList<Aportador>();
        for (Aportador a : registros) if (a.esValido()) validos.add(a);
        vista.mostrarLista(validos);
    }

    private void mostrarErrores() {
        ArrayList<Aportador> incorrectos = new ArrayList<Aportador>();
        for (Aportador a : registros) if (!a.esValido()) incorrectos.add(a);
        vista.mostrarLista(incorrectos);
    }

    private void mostrarResumen() {
        int erroresCedula = 0, erroresCorreo = 0, erroresMonto = 0;
        double totalValido = 0;
        for (Aportador a : registros) {
            if (!a.isCedulaValida()) erroresCedula++;
            if (!a.isCorreoValido()) erroresCorreo++;
            if (!a.isMontoValido()) erroresMonto++;
            if (a.esValido()) totalValido += a.getMonto();
        }
        vista.mostrarResumen(registros.size(), erroresCedula, erroresCorreo, erroresMonto, totalValido);
    }

    private void reemplazarTexto() {
        System.out.println("Ejemplo: la cédula de Juan Carlos Pérez es 1723456789.");
        System.out.print("Cédula que desea buscar: ");
        String cedulaBuscar = teclado.nextLine();
        System.out.print("Cédula nueva: ");
        String cedulaNueva = teclado.nextLine();
        try {
            boolean cambiado = analizador.reemplazarTexto(archivo, cedulaBuscar, cedulaNueva);
            if (cambiado) vista.mostrarMensaje("Se encontró la cédula y se guardó una copia modificada.");
            else vista.mostrarMensaje("No se encontró esa cédula. No se creó una copia.");
        } catch (FileNotFoundException error) {
            vista.mostrarMensaje("No se encontró el archivo original.");
        }
    }
}
