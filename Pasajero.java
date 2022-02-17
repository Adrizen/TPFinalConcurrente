
import static Auxiliares.Colores.*;

public class Pasajero implements Runnable {

    private String nombre;          // Nombre para identificar a un pasajero de otro.
    private Aeropuerto aeropuerto;  // Recurso compartido.
    private boolean atendido;       // Indica si este pasajero fue atendido en el aeropuerto o no.
    private Vuelo vuelo;            

    public Pasajero(String n, Aeropuerto a, Vuelo v) {
        this.nombre = n;
        this.aeropuerto = a;
        this.vuelo = v;
        this.atendido = false;
    }

    public void run() {
        while (!atendido){  // mientras no hayan atendido a este pasajero, intentará entrar al aeropuerto. 
            if (aeropuerto.intentarIngresarAeropuerto()){
                atendido = true;    // Pudo entrar al aeropuerto, lo van a atender.
                aeropuerto.ingresarAeropuerto(this);
                aeropuerto.ingresarTerminal(this);
            } else {
                try {
                    System.out.println(RED_BACKGROUND_BRIGHT + "El aeropuerto no está en horario de atención ahora mismo" + RESET);
                    Thread.sleep(11000);    // intentar de nuevo en una hora. (aprox 1 seg.)
                } catch (InterruptedException e) {
                    System.out.println("Ha ocurrido un error de tipo " + e);
                }   
            }
        }
    }

    public String getNombre() {
        return this.nombre;
    }

    public Vuelo getVuelo(){
        return this.vuelo;
    }

}
