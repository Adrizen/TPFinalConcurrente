
import java.time.LocalTime;
import static Auxiliares.Colores.*;
import static Auxiliares.Log.*;

public class Vuelo implements Runnable {
    private String nombre;
    private int reserva; // Indica en qué número de aerolinea tiene reserva el pasajero.
    private int puestoDeEmbarque;
    private char terminal;
    private LocalTime horaVuelo; // Indica a qué hora es el vuelo del pasajero.
    private Tiempo horaInicialAeropuerto;

    private boolean despego;

    public Vuelo(String n, int r, Tiempo t, LocalTime hv) {
        this.nombre = n;
        this.reserva = r;
        this.despego = false;
        this.horaInicialAeropuerto = t;
        this.horaVuelo = hv;
    }

    public void run() {
        try {
            while (!despego) { // Mientras este vuelo no haya partido, se comprueba periodicamente si este vuelo tiene que partir.
                escribirLOG(BLUE_BOLD + "Hora del " + this.nombre + ": " + horaVuelo.getHour() + " Reserva: " + reserva
                        + " Puesto de embarque: " + puestoDeEmbarque + RESET);
                if (horaInicialAeropuerto.getHora() == horaVuelo.getHour()) { // Comprobar si es la hora de salida de este vuelo.
                    synchronized (this) {
                        // Le avisa a todos los pasajeros que están esperando en el monitor de este objeto que el vuelo va a partir.
                        this.notifyAll();
                    }
                    despego = true;
                }
                Thread.sleep(10000); // Espera 10 segundos, es decir, el mismo tiempo que tarda el aeropuerto en pasar 1 hora.
            }
        } catch (InterruptedException e) {
            System.err.println("Ha ocurrido un error de tipo " + e);
        }
    }

    public String getNombre() {
        return this.nombre;
    }

    public int getReserva() {
        return this.reserva;
    }

    public char getTerminal() {
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

    public void setTerminal(char t) {
        this.terminal = t;
    }

    public void setPuestoDeEmbarque(int p) {
        this.puestoDeEmbarque = p;
    }

}
