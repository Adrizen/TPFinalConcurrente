
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.ReentrantLock;
import static Colores.Colores.*;

public class Aeropuerto {
    private CyclicBarrier trenInterno; // Tren interno.
    private final int MAX_CAPACIDAD_TREN; // Capacidad máxima tren interno.
    private HallCentral hallCentral; // Hall central donde los pasajeros esperan un lugar en los puestos de atención.
                                     // este hall central es compartido por todos los puesto de atención.

    private PuestoDeAtencion[] puestosDeAtencion; // Arreglo de puestos de atención, uno por aerolinea.
    private ReentrantLock puestoInformes; // Solo hay un puesto de informes y se atiende de a UN pasajero.

    public Aeropuerto(HallCentral h, PuestoDeAtencion[] p, int c) {
        puestoInformes = new ReentrantLock();
        MAX_CAPACIDAD_TREN = c;
        this.trenInterno = new CyclicBarrier(MAX_CAPACIDAD_TREN); // TODO: faltaría la tarea de la cyclicBarrier.
        puestosDeAtencion = p;
        hallCentral = h;
    }

    public void hacerFilaPuestoInformes(Pasajero pasajero) {
        try {
            puestoInformes.lock();
            Thread.sleep(1500); // Tiempo para atender al pasajero.
            //System.out.println(pasajero.getNombre() + " entra al puesto de informes y le indican que puesto de atención le corresponde");
            puestoInformes.unlock();
            puestosDeAtencion[pasajero.getReserva()].hacerFilaPuestoDeAtencion(pasajero);
        } catch (InterruptedException e) {
            System.err.println("Ha ocurrido un error de tipo " + e);
        }

    }

}
