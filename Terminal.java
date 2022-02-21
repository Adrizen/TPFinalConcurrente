import java.util.concurrent.Semaphore;

public class Terminal {

    private char letra;
    private FreeShop freeShop;
    private Tiempo horario;

    public Terminal(char l, int CAPACIDAD_MAXIMA, Tiempo h) {
        this.letra = l;
        this.freeShop = new FreeShop(CAPACIDAD_MAXIMA);
        this.horario = h;
    }

    public void intentarIngresarFreeShop(Pasajero pasajero) {
        if (freeShop.intentarIngresar()) {
            freeShop.ingresar(pasajero);
        }
    }

}
