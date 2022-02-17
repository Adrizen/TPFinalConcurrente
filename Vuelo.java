
public class Vuelo {
    private int reserva;            // Indica en qué número de aerolinea tiene reserva el pasajero.
    private int puestoDeEmbarque;
    private char terminal;

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

    public void setTerminal(char t) {
        this.terminal = t;
    }

    public void setPuestoDeEmbarque(int p){
        this.puestoDeEmbarque = p;
    }

}
