package Auxiliares;

import java.sql.Date;
import java.text.SimpleDateFormat;

/**
 *
 * @author Guillermo Andrés Pereyra.
 * From: https://stackoverflow.com/questions/10364383/how-to-transform-currenttimemillis-to-a-readable-date-format
 * Usar 'import static Auxiliares.Timestamp.*' en la clase a utilizar, luego en el print que se desea usar el timestamp se debe
 * concatenar el método estático 'obtenerTimestamp()'.
 */

public class Timestamp {
    private static long currentTimeMillis;   
    private static SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss"); // Formato del Timestamp. Hora:Minutos:Segundos
    public static Date resultDate;          

    // Devuelve un String con la Hora:Minutos:Segundos actuales.
    public static String obtenerTimestamp(){
        currentTimeMillis = System.currentTimeMillis(); // Tiempo actual del sistema en milisegundos.
        resultDate = new Date(currentTimeMillis);       // Crear una fecha con el tiempo actual y formato dado.
        return sdf.format(resultDate);
    }

}
