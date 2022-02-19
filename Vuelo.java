
import java.time.LocalTime;

public class Vuelo {
    private int reserva;            // Indica en qué número de aerolinea tiene reserva el pasajero.
    private int puestoDeEmbarque;
    private char terminal;
    private LocalTime horaVuelo;          // Indica a qué hora es el vuelo del pasajero.

    public Vuelo(int r) {
        this.reserva = r;
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
