package Auxiliares;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import static Auxiliares.Colores.*;

public class Log {
    public static PrintWriter log;

    public static void crearLog(){
        try {
            log = new PrintWriter(new File(".\\auxiliares\\log.txt"));
        } catch (IOException e) {
            System.err.println("Ha ocurrido un error de tipo " + e);
        }
    }

    public static synchronized void escribirLOG(String texto) {
        log.println(texto);
        log.flush();
    }

}
