
import java.util.concurrent.locks.ReentrantLock;
import static Auxiliares.Log.*;
import static Auxiliares.Colores.*;

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

    public void ingresar(Pasajero pasajero, char letra) {
        escribirLOG(WHITE_UNDERLINED + pasajero.getNombre() + " ingreso al FreeShop de su terminal " + letra
                + ". Quedan " + (CAPACIDAD_MAXIMA-cantidadPersonasActual) + " lugares" + RESET);
        try {
            Thread.sleep(10000); // tiempo que tarda el pasajero en estar en el FreeShop. (10 seg)
        } catch (InterruptedException e) {
            System.err.println("Ha ocurrido un error de tipo " + e);
        }
        //escribirLOG(pasajero.getNombre() + " terminó de " );
        // TODO: Hacer las 2 cajas y aleatoriedad para comprar/solo ver productos.
        mutex.lock();
        cantidadPersonasActual--;
        mutex.unlock();

    }

}
