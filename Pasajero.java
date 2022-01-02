
public class Pasajero implements Runnable {

    private String nombre;
    private Aeropuerto aeropuerto;
    private int reserva;

    public Pasajero(String n, Aeropuerto a, int r) {
        this.nombre = n;
        this.aeropuerto = a;
        this.reserva = r;
    }

    public String getNombre() {
        return this.nombre;
    }

    public int getReserva(){
        return this.reserva;
    }

    public void run() {
        aeropuerto.hacerFilaPuestoInformes(this);
    }

}
