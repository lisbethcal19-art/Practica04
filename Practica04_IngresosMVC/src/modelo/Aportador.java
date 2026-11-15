package modelo;

// RF-02, RF-03: guarda los datos encontrados para una persona.
public class Aportador {
    private String cedula;
    private String correo;
    private double monto;
    private boolean cedulaValida;
    private boolean correoValido;
    private boolean montoValido;

    public Aportador(String cedula, String correo, double monto,
            boolean cedulaValida, boolean correoValido, boolean montoValido) {
        this.cedula = cedula;
        this.correo = correo;
        this.monto = monto;
        this.cedulaValida = cedulaValida;
        this.correoValido = correoValido;
        this.montoValido = montoValido;
    }

    public String getCedula() { return cedula; }
    public String getCorreo() { return correo; }
    public double getMonto() { return monto; }
    public boolean isCedulaValida() { return cedulaValida; }
    public boolean isCorreoValido() { return correoValido; }
    public boolean isMontoValido() { return montoValido; }

    public boolean esValido() {
        return cedulaValida && correoValido && montoValido;
    }

    public String getErrores() {
        String errores = "";
        if (!cedulaValida) errores += "Cédula incorrecta; ";
        if (!correoValido) errores += "Correo incorrecto; ";
        if (!montoValido) errores += "Monto incorrecto; ";
        if (errores.equals("")) return "Válido";
        return errores;
    }
}
