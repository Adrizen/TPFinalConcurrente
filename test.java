import java.time.LocalTime;
import java.util.Random;
import static Auxiliares.Log.*;

public class test {

    public static void main(String[] args) {
        Random r = new Random();
        final int CAPACIDAD_MAXIMA_PUESTOSDEATENCION = 2; // Cuantos pasajeros como máximo puede haber esperando en un puesto de atención.
        final int CAPACIDAD_MAXIMA_TRENINTERNO = 2; // Capacidad máxima del tren interno del aeropuerto. (Siempre debe llenarse para viajar x consigna)
        final int CAPACIDAD_MAXIMA_FREESHOP = 5; // Capacidad máxima de pasajeros en el freeshop.
        final int HORA_INICIAL_AEROPUERTO = 6; // La hora inicial del aeropuerto. (atiende de 6hs a 22hs)
        final int CANTIDAD_TERMINALES = 3; // La consigna dice que son 3 terminales: A, B y C.
        final int CANTIDAD_AEROLINEAS = 10; // Cantidad de aerolineas en el aeropuerto.
        final int CANTIDAD_PASAJEROS = 2; // Cantidad pasajeros que ingresarán al aeropuerto.
        final int CANTIDAD_VUELOS = 5; // Cantidad de vuelos.

        crearLog(); // Arhivo log que registra todos los eventos que ocurren en el aeropuerto.
        Tiempo tiempo = new Tiempo(HORA_INICIAL_AEROPUERTO); // Controla qué hora es en el aeropuerto.
        new Thread(tiempo).start(); // Iniciar el hilo del Tiempo.
        TrenInterno trenInterno = new TrenInterno(CAPACIDAD_MAXIMA_TRENINTERNO); // Tren interno para transportar pasajeros a terminal.
        HallCentral hall = new HallCentral(CANTIDAD_AEROLINEAS); // Hall central compartido.
        PuestoDeAtencion[] puestosDeAtencion = new PuestoDeAtencion[CANTIDAD_AEROLINEAS]; // Arreglo de puestos de atención.
        Terminal[] terminales = new Terminal[CANTIDAD_TERMINALES]; // Arreglo de terminales.
        Vuelo[] vuelos = new Vuelo[CANTIDAD_VUELOS]; // Arreglo de vuelos.

        crearPuestosDeAtencion(puestosDeAtencion, hall, CAPACIDAD_MAXIMA_PUESTOSDEATENCION);
        crearTerminales(terminales, CAPACIDAD_MAXIMA_FREESHOP);
        Aeropuerto aeropuerto = new Aeropuerto(puestosDeAtencion, CAPACIDAD_MAXIMA_TRENINTERNO, terminales, trenInterno, tiempo);
        crearVuelos(vuelos, CANTIDAD_VUELOS, CANTIDAD_AEROLINEAS, tiempo, r);
        crearPasajeros(CANTIDAD_PASAJEROS, aeropuerto, vuelos, CANTIDAD_VUELOS, r);
    }

    // CREACIÓN PUESTOS DE ATENCIÓN.
    public static void crearPuestosDeAtencion(PuestoDeAtencion[] puestosDeAtencion, HallCentral hall, int CAPACIDAD_MAXIMA_PUESTOSDEATENCION) {
        for (int i = 0; i < puestosDeAtencion.length; i++) {
            puestosDeAtencion[i] = new PuestoDeAtencion(hall, CAPACIDAD_MAXIMA_PUESTOSDEATENCION);
        }

    }

    public static void crearTerminales(Terminal[] terminales, int CAPACIDAD_MAXIMA_FREESHOP) {
        for (int i = 0; i < terminales.length; i++) {
            terminales[i] = new Terminal((char) (65 + i), CAPACIDAD_MAXIMA_FREESHOP);
            // se asigna las letras A, B y C a cada terminal. Cada terminal tiene un FreeShop con una capacidad máxima definida.
        }
    }

