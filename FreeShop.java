
import java.util.concurrent.locks.ReentrantLock;
import static Auxiliares.Log.*;

public class FreeShop {
    private final int CAPACIDAD_MAXIMA;
    private int cantidadPersonasActual; // Entero que indica cuantas personas hay dentro del FreeShop actualmente.
    private ReentrantLock mutex; // mutex para la variable 'cantidadPersonasActual'

    public FreeShop(int c) {
        this.CAPACIDAD_MAXIMA = c;
        this.cantidadPersonasActual = 0;
        this.mutex = new ReentrantLock();
    }

    public boolean intentarIngresar() {
        boolean pudoIngresar = false;
        mutex.lock();
        if (CAPACIDAD_MAXIMA > cantidadPersonasActual) {
            pudoIngresar = true;
            cantidadPersonasActual++;
        }
        mutex.unlock();
        return pudoIngresar;
    }

    public void ingresar(Pasajero pasajero) {
        escribirLOG(pasajero.getNombre() + " ingresó al FreeShop.");
        try {
            Thread.sleep(10000); // tiempo que tarda el pasajero en estar en el FreeShop. (10 seg)
        } catch (InterruptedException e) {
            System.err.println("Ha ocurrido un error de tipo " + e);
        }
        mutex.lock();
        cantidadPersonasActual--;
        mutex.unlock();

    }

}
