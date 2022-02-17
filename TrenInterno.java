import static Auxiliares.Colores.*;

import java.util.concurrent.CyclicBarrier;

public class TrenInterno implements Runnable {

    private CyclicBarrier tren;

    public TrenInterno(int c){
        tren = new CyclicBarrier(c, this);  // Una CyclicBarrier que simula el tren interno, cuando se llega a la cantidad 'c' de
                                            // pasajeros, el tren inicia el recorrido y deja a los pasajeros en su terminal correspondiente.
    }
    
    public void run(){
        System.out.println(PURPLE_BOLD + "El tren está lleno y empieza a viajar" + RESET);
        dejarPasajeros();
    }

    public void dirigirseATerminal(){
        try {
            tren.await();   // El pasajero espera a que el tren se llene.
        } catch (Exception e) {
            System.out.println("Ha ocurrido un error de tipo " + e);
        }
    }

    public void dejarPasajeros(){
        try {
            Thread.sleep(7000); // tiempo en hacer el recorrido e ir dejando a cada pasajero en su terminal correspondiente.
            System.out.println(PURPLE_BOLD + "El tren ha dejado a todos los pasajeros en su destino y comienza a volver al inicio del recorrido" + RESET);
            Thread.sleep(4000); // tiempo en volver al inicio del recorrido.
        } catch (InterruptedException e) {
            System.out.println("Ha ocurrido un error de tipo " + e);
        }
    }

}
