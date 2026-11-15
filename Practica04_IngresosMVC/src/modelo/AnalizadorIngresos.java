package modelo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// RF-01 a RF-03, RF-06: lee texto, busca datos y guarda una copia modificada.
public class AnalizadorIngresos {
    // Busca la palabra cédula o CI y luego los caracteres escritos como número.
    private Pattern patronCedula = Pattern.compile("(?i)(?:cédula|cedula|\\bCI\\b)\\s*(?:es\\s*)?([0-9A-Za-z]+)");
    // Toma el texto que está después de correo hasta un espacio o una coma.
    private Pattern patronCorreo = Pattern.compile("(?i)correo\\s+([^\\s,;]+)");
    // Acepta el signo $ antes del monto o dólares, USD o $ después del número.
    private Pattern patronMonto = Pattern.compile("(?i)\\$\\s*([0-9]+(?:[.,][0-9]{1,2})?)|([0-9]+(?:[.,][0-9]{1,2})?)\\s*(?:USD|d[oó]lares|\\$)");
    private Pattern patronCorreoValido = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    public ArrayList<Aportador> leerArchivo(String nombreArchivo) throws FileNotFoundException {
        ArrayList<Aportador> lista = new ArrayList<Aportador>();
        File archivo = new File(nombreArchivo);
        Scanner lector = new Scanner(archivo, "UTF-8");
        while (lector.hasNextLine()) {
            String linea = lector.nextLine().trim();
            if (!linea.equals("") && !linea.startsWith("REGISTRO DE INGRESOS")) lista.add(analizarLinea(linea));
        }
        lector.close();
        return lista;
    }

    private Aportador analizarLinea(String linea) {
        String cedula = buscarDato(patronCedula, linea);
        String correo = buscarDato(patronCorreo, linea);
        String textoMonto = buscarMonto(linea);

        boolean cedulaValida = cedula.matches("[0-9]{10}");
        boolean correoValido = patronCorreoValido.matcher(correo).matches();
        double monto = 0;
        boolean montoValido = false;
        try {
            monto = Double.parseDouble(textoMonto.replace(',', '.'));
            montoValido = monto > 0;
        } catch (NumberFormatException error) {
            // Si no se encontró un número, el monto queda inválido.
        }
        return new Aportador(cedula, correo, monto, cedulaValida, correoValido, montoValido);
    }

    private String buscarDato(Pattern patron, String texto) {
        Matcher buscador = patron.matcher(texto);
        String dato = "No encontrado";
        // Algunas líneas mencionan el campo antes de escribir su valor; se toma la última coincidencia.
        while (buscador.find()) dato = buscador.group(1);
        if (!dato.equals("No encontrado")) return dato.replaceAll("[.]$", "");
        return dato;
    }

    private String buscarMonto(String texto) {
        Matcher buscador = patronMonto.matcher(texto);
        if (buscador.find()) {
            if (buscador.group(1) != null) return buscador.group(1);
            return buscador.group(2);
        }
        return "";
    }

    // RF-06: reemplaza texto y escribe el resultado en un archivo nuevo.
    public boolean reemplazarTexto(String archivoEntrada, String buscar, String reemplazar)
            throws FileNotFoundException {
        if (buscar == null || buscar.trim().equals("")) return false;
        Scanner lector = new Scanner(new File(archivoEntrada), "UTF-8");
        lector.useDelimiter("\\A");
        String contenido = lector.hasNext() ? lector.next() : "";
        lector.close();
        if (!contenido.contains(buscar)) return false;
        contenido = contenido.replace(buscar, reemplazar);
        PrintWriter escritor = new PrintWriter(new File("registro_ingresos_modificado.txt"));
        escritor.print(contenido);
        escritor.close();
        return true;
    }
}


