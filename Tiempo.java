public class Tiempo implements Runnable {

    private int horaActual;
    
    public Tiempo(int h){
        this.horaActual = h;    // la hora inicial son es 6hs.
    }

    public void run(){
        try {
            Thread.sleep(10000);    // cada 10 segundos, pasa una hora en el aeropuerto.
            incrementarHora();
        } catch (InterruptedException e) {
            System.out.println("Ha ocurrido un error de tipo " + e);
        }
    }

    public int getHora(){
        return this.horaActual;
    }

    // Aumenta la hora actual en 1.
    private void incrementarHora(){
        this.horaActual = this.horaActual + 1;
    }

}
