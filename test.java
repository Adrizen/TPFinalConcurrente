

public class test {
    public static void main(String[] args) {
        final int CANTIDAD_PASAJEROS = 5;
        Aeropuerto aeropuerto = new Aeropuerto();

        for (int i = 0; i < CANTIDAD_PASAJEROS; i++) {
            new Thread(new Pasajero("pasajero" + i, aeropuerto)).start();
        }

    }
}

/*
Como abordar este tipo de problemas:
    -Identificar los hilos: Pasajero, TrenInterno.
	-Identificar las entidades que forman parte del problema: Aeropuerto, Pasajero, Aerolineas, PuestoDeAtencion, Terminal, FreeShop, TrenInterno.
	-Identificar las clases correspondientes: Aeropuerto, Pasajero, TrenInterno, PuestoDeInformes.
Identificar recursos compartidos:
	-Es necesario asociar herramientas de sincronización para garantizar seguridad: Aeropuerto.
Identificar los eventos de sincronización:
Cuando un pasajero entra al aeropuerto, se dirige a un puesto de informes (son X) y desde allí a un puesto de atención según su vuelo y aerolinea.
        Cada puesto de atención tiene una capacidad máxima y se atiende por orden de llegada.
        Los que llegan después que se llenó, esperan en un hall y un guardia les va avisando cuando se desocupa un lugar.
    Cuando un pasajero hace el check-in, se le informa su terminal y puesto de embarque. A continuación se lo traslada a la terminal y puesto de
    embarque que le corresponde mediante un tren interno.

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
