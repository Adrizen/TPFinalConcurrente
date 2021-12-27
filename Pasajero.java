public class Pasajero implements Runnable {

    private String nombre;
    private Aeropuerto aeropuerto;

    public Pasajero(String n, Aeropuerto a){
        this.nombre = n;
        this.aeropuerto = a;
    }

    public String getNombre(){
        return this.nombre;
    }
    
    public void run(){
        //System.out.println(nombre + " entra al aeropuerto.");
        aeropuerto.hacerFilaPuestoInformes(this);
    }

}
