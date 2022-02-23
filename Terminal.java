import java.util.concurrent.Semaphore;

public class Terminal {

    private char letra;
    private FreeShop freeShop;

    public Terminal(char l, int CAPACIDAD_MAXIMA) {
        this.letra = l;
        this.freeShop = new FreeShop(CAPACIDAD_MAXIMA);
    }

    public void intentarIngresarFreeShop(Pasajero pasajero) {
        if (freeShop.intentarIngresar()) {
            freeShop.ingresar(pasajero);
        } else {
            System.out.println(pasajero.getNombre() + " quiso entrar al FreeShop pero estaba lleno.");
        }
    }

}
