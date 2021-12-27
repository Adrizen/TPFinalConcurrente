import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class PuestoDeInformes implements Runnable {

    private BlockingQueue<Pasajero> puesto;
    private final int MAX_PASAJEROS;

    public PuestoDeInformes() {
        MAX_PASAJEROS = 5;
        this.puesto = new ArrayBlockingQueue<Pasajero>(MAX_PASAJEROS);
    }

    public void run() {
        while (true) {
            try {
                Pasajero pasajero = puesto.take();
                Thread.sleep(2000); // simula tiempo en atender al pasajero.
                System.out.println(pasajero.getNombre() + " sale del puesto de informes.");
            } catch (InterruptedException e) {
                System.out.println("Ha ocurrido un error de tipo " + e);
            }
        }
    }

    public BlockingQueue<Pasajero> getPuesto() {
        return this.puesto;
    }

}