    // CREACIÓN VUELOS.
    public static void crearVuelos(Vuelo[] vuelos, int CANTIDAD_VUELOS, int CANTIDAD_AEROLINEAS, Tiempo tiempo, Random r) {
        int reservaAleatoria = 0;
        for (int i = 0; i < CANTIDAD_VUELOS; i++) {
            reservaAleatoria = r.nextInt(CANTIDAD_AEROLINEAS); // Se selecciona una aerolinea de manera aleatoria.
            // Se crea un vuelo y se le asigna la aerolinea del paso anterior.
            int horaVueloAleatoria = r.nextInt(23) + 1; // Entero aleatorio para el vuelo, entre 00hs y 23hs
            LocalTime hora = LocalTime.of(horaVueloAleatoria, 00); // Hora aleatoria para el vuelo, entre 00hs y 23hs
            Vuelo vuelo = new Vuelo("vuelo" + i, reservaAleatoria, tiempo, hora);
            vuelos[i] = vuelo; // Se agrega el vuelo recien creado al arreglo de vuelos.
            asignarPuestoDeEmbarque(reservaAleatoria, horaVueloAleatoria, vuelo, r);
            new Thread(vuelo).start(); // Se inicia el hilo del vuelo.
        }
    }

    private static void asignarPuestoDeEmbarque(int reservaAleatoria, int horaVueloAleatoria, Vuelo vuelo, Random r) {
        r = new Random(reservaAleatoria + horaVueloAleatoria); // TODO: Semilla custom epica
        int puestoDeEmbarque = r.nextInt(21 - 1) + 1; // Se genera un puesto de embarque de manera aleatoria para el pasajero.
        vuelo.setPuestoDeEmbarque(puestoDeEmbarque);
    }

    // CREACIÓN PASAJEROS.
    public static void crearPasajeros(int CANTIDAD_PASAJEROS, Aeropuerto aeropuerto, Vuelo[] vuelos, int CANTIDAD_VUELOS, Random r) {
        // CREACIÓN PASAJEROS.
        for (int i = 0; i < CANTIDAD_PASAJEROS; i++) {
            // Se crea el hilo del Pasajero con su nombre, el aeropuerto y un vuelo aleatorio del arreglo de todos los vuelos.
            new Thread(new Pasajero("pasajero" + i, aeropuerto, vuelos[r.nextInt(CANTIDAD_VUELOS)])).start();
            // Nota: A la hora de crear el vuelo, su n° de reserva es creada de manera aleatoria.
        }
    }

}

/*
Cuando un pasajero entra al aeropuerto, se dirige a un puesto de informes y desde 
    allí a un puesto de atención (hay tantos puestos de atención como aerolineas) según su aerolinea.
        Cada puesto de atención tiene una capacidad máxima y se atiende por orden de llegada.
        Los que llegan después que se llenó, esperan en un hall y un guardia les va avisando cuando se desocupa un lugar.
    Cuando un pasajero hace el check-in, se le informa su terminal y puesto de embarque. A continuación se lo traslada a la terminal y 
        puesto de embarque que le corresponde mediante un tren interno.

    En el aeropuerto hay 3 terminales (A, B y C). Cada una tiene puestos de embarque y una sala de embarques compartida.
        A: Puestos de embarque 1 a 7.
        B: Puestos de embarque 8 a 15.
        C: Puestos de embarque 16 a 20.

    Cada pasajero puede ir al free-shop (si le sobra bastante tiempo) mientras espera su vuelo o simplemente esperar sentado.
    En cada terminal hay un free-shop. Cada uno tiene una capacidad máxima "lugar". Hay 2 cajas para procesar los pagos.

    En el aeropuerto hay un tren interno que traslada los pasajeros a la terminal que le corresponde a cada uno. Cuando llega al final del recorrido
    el tren se vacia y vuelve al inicio del recorrido. El tren sale de nuevo una vez que se llena por completo nuevamente.


*/
