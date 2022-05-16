
import static Auxiliares.Colores.*;
import static Auxiliares.Log.*;

public class Pasajero implements Runnable {

    private String nombre; // Nombre para identificar a un pasajero de otro.
    private Aeropuerto aeropuerto; // Recurso compartido.
    private boolean atendido; // Indica si este pasajero fue atendido en el aeropuerto o no.
    private Vuelo vuelo;

    public Pasajero(String n, Aeropuerto a, Vuelo v) {
        this.nombre = n;
        this.aeropuerto = a;
        this.vuelo = v;
        this.atendido = false;
    }

    public void run() {
        while (!atendido) { // Mientras no hayan atendido a este pasajero, intentará entrar al aeropuerto. 
            escribirLOG(RED_BOLD + "DEBUG: " + this.nombre + " tiene el vuelo " + vuelo.getNombre() + RESET);
            if (aeropuerto.intentarIngresarAeropuerto()) {
                atendido = true; // Pudo entrar al aeropuerto, lo van a atender.
                aeropuerto.ingresarAeropuerto(this);
                aeropuerto.viajarATerminal(this);
                // El Pasajero se dirige al FreeShop solo si tiene como mínimo 2 horas hasta que salga su vuelo.
                if (Math.abs(aeropuerto.getHora() - vuelo.getHoraVuelo()) >= 2) {
                    intentarIngresarFreeShop();
                }
                escribirLOG(this.nombre + " se sienta a esperar su vuelo en la sala de embarque general.");
                try {
                    synchronized (vuelo){
                        vuelo.wait();   // El Pasajero espera en el monitor de su vuelo hasta que sea la hora de despegue.
                    }
                    escribirLOG(GREEN_UNDERLINED + " " + this.nombre + " subio a su avion y despego." + " " + RESET);
                } catch (InterruptedException e) {
                    System.out.println("Ha ocurrido un error de tipo " + e);
                }
            } else {
                try {
                    escribirLOG(
                            RED_BACKGROUND_BRIGHT + " El aeropuerto no esta en horario de atención ahora mismo " + RESET);
                    Thread.sleep(11000); // Intentar entrar de nuevo al aeropuerto en una hora. (aprox 1 seg.)
                } catch (InterruptedException e) {
                    System.out.println("Ha ocurrido un error de tipo " + e);
                }
            }
        }
    }

    // Si el pasajero tiene más de 2 horas de espera para su vuelo, puede intentar entrar al FreeShop. (si es que hay lugar disponible)
    public void intentarIngresarFreeShop(){
        this.getVuelo().getTerminal().intentarIngresarFreeShop(this);
    }

    public String getNombre() {
        return this.nombre;
    }

    public Vuelo getVuelo() {
        return this.vuelo;
    }

}
