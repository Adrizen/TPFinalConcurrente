
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;
import static Auxiliares.Log.*;
import static Auxiliares.Colores.*;

public class FreeShop {
    private final int CAPACIDAD_MAXIMA;
    private int cantidadPersonasActual; // Entero que indica cuantas personas hay dentro del FreeShop actualmente.
    private ReentrantLock mutex; // mutex para la variable 'cantidadPersonasActual'
    Semaphore cajas = new Semaphore(2); // Un semáforo con 2 permisos que simula las cajas del FreeShop.

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
        mutex.lock();
        escribirLOG(WHITE_UNDERLINED + pasajero.getNombre() + " ingreso al FreeShop de su terminal " + letra
                + ". Quedan " + (CAPACIDAD_MAXIMA - cantidadPersonasActual) + " lugares" + RESET);
        mutex.unlock();
        try {
            Thread.sleep(5000); // tiempo que tarda el pasajero en entrar y ojear el FreeShop. (5 seg)
            if (comprarOMirar()) {  // El pasajero decide si comprar algo o solo mirar los productos.
                escribirLOG(BLUE_BRIGHT + pasajero.getNombre() + " decidio comprar algo en el FreeShop." + RESET);
                cajas.acquire();    // El pasajero ocupa una de las cajas para que le cobren su compra.
				escribirLOG(pasajero.getNombre() + " esta pagando lo que compro en el FreeShop.");
                Thread.sleep(5000); // tiempo que tarda el pasajero en hacer su compra. (5 seg)
                cajas.release();    // El pasajero termina de pagar y libera la caja donde lo atendían.
            } else {
                escribirLOG(BLUE_BRIGHT + pasajero.getNombre() + " decidio no comprar algo en el FreeShop. Esta todo muy caro." + RESET);
            }
        } catch (InterruptedException e) {
            System.err.println("Ha ocurrido un error de tipo " + e);
        }
        mutex.lock();
        cantidadPersonasActual--;
        mutex.unlock();
    }

    // Genera un boolean aleatorio que tiene 50% de probabilidades de ser 'true' y 50% de ser 'false'. Usado para decidir aleatoriamente si el pasajero
    // va a comprar algo en el FreeShop o solo mirar.
    private boolean comprarOMirar() {
        Random r = new Random();
        boolean comprar = r.nextBoolean();
        return comprar;
    }

}
