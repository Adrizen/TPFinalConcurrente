
public class test {

    public static void main(String[] args) {
        final int CANTIDAD_PASAJEROS = 6;
        final int CANTIDAD_TERMINALES = 2;
        final int CAPACIDAD_MAXIMA_PUESTOSDEATENCION = 2;
        final int CAPACIDAD_MAXIMA_TRENINTERNO = 5;

        HallCentral hall = new HallCentral();
        PuestoDeAtencion[] puestoDeAtencion = new PuestoDeAtencion[2];
        for (int i = 0; i < puestoDeAtencion.length; i++) {
            puestoDeAtencion[i] = new PuestoDeAtencion(hall,CAPACIDAD_MAXIMA_PUESTOSDEATENCION);
        }

        Aeropuerto aeropuerto = new Aeropuerto(hall,puestoDeAtencion,CAPACIDAD_MAXIMA_TRENINTERNO);   //no haria falta que el airport tenga el hall

        int numeroReservaPasajero = 0;  // temporal, luego tiene que ser aleatorio.
        for (int i = 0; i < CANTIDAD_PASAJEROS; i++) {
            new Thread(new Pasajero("pasajero" + i, aeropuerto,numeroReservaPasajero)).start();
        }


    }


}

/*
Como abordar este tipo de problemas:
    -Identificar los hilos: Pasajero, TrenInterno.
	-Identificar las entidades que forman parte del problema: Aeropuerto, Pasajero, Aerolineas, PuestoDeAtencion, Terminal, 
        FreeShop, TrenInterno.
	-Identificar las clases correspondientes: Aeropuerto, Pasajero, TrenInterno, PuestoDeInformes.
Identificar recursos compartidos: Aeropuerto, PuestoDeInformes
	-Es necesario asociar herramientas de sincronización para garantizar seguridad.
Identificar los eventos de sincronización:
Cuando un pasajero entra al aeropuerto, se dirige a un puesto de informes (hay tantos puestos de atención como aerolineas) y desde 
    allí a un puesto de atención según su vuelo y aerolinea.
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

Observaciones:
        Mozo y Cocinero se dedican a su hobbie si no hay Empleados.
        Empleado puede pedir comida, bebida o ambas.
        A la hora de pedir el menú:
            1 = solo bebida.
            2 = solo comida.
            3 = comida y bebida.
*/
