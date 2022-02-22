import java.time.LocalTime;
import static Auxiliares.Colores.*;

public class Tiempo implements Runnable { // Este hilo controla la hora del aeropuerto.

    private LocalTime horaActual;

    public Tiempo(int h) {
        this.horaActual = LocalTime.of(h, 00);
    }

    public void run() {
        while (true) {
            try {
                System.out.println(CYAN_BOLD + "---- Hora aeropuerto: " + this.getHora() + " ----" + RESET);
                Thread.sleep(10000); // cada 10 segundos, pasa una hora en el aeropuerto.
                incrementarHora();
            } catch (InterruptedException e) {
                System.out.println("Ha ocurrido un error de tipo " + e);
            }
        }
    }

    public int getHora() {
        return this.horaActual.getHour();
    }

    // Aumenta la hora actual en 1.
    private void incrementarHora() {
        this.horaActual = this.horaActual.plusHours(1);
    }

}
