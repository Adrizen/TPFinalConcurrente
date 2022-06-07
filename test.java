import java.time.LocalTime;
import java.util.Random;
import static Auxiliares.Log.*;

/*
    Se recomienda ejecutar test.java en VS Code. Dejar correr durante un minuto o así y luego abrir el log.txt para seguir el algoritmo.
    Utilizar la extensión 'ANSI Colors' de 'Ilia Pozdnyakov' para abrir el log.txt y poder ver los colores de cada print. Para ello, abrir 
    log.txt en una ventana de VS Code, presionar 'F1' y buscar la opción 'ANSI Text: Open Preview', en esa preview se deberían  ver los 
    colores de cada print. (se puede resaltar/seleccionar el texto de un pasajero particular para que sea "highlighted")
*/
public class test {

    public static void main(String[] args) {
        Random r = new Random();
        final int CAPACIDAD_MAXIMA_PUESTOSDEATENCION = 3; // Cuantos pasajeros como máximo puede haber esperando en un puesto de atención.
        final int CAPACIDAD_MAXIMA_TRENINTERNO = 3; // Capacidad máxima del tren interno del aeropuerto.(Por consigna, siempre debe llenarse para viajar)
        final int CAPACIDAD_MAXIMA_FREESHOP = 2; // Capacidad máxima de pasajeros en el freeshop.
        final int HORA_INICIAL_AEROPUERTO = 6; // La hora inicial del aeropuerto. (atiende de 6hs a 22hs)
        final int CANTIDAD_TERMINALES = 3; // La consigna dice que son 3 terminales: A, B y C.
        final int CANTIDAD_AEROLINEAS = 2; // Cantidad de aerolineas en el aeropuerto.
        final int CANTIDAD_PASAJEROS = 9; // Cantidad pasajeros que ingresarán al aeropuerto.
        final int CANTIDAD_VUELOS = 2; // Cantidad de vuelos.

        crearLog(); // Arhivo log que registra todos los eventos que ocurren en el aeropuerto.
        Tiempo tiempo = new Tiempo(HORA_INICIAL_AEROPUERTO); // Controla qué hora es en el aeropuerto.
        new Thread(tiempo).start(); // Iniciar el hilo del Tiempo.
        TrenInterno trenInterno = new TrenInterno(CAPACIDAD_MAXIMA_TRENINTERNO); // Tren interno para transportar pasajeros a terminal.
        new Thread(trenInterno).start();
        HallCentral hall = new HallCentral(CANTIDAD_AEROLINEAS); // Hall central compartido.
        PuestoDeAtencion[] puestosDeAtencion = new PuestoDeAtencion[CANTIDAD_AEROLINEAS]; // Arreglo de puestos de atención.
        Terminal[] terminales = new Terminal[CANTIDAD_TERMINALES]; // Arreglo de terminales.
        Vuelo[] vuelos = new Vuelo[CANTIDAD_VUELOS]; // Arreglo de vuelos.

        crearPuestosDeAtencion(puestosDeAtencion, hall, CAPACIDAD_MAXIMA_PUESTOSDEATENCION, terminales);
        crearTerminales(terminales, CAPACIDAD_MAXIMA_FREESHOP);
        Aeropuerto aeropuerto = new Aeropuerto(puestosDeAtencion, CAPACIDAD_MAXIMA_TRENINTERNO, terminales, trenInterno, tiempo);
        crearVuelos(vuelos, CANTIDAD_VUELOS, CANTIDAD_AEROLINEAS, tiempo, r);
        crearPasajeros(CANTIDAD_PASAJEROS, aeropuerto, vuelos, CANTIDAD_VUELOS, r);
    }

    // CREACIÓN PUESTOS DE ATENCIÓN.
    public static void crearPuestosDeAtencion(PuestoDeAtencion[] puestosDeAtencion, HallCentral hall, int CAPACIDAD_MAXIMA_PUESTOSDEATENCION, Terminal[] terminales) {
        for (int i = 0; i < puestosDeAtencion.length; i++) {
            puestosDeAtencion[i] = new PuestoDeAtencion(hall, CAPACIDAD_MAXIMA_PUESTOSDEATENCION, terminales);
        }
    }

