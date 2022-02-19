public class Terminal implements Runnable {
    
    private char letra;
    private FreeShop freeShop;

    public Terminal(char l, int CAPACIDAD_MAXIMA){
        this.letra = l;
        this.freeShop = new FreeShop(CAPACIDAD_MAXIMA);
    }

    public void run(){

    }

}
