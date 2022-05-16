
import java.time.LocalTime;
import static Auxiliares.Colores.*;
import static Auxiliares.Log.*;

public class Vuelo implements Runnable {
    private String nombre;
    private int aerolinea; // Indica el número de aerolinea que tiene este vuelo.
    private int puestoDeEmbarque;
    private Terminal terminal;
    private LocalTime horaVuelo; // Indica a qué hora es el vuelo del pasajero.
    private Tiempo horaAeropuerto;

    private boolean despego;

    public Vuelo(String n, int a, Tiempo t, LocalTime hv) {
        this.nombre = n;
        this.aerolinea = a;
        this.despego = false;
        this.horaAeropuerto = t;
        this.horaVuelo = hv;
    }

    public void run() {
        try {
            escribirLOG(RED_BOLD + "DEBUG: " + this.nombre + " con hora asignada: " + horaVuelo.getHour() + "hs. Aerolinea n" + aerolinea
            + ". Puesto de embarque n" + puestoDeEmbarque + " " + RESET);
            while (!despego) { // Mientras este vuelo no haya partido, se comprueba periodicamente si este vuelo tiene que partir.
                if (horaAeropuerto.getHora() == horaVuelo.getHour()) { // Comprobar si es la hora de salida de este vuelo.
                    escribirLOG("Ha despegado el " + nombre + " . Su hora de despegue eran las " + horaVuelo.getHour());
                    synchronized (this) {
                        // Le avisa a todos los pasajeros que están esperando en el monitor de este objeto que el vuelo va a partir.
                        this.notifyAll();
                    }
                    despego = true;
                }
                Thread.sleep(9000); // Espera 9 segundos, es decir, un poquito más de lo que tarda el aeropuerto en pasar una hora.
            }
        } catch (InterruptedException e) {
            System.err.println("Ha ocurrido un error de tipo " + e);
        }
    }

    public String getNombre() {
        return this.nombre;
    }

    public int getAerolinea() {
        return this.aerolinea;
    }

    public Terminal getTerminal() {
        return this.terminal;
    }

    public int getHoraVuelo() {
        return this.horaVuelo.getHour();
    }

    public int getPuestoDeEmbarque() {
        return this.puestoDeEmbarque;
    }

    public void setHoraVuelo(LocalTime lt) {
        this.horaVuelo = lt;
    }

    public void setTerminal(Terminal t) {
        this.terminal = t;
    }

    public void setPuestoDeEmbarque(int p) {
        this.puestoDeEmbarque = p;
    }

}