    public static void crearTerminales(Terminal[] terminales, int CAPACIDAD_MAXIMA_FREESHOP) {
        for (int i = 0; i < terminales.length; i++) {
            terminales[i] = new Terminal((char) (65 + i), CAPACIDAD_MAXIMA_FREESHOP);
            // se asigna las letras A, B, C, ... a cada terminal. Cada terminal tiene un FreeShop con una capacidad máxima definida.
        }
    }

    // CREACIÓN VUELOS.
    public static void crearVuelos(Vuelo[] vuelos, int CANTIDAD_VUELOS, int CANTIDAD_AEROLINEAS, Tiempo tiempo, Random r) {
        int aerolineaAleatoria = 0;
        for (int i = 0; i < CANTIDAD_VUELOS; i++) {
            aerolineaAleatoria = r.nextInt(CANTIDAD_AEROLINEAS); // Se selecciona una aerolinea de manera aleatoria.
            // Se crea un vuelo y se le asigna la aerolinea del paso anterior.
            int horaVueloAleatoria = r.nextInt(11) + 12; // Entero aleatorio para el vuelo, entre 00hs y 23hs
            LocalTime hora = LocalTime.of(horaVueloAleatoria, 00); // Hora aleatoria para el vuelo, entre 00hs y 23hs
            Vuelo vuelo = new Vuelo("vuelo" + i, aerolineaAleatoria, tiempo, hora);
            vuelos[i] = vuelo; // Se agrega el vuelo recien creado al arreglo de vuelos.
            asignarPuestoDeEmbarque(aerolineaAleatoria, horaVueloAleatoria, vuelo, r);
            new Thread(vuelo).start(); // Se inicia el hilo del vuelo.
        }
    }

    private static void asignarPuestoDeEmbarque(int aerolineaAleatoria, int horaVueloAleatoria, Vuelo vuelo, Random r) {
        r = new Random(aerolineaAleatoria + horaVueloAleatoria);
        // A continuación se genera un puesto de embarque de manera aleatoria para el pasajero. Entre 1 y 20.
        int puestoDeEmbarque = r.nextInt(21 - 1) + 1; 
        vuelo.setPuestoDeEmbarque(puestoDeEmbarque);
    }

    // CREACIÓN PASAJEROS.
    public static void crearPasajeros(int CANTIDAD_PASAJEROS, Aeropuerto aeropuerto, Vuelo[] vuelos, int CANTIDAD_VUELOS, Random r) {
        // CREACIÓN PASAJEROS.
        for (int i = 0; i < CANTIDAD_PASAJEROS; i++) {
            // Se crea el hilo del Pasajero con su nombre, el aeropuerto y un vuelo aleatorio del arreglo de todos los vuelos.
            new Thread(new Pasajero("pasajero" + i, aeropuerto, vuelos[r.nextInt(CANTIDAD_VUELOS)])).start();
            // Nota: A la hora de crear el vuelo, su n° de aerolinea es creada de manera aleatoria.
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

    Las aerolineas se generan de manera aleatoria (como dice el enunciado) de entre todas las aerolineas que hay en el aeropuerto.
    n° aerolineas = n° puesto de atención = n° aerolinea.

    Mecanismos de sincronización utilizados:
    TrenInterno: CyclicBarrier. (Los pasajeros van entrando al tren y cuando este llega a su capacidad máxima, comienza a viajar.)
    HallCentral: Semaphore[]. (Los pasajeros esperan en el hall central porque su puesto de atención está lleno. Cuando se libera un lugar en
        x puesto de atención, un guardia avisa a los pasajeros de ese puesto de atención x, que hay un lugar libre.)
    PuestoDeAtencion: ArrayBlockingQueue. (Los pasajeros se van colocando en esta cola para ir siendo atendidos en orden de llegada.)
                      ReentrantLock. (Utilizado para mutex a la hora de cambiar variables compartidas.)
    Vuelos: Monitores. (Los pasajeros esperan en el monitor de su vuelo a que sea la hora de partir.)
*/