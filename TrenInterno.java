import static Auxiliares.Colores.*;

import java.util.concurrent.CyclicBarrier;
import static Auxiliares.Log.*;

public class TrenInterno implements Runnable {

    private CyclicBarrier tren;

    public TrenInterno(int c){
        tren = new CyclicBarrier(c, this);  // Una CyclicBarrier que simula el tren interno, cuando se llega a la cantidad 'c' de
                                            // pasajeros, el tren inicia el recorrido y deja a los pasajeros en su terminal correspondiente.
                                            // La consigna requiere que el tren inicie el recorrido solo cuando está lleno.
    }
    
    // Este run() se ejecutará cuando se llegue a la cantidad 'c' de pasajeros que hayan hecho 'await()'
    public void run(){
        escribirLOG(PURPLE_BOLD + "El tren esta lleno y empieza a viajar" + RESET);
        dejarPasajeros();
    }

    public void dirigirseATerminal(){
        try {
            tren.await();   // El pasajero llega aquí a esperar que el tren se llene.
        } catch (Exception e) {
            System.out.println("Ha ocurrido un error de tipo " + e);
        }
    }

    public void dejarPasajeros(){
        try {
            Thread.sleep(7000); // tiempo en hacer el recorrido e ir dejando a los pasajeros en su terminal correspondiente.
            escribirLOG(PURPLE_BOLD + "El tren ha dejado a todos los pasajeros en su destino y comienza a volver al inicio del recorrido" + RESET);
        } catch (InterruptedException e) {
            System.out.println("Ha ocurrido un error de tipo " + e);
        }
    }

}
