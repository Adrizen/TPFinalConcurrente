import static Auxiliares.Colores.*;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

public class PuestoDeAtencion {

    private ArrayBlockingQueue<Pasajero> puestoDeAtencion; // Cola del puesto de atención para atender a los pasajeros en órden.
    private HallCentral hallCentral; // Hall central donde esperan los que no se pueden formar en la cola del puesto de atención.
                                     // este hall central es compartido por todos los puesto de atención.

    private ReentrantLock mutex = new ReentrantLock(); // mutex de la cola "puestoDeAtencion".

    // Constructor.
    public PuestoDeAtencion(HallCentral h, int c) {
        puestoDeAtencion = new ArrayBlockingQueue<Pasajero>(c);
        hallCentral = h;
    }

    // Los pasajeros hacen fila en este puesto de atención de una aerolinea particular.
    public void hacerFilaPuestoDeAtencion(Pasajero pasajero) {
        boolean atendido = false;
        while (!atendido) { // Mientras el pasajero no sea atendido en el puesto de atención...
            mutex.lock();
            boolean bandera = puestoDeAtencion.offer(pasajero); // colocar al pasajero en la cola de espera. Si éxito, devuelve true.
            mutex.unlock();
            if (bandera) { // Se pudo colocar al pasajero en la cola del puesto de atención.
                System.out.println(GREEN_BOLD + pasajero.getNombre()
                        + " ingresa al puesto de atención que le corresponde." + RESET);
                atenderPasajero(pasajero);
                System.out.println(
                        GREEN_BOLD + pasajero.getNombre() + " se dirige a la estación del tren interno." + RESET);
                atendido = true;
            } else { // No se pudo colocar al pasajero en la cola de espera (no había lugar). Pasajero se dirige al hall central a esperar.
                System.err.println(RED_BOLD + "No hay lugar en el puesto de atención. " + pasajero.getNombre()
                        + " se dirige al hall central a esperar un lugar." + RESET);
                hallCentral.esperar(pasajero); // El pasajero se dirige al hall central a esperar un lugar en el puesto de atención.
            }
        }
    }

    // Atender al pasajero que se encuentra 1° en la cola de espera de este puesto de atención.
    public void atenderPasajero(Pasajero pasajero) {
        try {
            Thread.sleep(4000); // tiempo para atender al pasajero en el puesto de atención.
            mutex.lock();
            if (puestoDeAtencion.remainingCapacity() == 0) {    // Si la capacidad del puesto de atención estaba llena ...
                hallCentral.avisarGuardia(pasajero);            // ... aviso al guardia que se desocupó un lugar.
                // Con esta condición se evita liberar varios permisos en el semáforo correspondiente del hall central.
            }
            puestoDeAtencion.take(); // saco al pasajero que acabo de atender de la cola de espera.
            mutex.unlock();
            asignarTerminal(pasajero); // Se le asigna al pasajero a qué terminal tiene que ir.
        } catch (InterruptedException e) {
            System.err.println("Ha ocurrido un error de tipo " + e);
        }
    }

    private void asignarTerminal(Pasajero pasajero) {
        Random r = new Random();
        int terminal = r.nextInt(68 - 65) + 65; // Se genera una terminal destino de manera aleatoria para el pasajero.
        //pasajero.setTerminal((char) terminal);
    }

}
