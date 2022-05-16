
import static Auxiliares.Colores.*;

import java.util.concurrent.Semaphore;
import static Auxiliares.Log.*;


public class HallCentral {

    /* Este arreglo de Semáforos binarios se utiliza para que el guardia sepa a qué pasajero tiene que avisarle que se desocupó un lugar en su 
    puesto de atención correspondiente. En la posición 0 "esperan" los pasajeros del puesto de atención con aerolinea N° 0, en la 
    posición 1 "esperan" los pasajeros del puesto de atención con aerolinea N° 1 y así. */

    Semaphore[] arregloPuestosDeAtencion;

    public HallCentral(int cantidadAerolineas) {
        this.arregloPuestosDeAtencion = new Semaphore[cantidadAerolineas];
        for (int i = 0; i < cantidadAerolineas; i++) {
            arregloPuestosDeAtencion[i] = new Semaphore(0);
        }
    }

    public void esperar(Pasajero pasajero) {
        escribirLOG(YELLOW_BOLD + pasajero.getNombre() + " espera en el hall central." + RESET);
        try {
            arregloPuestosDeAtencion[pasajero.getVuelo().getAerolinea()].acquire(); 
            /* Los pasajeros que quieren acceder al puesto de atención 'n' esperan en el semáforo 'n' de este arreglo. Entonces, al desocuparse un lugar
            (liberarse un permiso) todos pelean por adquirir el permiso que les permita colocarse en la cola de espera del puesto de atención 'n'. */
        } catch (InterruptedException e) {
            System.out.println("Ha ocurrido un error de tipo " + e);
        }
    }

    public void avisarGuardia(Pasajero pasajero) {
        escribirLOG(CYAN_UNDERLINED + "El guardia avisa que se libero un lugar en el puesto de atencion n" 
        + pasajero.getVuelo().getAerolinea() + RESET);
        arregloPuestosDeAtencion[pasajero.getVuelo().getAerolinea()].release(); // libera el puesto que tenía en el puesto de atención.
    }

}
