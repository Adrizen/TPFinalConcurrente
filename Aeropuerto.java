
import static Auxiliares.Colores.*;

import java.util.concurrent.locks.ReentrantLock;

public class Aeropuerto {
    private TrenInterno trenInterno;      // Tren interno.
    private PuestoDeAtencion[] puestosDeAtencion; // Arreglo de puestos de atención, uno por aerolinea.
    private ReentrantLock puestoInformes;         // Solo hay un puesto de informes y se atiende a UN solo pasajero al mismo tiempo.
    private Terminal[] terminales;

    private Tiempo horario;  // Utilizado para saber qué hora es en el aeropuerto y si este está atendiendo pasajeros.
                             // El horario de atención es de 6hs a 22hs. Arranca en 6hs.

    public Aeropuerto(PuestoDeAtencion[] p, int c, Terminal[] t, TrenInterno tr, Tiempo ti) {
        this.puestoInformes = new ReentrantLock();
        this.trenInterno = tr;
        this.puestosDeAtencion = p;
        this.terminales = t;
        this.horario = ti;
    }

    // Un pasajero intenta entrar al aeropuerto y checkear si el mismo está en horario de atención.
    // Devuelve true si el aeropuerto está en horario de atención, si no devuelve false.
    public boolean intentarIngresarAeropuerto(){
        boolean atiendePasajeros;
        if (horario.getHora() >= 6 && horario.getHora() <= 22){
            atiendePasajeros = true;    // El aeropuerto está en horario de atención.
        } else {
            atiendePasajeros = false;   // El aeropuerto no está en horario de atención.
        }
        return atiendePasajeros;
    }

    // Un pasajero ingresa al aeropuerto y se dirige al puesto de informes. (solo existe uno en el aeropuerto)
    public void ingresarAeropuerto(Pasajero pasajero) {
        try {
            puestoInformes.lock();
            Thread.sleep(1500); // Tiempo para atender al pasajero.
            //System.out.println(pasajero.getNombre() + " entra al puesto de informes y le indican que puesto de atención le corresponde");
            puestoInformes.unlock();
            puestosDeAtencion[pasajero.getVuelo().getReserva()].hacerFilaPuestoDeAtencion(pasajero);
            trenInterno.dirigirseATerminal();
        } catch (InterruptedException e) {
            System.err.println("Ha ocurrido un error de tipo " + e);
        }
    }

    // Luego de viajar en el tren interno, el pasajero ingresa a la terminal que le toca.
    public void ingresarTerminal(Pasajero pasajero){

    }


}
