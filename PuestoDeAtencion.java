import static Auxiliares.Colores.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;
import static Auxiliares.Log.*;

public class PuestoDeAtencion {

    private ArrayBlockingQueue<Pasajero> puestoDeAtencion; // Cola del puesto de atención para atender a los pasajeros en órden.
    private HallCentral hallCentral; // Hall central donde esperan los que no se pueden formar en la cola del puesto de atención.
                                     // este hall central es compartido por todos los puestos de atención.

    private ReentrantLock mutex = new ReentrantLock(); // mutex del ArrayBlockingQueue "puestoDeAtencion".
    private int pasajerosEsperandoEnHall; // Cantidad de pasajeros que están esperando en el hall central a que se desocupe un lugar en este puesto.
    private Terminal[] terminales;  // Arreglo que contiene las terminales, utilizado para asignarle la terminal correspondiente a cada pasajero.

    // Constructor.
    public PuestoDeAtencion(HallCentral h, int c, Terminal[] t) {
        this.puestoDeAtencion = new ArrayBlockingQueue<Pasajero>(c);
        this.hallCentral = h;
        this.pasajerosEsperandoEnHall = 0;
        this.terminales = t;
    }

    // Los pasajeros hacen fila en este puesto de atención de una aerolinea particular.
    public void hacerFilaPuestoDeAtencion(Pasajero pasajero) {
        boolean atendido = false;
        while (!atendido) { // Mientras el pasajero no sea atendido en el puesto de atención...
            mutex.lock();
            boolean bandera = puestoDeAtencion.offer(pasajero); // colocar al pasajero en la cola de espera. Si éxito, devuelve true.
            mutex.unlock();
            if (bandera) { // Se pudo colocar al pasajero en la cola del puesto de atención.
                escribirLOG(GREEN_BOLD + pasajero.getNombre() + " ingresa al puesto de atencion n"
                                + pasajero.getVuelo().getAerolinea()
                                + ". En el puesto quedan " + puestoDeAtencion.remainingCapacity() + " lugares" + RESET);
                atenderPasajero(pasajero);
                escribirLOG(YELLOW_BOLD + pasajero.getNombre() + " se dirige a la estacion del tren interno." + RESET);
                atendido = true;
            } else { // No se pudo colocar al pasajero en la cola de espera (no había lugar). Pasajero se dirige al hall central a esperar.
                escribirLOG(RED_BOLD + pasajero.getNombre() + " intento entrar al puesto de atencion n"+ pasajero.getVuelo().getAerolinea() 
                +" pero no habia lugar. El pasajero se dirige al hall central a esperar" + RESET);
                mutex.lock();
                pasajerosEsperandoEnHall++;
                mutex.unlock();
                hallCentral.esperar(pasajero); // El pasajero se dirige al hall central a esperar que se desocupe un lugar en su puesto de atención.
                /* En este punto, la ejecución de este hilo pasajero en particular solo va a 
                continuar cuando pueda conseguir un lugar en el puesto de atención. */
            }
        }
    }

    // Atender al pasajero que se encuentra 1° en la cola de espera de este puesto de atención.
    public void atenderPasajero(Pasajero pasajero) {
        try {
            Thread.sleep(4000); // tiempo para atender al pasajero en el puesto de atención.
            mutex.lock();
            // A continuación pregunto si la capacidad del puesto de atención estaba llena Y si había gente esperando en el hall, entonces ...
            if (puestoDeAtencion.remainingCapacity() == 0 && pasajerosEsperandoEnHall > 0) {
                hallCentral.avisarGuardia(pasajero); // ... aviso al guardia que se desocupó un lugar y ...
                pasajerosEsperandoEnHall--;          // ... resto en 1 a la variable que controla cuantos pasajeros hay esperando.
                // Con esta condición se evita liberar varios permisos en el semáforo correspondiente del hall central.
            }
            puestoDeAtencion.take(); // saco al pasajero que acabo de atender de la cola de espera.
            mutex.unlock();
            asignarTerminal(pasajero, pasajero.getVuelo().getPuestoDeEmbarque());
        } catch (InterruptedException e) {
            System.err.println("Ha ocurrido un error de tipo " + e);
        }
    }

    // Asigna a un pasajero su terminal correspondiente, dependiendo de su puesto de embarque.
    private void asignarTerminal(Pasajero pasajero, int puestoDeEmbarque) {
        // La asignación de terminales se hace de esta manera ya que que si existieran más terminales no se podría saber 
        // el rango de puestos de embarque de cada nueva terminal. 
        if (puestoDeEmbarque >= 1 && puestoDeEmbarque <= 7) {
            pasajero.getVuelo().setTerminal(terminales[0]);
        } else if (puestoDeEmbarque >= 8 && puestoDeEmbarque <= 15) {
            pasajero.getVuelo().setTerminal(terminales[1]);
        } else {
            pasajero.getVuelo().setTerminal(terminales[2]);
        }
        escribirLOG(WHITE_BOLD + "El " + pasajero.getNombre() + " se va de su puesto de atencion. Le asignaron el puesto de embarque n"
            + puestoDeEmbarque + " en la terminal " + pasajero.getVuelo().getTerminal().getLetra() + RESET);
    }

}
