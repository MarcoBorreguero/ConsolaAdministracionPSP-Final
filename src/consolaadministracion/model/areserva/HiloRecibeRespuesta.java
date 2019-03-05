package consolaadministracion.model.areserva;

import consolaadministracion.controller.consolas.ControladorConsolaAReservas;
import consolaadministracion.model.GestionaAdminReservas;
import consolaadministracion.model.types.Caso;
import consolaadministracion.model.types.Ticket;
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 *
 * Hilo encargado de recibir las respuestas por parte del Administrador de
 * Sistemas
 *
 * @author Marco Borreguero
 */
public class HiloRecibeRespuesta implements Runnable {

    private GestionaAdminReservas modelo;
    private ControladorConsolaAReservas car;

    /**
     *
     * @param modeloAR
     * @param car
     */
    public HiloRecibeRespuesta(GestionaAdminReservas modeloAR, ControladorConsolaAReservas car) {
        this.modelo = modeloAR;
        this.car = car;
    }

    @Override
    public void run() {

        ObjectInputStream fentrada = null;

        try {
            fentrada = new ObjectInputStream(modelo.getS().getInputStream());
            while (true) {
                String cadena = "";
                Ticket t = null;
                try {
                    Object o = fentrada.readObject(); //Se leen objetos recibidos
                    if (o.getClass().equals(String.class)) {
                        cadena = (String) o;
                        if (cadena.trim().equals("#")) {// Si se recibe la cadena "#", el Servidor ha cerrado la conexión
                            fentrada.close();
                            modelo.getS().close(); //Se cierra la conexión
                            break;
                        }
                    }
                    //Se ha recibido un ticket
                    if (o.getClass().equals(Ticket.class)) {
                        t = (Ticket) o;
                        for (Caso c : car.getCasos()) {
                            if (c.getId() == t.getCasoId()) {//Se ha recibido un ticket para un caso existente
                                c.getTickets().add(t); //añado el ticket al caso
                                car.cargarListaCasos();
                                car.actualizar();
                                break;
                            }
                        }
                    }
                } catch (IOException ex) {
                    car.lanzaExcepcion(ex.getMessage());
                    break;
                } catch (ClassNotFoundException ex) {
                    car.lanzaExcepcion(ex.getMessage());
                    break;
                }
            } //FIN WHILE
            try {
                fentrada.close();
                modelo.getS().close();
            } catch (IOException ex) {
            }
        } catch (IOException ex) {
            car.lanzaExcepcion(ex.getMessage());
        } finally {
            try {
                fentrada.close();
            } catch (IOException ex) {
            }
        }

    }

}
