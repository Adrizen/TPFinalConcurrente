
import java.time.LocalTime;

public class Vuelo implements Runnable{
    private int reserva;            // Indica en qué número de aerolinea tiene reserva el pasajero.
    private int puestoDeEmbarque;
    private char terminal;
    private LocalTime horaVuelo;          // Indica a qué hora es el vuelo del pasajero.
    private Tiempo horaInicialAeropuerto;

    private boolean despego;

    public Vuelo(int r, Tiempo t, LocalTime hv) {
        this.reserva = r;
        this.despego = false;
        this.horaInicialAeropuerto = t;
        this.horaVuelo = hv;
    }

    public void run(){
        try {
            while (!despego){
                System.out.println("hora airport: " + horaInicialAeropuerto.getHora() + " . Hora vuelo: " + horaVuelo.getHour());
                if (horaInicialAeropuerto.getHora() == horaVuelo.getHour()){
                    synchronized (this){
                        this.notifyAll();
                    }
                    despego = true;
                }
                Thread.sleep(10000);    // espera 10 segundos.
            }
        } catch (InterruptedException e) {
            System.err.println("Ha ocurrido un error de tipo " + e);
        }
    }

    public int getReserva() {
        return this.reserva;
    }

    public int getPuestoDeEmbarque() {
        return this.puestoDeEmbarque;
    }

    public char getTerminal() {
        return this.terminal;
    }

    public int getHoraVuelo(){
        return this.horaVuelo.getHour();
    }

    public void setHoraVuelo(LocalTime lt){
        this.horaVuelo = lt;
    }

    public void setTerminal(char t) {
        this.terminal = t;
    }

    public void setPuestoDeEmbarque(int p){
        this.puestoDeEmbarque = p;
    }

}
