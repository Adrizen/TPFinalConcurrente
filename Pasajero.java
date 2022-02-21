
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
        Random r = new Random(vuelo.getReserva());
        /* Setea la hora del vuelo del pasajero de manera aleatoria, usando la semilla establecida previamente. Entonces dada una 
        reserva determinada, la hora del vuelo será la misma para todos los pasajeros. */
        //int horaVueloAleatoria = r.nextInt(23) + 1;
        //this.vuelo.setHoraVuelo(LocalTime.of(horaVueloAleatoria, 00));
        System.out.println(
                "reserva: " + vuelo.getReserva() + " para " + this.nombre + " hora vuelo: " + vuelo.getHoraVuelo()); //TODO: Debug
        // Se acaba de setear la hora del vuelo (usando LocalTime), con upperbound '23' y 00 minutos.
        this.atendido = false;
    }

    public void run() {
        while (!atendido) { // Mientras no hayan atendido a este pasajero, intentará entrar al aeropuerto. 
            if (aeropuerto.intentarIngresarAeropuerto()) {
                atendido = true; // Pudo entrar al aeropuerto, lo van a atender.
                aeropuerto.ingresarAeropuerto(this);
                aeropuerto.viajarATerminal(this);
                System.out.println("hora aeropuerto: " + aeropuerto.getHora() + ". hora vuelo de " + this.nombre + ": "
                        + vuelo.getHoraVuelo()); // TODO: Debug.
                // El Pasajero se dirige al FreeShop solo si tiene como mínimo 2 horas hasta que salga su vuelo.
                if (Math.abs(aeropuerto.getHora() - vuelo.getHoraVuelo()) >= 2) {
                    System.out.println(this.nombre + " entró a un freeshop.");
                    aeropuerto.intentarIngresarFreeShop(this);
                }
                System.out.println(this.nombre + " se sienta a esperar su vuelo en la sala de embarque general.");
                try {
                    synchronized (vuelo){
                        vuelo.wait();
                    }
                    System.out.println(this.nombre + " se fue en su avioncito");
                } catch (InterruptedException e) {
                    System.out.println("Ha ocurrido un error de tipo " + e);
                }
            } else {
                try {
                    System.out.println(
                            RED_BACKGROUND_BRIGHT + "El aeropuerto no está en horario de atención ahora mismo" + RESET);
                    Thread.sleep(11000); // Intentar entrar de nuevo al aeropuerto en una hora. (aprox 1 seg.)
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
