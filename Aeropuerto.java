import java.util.concurrent.CyclicBarrier;

public class Aeropuerto {
    private CyclicBarrier trenInterno;
    private final int MAX_CAPACIDAD_TREN;

    private PuestoDeInformes puestoInformes;


    public Aeropuerto() {
        MAX_CAPACIDAD_TREN = 5;
        this.trenInterno = new CyclicBarrier(MAX_CAPACIDAD_TREN); // faltaría la tarea de la cyclicBarrier.
        
        puestoInformes = new PuestoDeInformes();
        new Thread(puestoInformes).start();
    }

    public synchronized void hacerFilaPuestoInformes(Pasajero pasajero) {
        //System.out.println(pasajero.getNombre() + " hace la fila en el puesto de informes.");
        try {
            puestoInformes.getPuesto().put(pasajero);
            System.out.println(pasajero.getNombre() + " entra al puesto de informes.");
        } catch (InterruptedException e) {
            System.out.println("Ha ocurrido un error de tipo " + e);
        }

    }

}
