import java.time.LocalTime;
import java.util.Random;

public class test {

    public static void main(String[] args) {
        Random r = new Random();
        final int CANTIDAD_PASAJEROS = 2; // cantidad pasajeros que ingresarán al aeropuerto.
        final int CANTIDAD_AEROLINEAS = 2; // cantidad de aerolineas en el aeropuerto. (existe un puesto de atención x aerolinea)
        final int CAPACIDAD_MAXIMA_PUESTOSDEATENCION = 2; // cuantos pasajeros puede haber esperando en un puesto de atención.
        final int CAPACIDAD_MAXIMA_TRENINTERNO = 2; // capacidad máxima del tren interno del aeropuerto.
        final int CAPACIDAD_MAXIMA_FREESHOP = 5;
        final int HORA_INICIAL_AEROPUERTO = 6; // la hora inicial del aeropuerto. (atiende de 6hs a 22hs)
        final int CANTIDAD_TERMINALES = 3; // la consigna dice que son 3 terminales: A, B y C.

        Tiempo tiempo = new Tiempo(HORA_INICIAL_AEROPUERTO); // controla qué hora es en el aeropuerto.
        new Thread(tiempo).start();
        TrenInterno trenInterno = new TrenInterno(CAPACIDAD_MAXIMA_TRENINTERNO);
        HallCentral hall = new HallCentral(CANTIDAD_AEROLINEAS);
        PuestoDeAtencion[] puestoDeAtencion = new PuestoDeAtencion[CANTIDAD_AEROLINEAS];
        Terminal[] terminales = new Terminal[CANTIDAD_TERMINALES];

        Vuelo[] vuelos = new Vuelo[CANTIDAD_AEROLINEAS];    // Arreglo de vuelos.

        // CREACIÓN PUESTOS DE ATENCIÓN.
        for (int i = 0; i < puestoDeAtencion.length; i++) {
            puestoDeAtencion[i] = new PuestoDeAtencion(hall, CAPACIDAD_MAXIMA_PUESTOSDEATENCION);
        }

        // CREACIÓN TERMINALES.
        for (int i = 0; i < terminales.length; i++) {
            terminales[i] = new Terminal((char) (65 + i), CAPACIDAD_MAXIMA_FREESHOP, tiempo);
            // se asigna las letras A, B y C a las terminales. Cada terminal tiene un FreeShop con una capacidad máxima definida.
        }

        Aeropuerto aeropuerto = new Aeropuerto(puestoDeAtencion, CAPACIDAD_MAXIMA_TRENINTERNO, terminales, trenInterno, tiempo);

        // CREACIÓN VUELOS.
        int reservaAleatoria = 0;
        for (int i = 0; i < CANTIDAD_AEROLINEAS; i++) {
            reservaAleatoria = r.nextInt(CANTIDAD_AEROLINEAS); // Se selecciona una aerolinea de manera aleatoria.
            // Se crea un vuelo y se le asigna la aerolinea del paso anterior.
            int horaVueloAleatoria = r.nextInt(23) + 1;
            LocalTime hora = LocalTime.of(horaVueloAleatoria, 00);
            Vuelo vuelo = new Vuelo(reservaAleatoria, tiempo,hora);
            System.out.println("vuelo" + i + " hora: " + hora.getHour());
            vuelos[i] = vuelo;         // Se agrega el vuelo recien creado al arreglo de vuelos.
            new Thread(vuelo).start(); // Se inicia el hilo del vuelo.
        }

        // CREACIÓN PASAJEROS.
        for (int i = 0; i < CANTIDAD_PASAJEROS; i++) {
            // Se crea el hilo del Pasajero con su nombre, el aeropuerto y un vuelo aleatorio del arreglo de todos los vuelos.
            new Thread(new Pasajero("pasajero" + i, aeropuerto, vuelos[reservaAleatoria])).start();
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
