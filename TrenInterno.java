import static Auxiliares.Colores.*;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;
import static Auxiliares.Log.*;

public class TrenInterno implements Runnable {
    private ReentrantLock terminalA;    // Estos 3 lock se utilizan como monitores, para que los hilos pasajeros se bajen en su terminal correspondiente.
    private ReentrantLock terminalB;
    private ReentrantLock terminalC;
    private int CAPACIDAD_MAXIMA_TRENINTERNO;
    private Semaphore trenLleno;          // Semáforo usado para saber cuando el tren está lleno y debe partir.
    private Semaphore trenEnEstacion;     // Semáforo utilizado para verificar si el tren está en la estación y nuevos pasajeros pueden subir.

    public TrenInterno(int capacidadMaxima) {
        terminalA = new ReentrantLock(); 
        terminalB = new ReentrantLock();
        terminalC = new ReentrantLock();
        CAPACIDAD_MAXIMA_TRENINTERNO = capacidadMaxima;
        trenLleno = new Semaphore(0);   
        trenEnEstacion = new Semaphore(capacidadMaxima);    // Cada pasajero que se sube al tren consumirá un permiso.
    }

    public void run() {
        while (true) {
            try {
                trenLleno.acquire(CAPACIDAD_MAXIMA_TRENINTERNO); // El tren se queda esperando acá hasta que este se llene de pasajeros.
                escribirLOG(PURPLE_BOLD + "El tren esta lleno y empieza a viajar" + RESET);
                dejarPasajeros();
                trenEnEstacion.release(CAPACIDAD_MAXIMA_TRENINTERNO); // Se reinicia el semáforo para permitir a nuevos pasajeros subirse al tren.
            } catch (InterruptedException e) {
                System.out.println("Ha ocurrido un error de tipo " + e);
            }
        }
    }

    public void dirigirseATerminal(Pasajero pasajero) {
        try {
            trenEnEstacion.acquire();  // Solo se permite que pasajeros suban al tren si este está en la estación.
            trenLleno.release();       // Pasajero libera un permiso de tren lleno. El tren parte cuando se llega a la capacidad máxima del tren. run()
            if (pasajero.getVuelo().getTerminal().getLetra() == 'A') {
                escribirLOG(WHITE_BACKGROUND_BRIGHT + pasajero.getNombre() + " se sube al tren interno. Se va a bajar en la terminal A." + RESET);
                synchronized (terminalA) {
                    terminalA.wait();   // Pasajero espera a que lo despierten en su estación.
                }   
            } else if (pasajero.getVuelo().getTerminal().getLetra() == 'B') {
                escribirLOG(WHITE_BACKGROUND_BRIGHT + pasajero.getNombre() + " se sube al tren interno. Se va a bajar en la terminal B." + RESET);
                synchronized (terminalB) {
                    terminalB.wait();   // Pasajero espera a que lo despierten en su estación.
                }
            } else {
                escribirLOG(WHITE_BACKGROUND_BRIGHT + pasajero.getNombre() + " se sube al tren interno. Se va a bajar en la terminal C." + RESET);
                synchronized (terminalC) {
                    terminalC.wait();   // Pasajero espera a que lo despierten en su estación.
                }
            }
        } catch (InterruptedException e) {
            System.out.println("Ha ocurrido un error de tipo " + e);
        }
    }

    public void dejarPasajeros() {
        try {
            Thread.sleep(4000); // Tiempo para viajar a la terminal
            synchronized (terminalA) {
                terminalA.notifyAll();
            }
            escribirLOG(PURPLE_BOLD + "El tren ha dejado a todos los pasajeros de la terminal A" + RESET);
            Thread.sleep(4000);
            synchronized (terminalB) {
                terminalB.notifyAll();
            }
            escribirLOG(PURPLE_BOLD + "El tren ha dejado a todos los pasajeros de la terminal B" + RESET);
            Thread.sleep(4000);
            synchronized (terminalC) {
                terminalC.notifyAll();
            }
            escribirLOG(PURPLE_BOLD + "El tren ha dejado a todos los pasajeros de la terminal C" + RESET);
            escribirLOG(PURPLE_BOLD + "El tren se dispone a volver a la estacion" + RESET);
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            System.out.println("Ha ocurrido un error de tipo " + e);
        }
    }

}