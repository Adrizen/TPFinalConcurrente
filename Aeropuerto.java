import java.util.concurrent.locks.ReentrantLock;
import static Auxiliares.Log.*;

public class Aeropuerto {
    private TrenInterno trenInterno; // Tren interno.
    private PuestoDeAtencion[] puestosDeAtencion; // Arreglo de puestos de atención, uno por aerolinea.
    private ReentrantLock puestoInformes; // Solo hay un puesto de informes y se atiende a UN solo pasajero al mismo tiempo.

    private Tiempo horario; // Utilizado para saber qué hora es en el aeropuerto y si este está atendiendo pasajeros.
                            // El horario de atención es de 6hs a 22hs. Arranca en 6hs.

    public Aeropuerto(PuestoDeAtencion[] p, int c, Terminal[] t, TrenInterno tr, Tiempo ti) {
        this.puestoInformes = new ReentrantLock();
        this.trenInterno = tr;
        this.puestosDeAtencion = p;
        this.horario = ti;
    }

    // Un pasajero intenta entrar al aeropuerto y checkear si el mismo está en horario de atención.
    // Devuelve true si el aeropuerto está en horario de atención, si no devuelve false.
    public boolean intentarIngresarAeropuerto() {
        boolean atiendePasajeros;
        if (horario.getHora() >= 6 && horario.getHora() <= 22) {
            atiendePasajeros = true; // El aeropuerto está en horario de atención.
        } else {
            atiendePasajeros = false; // El aeropuerto no está en horario de atención.
        }
        return atiendePasajeros;
    }

    // Un pasajero ingresa al aeropuerto y se dirige al puesto de informes, luego es derivado a su puesto de atención correspondiente.
    public void ingresarAeropuerto(Pasajero pasajero) {
        try {
            puestoInformes.lock(); // mutex puesto de informes (solo hay uno en el aeropuerto.)
            Thread.sleep(500); // Tiempo para atender al pasajero en el puesto de informes.
            escribirLOG(pasajero.getNombre()
                    + " es atendido en informes y le indican que le corresponde el puesto de atencion n"
                    + pasajero.getVuelo().getAerolinea());
            puestoInformes.unlock();
            // A continuación el pasajero es derivado al puesto de atención que le corresponde y se dispone a hacer la fila allí.
            puestosDeAtencion[pasajero.getVuelo().getAerolinea()].hacerFilaPuestoDeAtencion(pasajero);
        } catch (InterruptedException e) {
            System.err.println("Ha ocurrido un error de tipo " + e);
        }
    }

    // Para dirigirse a la terminal que le tocó, el pasajero utiliza el tren interno del aeropuerto.
    public void viajarATerminal(Pasajero pasajero) {
        trenInterno.dirigirseATerminal(pasajero);
    }

    public int getHora() {
        return this.horario.getHora();
    }

}
