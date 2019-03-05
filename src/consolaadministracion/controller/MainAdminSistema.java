package consolaadministracion.controller;

import consolaadministracion.controller.consolas.ControladorConsolaASistemas;
import consolaadministracion.controller.dialogos.ControladorDialogoCamposTicket;
import consolaadministracion.model.excepciones.ExcepcionConsola;
import consolaadministracion.model.GestionaAdminSistema;
import consolaadministracion.model.asistema.HiloGestionaConexion;
import consolaadministracion.model.types.Ticket;
import javax.swing.JOptionPane;

/**
 *
 *
 * @author Marco Borreguero
 */
public class MainAdminSistema { //Super Controlador o controlador principal del Administrador de Sistemas

    private static GestionaAdminSistema modeloAS;
    private static ControladorConsolaASistemas controladorAS;
    private static HiloGestionaConexion hiloConexiones;

    /**
     * Constructor del super controlador del A. Sistema
     */
    public MainAdminSistema() {
    }

    /**
     * Main ejecutable de la APP del A. Reserva
     *
     * @param args
     */
    public static void main(String[] args) {

        MainAdminSistema main = new MainAdminSistema();
        main.launch();

    }

    /**
     *
     * Actualiza la vista principal con los nuevos datos entrantes
     *
     */
    public void actualizaConsolaASistemas() {
        if (controladorAS != null) {
            controladorAS.cargarListaAdminsR();
            controladorAS.actualizar();
        }
    }

    /**
     * Muestra el dialogo con los campos de un ticket en modo lectura
     *
     *
     * @param t ticket a leer
     */
    public void muestraDialogoCamposTicketLectura(Ticket t) {
        ControladorDialogoCamposTicket dct = new ControladorDialogoCamposTicket(modeloAS, this, false, t);
        dct.abrir();
    }

    /**
     *
     * Muestra el dialogo con los campos de un ticket nuevo para responder un
     * caso ya existente
     *
     *
     * @param t nuevo ticket con datos previos del Caso
     */
    public void muestraDialogoCamposTicketRespondeCaso(Ticket t) {

        ControladorDialogoCamposTicket dct = new ControladorDialogoCamposTicket(modeloAS, this, true, t);
        dct.abrir();
    }

    /**
     * Método que inicia la APP del A. Sistemas. Inicia la vista, y todas las
     * clases que van a participiar inicialmente en el proceso de ejecución:
     * Modelo, Controlador, Vista, etc.
     *
     * Además, inicia el hilo que estará esperando en segundo plano las nuevas
     * conexiones de los A. Reserva
     */
    public void launch() {
        try {
            this.modeloAS = new GestionaAdminSistema();
            this.controladorAS = new ControladorConsolaASistemas(modeloAS, this);
            this.hiloConexiones = new HiloGestionaConexion(modeloAS, controladorAS);
            controladorAS.abrir();
            new Thread(hiloConexiones).start();
            Thread t1 = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        controladorAS.produce();
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            });

            // Create consumer thread 
            Thread t2 = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        controladorAS.consume();
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            });
            // Start both threads 
            t1.start();
            t2.start();

            // t1 finishes before t2 
            t1.join();
            t2.join();
        } catch (InterruptedException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.INFORMATION_MESSAGE);
        } catch (ExcepcionConsola ex) {
            JOptionPane.showMessageDialog(null, "No se ha podido realizar la conexión con la Base de Datos", "Error", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }
    }
}
