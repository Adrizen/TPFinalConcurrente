
import java.util.concurrent.Semaphore;
import static Colores.Colores.*;

public class HallCentral implements Runnable {

    private Semaphore hallCentral = new Semaphore(0);

    public void run() {
        while (true){

        }
    }

    public void esperar(Pasajero pasajero){
        System.out.println(YELLOW_BOLD + pasajero.getNombre() + " espera en el hall central." + RESET);
        try{
            hallCentral.acquire();
        } catch (InterruptedException e){
            System.out.println("Ha ocurrido un error de tipo " + e);
        }
        
    }

    public void avisarGuardia(){
        System.out.println("El guardia le avisa a un pasajero que se liberó un lugar en el puesto de atención");
        hallCentral.release();
    }

}
