
import static Auxiliares.Colores.*;

import java.time.LocalTime;
import java.util.Random;

public class Pasajero implements Runnable {

    private String nombre; // Nombre para identificar a un pasajero de otro.
    private Aeropuerto aeropuerto; // Recurso compartido.
    private boolean atendido; // Indica si este pasajero fue atendido en el aeropuerto o no.
    private Vuelo vuelo;

    public Pasajero(String n, Aeropuerto a, Vuelo v) {
        this.nombre = n;
        this.aeropuerto = a;
        this.vuelo = v;
        // Genera una instancia de random usando como semilla la reserva del vuelo del pasajero.
        System.out.println("reserva: " + vuelo.getReserva() + " para " + this.nombre);
        Random r = new Random(vuelo.getReserva());
        /* Setea la hora del vuelo del pasajero de manera aleatoria, usando la semilla establecida previamente. Entonces dada una 
        reserva determinada, la hora del vuelo será la misma para todos los pasajeros. */
        this.vuelo.setHoraVuelo(LocalTime.of(r.nextInt(23), 00));
        // Se acaba de setear la hora del vuelo (usando LocalTime), con upperbound '23' y 00 minutos.
        this.atendido = false;
    }

    public void run() {
        while (!atendido) { // mientras no hayan atendido a este pasajero, intentará entrar al aeropuerto. 
            if (aeropuerto.intentarIngresarAeropuerto()) {
                atendido = true; // Pudo entrar al aeropuerto, lo van a atender.
                aeropuerto.ingresarAeropuerto(this);
                aeropuerto.viajarATerminal(this);
                System.out.println("hora aeropuerto: " + aeropuerto.getHora() + ". hora vuelo de " + this.nombre + ": "
                        + vuelo.getHoraVuelo());
                if (Math.abs(aeropuerto.getHora() - vuelo.getHoraVuelo()) >= 2) {
                    // El Pasajero se dirige al FreeShop solo si tiene como mínimo 2 horas hasta que salga su vuelo.
                    //aeropuerto.intentarIngresarFreeShop();
                }
            } else {
                try {
                    System.out.println(
                            RED_BACKGROUND_BRIGHT + "El aeropuerto no está en horario de atención ahora mismo" + RESET);
                    Thread.sleep(11000); // intentar de nuevo en una hora. (aprox 1 seg.)
                } catch (InterruptedException e) {
                    System.out.println("Ha ocurrido un error de tipo " + e);
                }
            }
        }
    }

    public String getNombre() {
        return this.nombre;
    }

    public Vuelo getVuelo() {
        return this.vuelo;
    }

}
