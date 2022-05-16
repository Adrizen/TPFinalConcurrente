
import static Auxiliares.Colores.*;
import static Auxiliares.Log.*;

public class Terminal {

    private char letra;
    private FreeShop freeShop;

    public Terminal(char l, int CAPACIDAD_MAXIMA) {
        this.letra = l;
        this.freeShop = new FreeShop(CAPACIDAD_MAXIMA);
    }

    public void intentarIngresarFreeShop(Pasajero pasajero) {
        if (freeShop.intentarIngresar()) {
            freeShop.ingresar(pasajero, letra);
        } else {
            escribirLOG(RED_BOLD + pasajero.getNombre() + " quiso entrar al FreeShop de su terminal "+ letra +" pero estaba lleno." + RESET);
        }
    }

    public char getLetra(){
        return this.letra;
    }

}
