
import static Auxiliares.Colores.*;

import java.util.concurrent.Semaphore;

public class HallCentral {

    /* Este arreglo de Semáforos se utiliza para que el guardia sepa a qué pasajero tiene que avisarle que se desocupó un lugar en su 
    puesto de atención correspondiente. En la posición 0 "esperan" los pasajeros del puesto de atención con reserva N° 0, en la 
    posición 1 "esperan" los pasajeros del puesto de atencióncon reserva N° 1 y así. */
    Semaphore[] arregloPuestosDeAtencion;   

    public HallCentral(int cantidadAerolineas){
        this.arregloPuestosDeAtencion = new Semaphore[cantidadAerolineas];
        for (int i = 0; i < cantidadAerolineas; i++) {
            arregloPuestosDeAtencion[i] = new Semaphore(0);
        }
    }

    public void esperar(Pasajero pasajero){
        System.out.println(YELLOW_BOLD + pasajero.getNombre() + " espera en el hall central." + RESET);
        try{
            arregloPuestosDeAtencion[pasajero.getReserva()].acquire();  // un pasajero espera en su semáforo correspondiente.
        } catch (InterruptedException e){
            System.out.println("Ha ocurrido un error de tipo " + e);
        }
        
    }

    public void avisarGuardia(Pasajero pasajero){
        System.out.println("El guardia le avisa a un pasajero que se liberó un lugar en el puesto de atención");
        arregloPuestosDeAtencion[pasajero.getReserva()].release();  // libera el puesto que tenía en el puesto de atención.
    }

}
