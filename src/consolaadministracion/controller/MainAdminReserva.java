package consolaadministracion.controller;

import consolaadministracion.controller.consolas.ControladorConsolaAReservas;
import consolaadministracion.controller.dialogos.ControladorDialogoCamposCorreo;
import consolaadministracion.controller.dialogos.ControladorDialogoCamposTicket;
import consolaadministracion.model.excepciones.ExcepcionConsola;
import consolaadministracion.model.GestionaAdminReservas;
import consolaadministracion.model.areserva.HiloRecibeRespuesta;
import consolaadministracion.model.types.Prioridad;
import consolaadministracion.model.types.Ticket;
import javax.swing.JOptionPane;

/**
 * Super Controlador o controlador principal del Administrador de Reserva
 *
 *
 * @author Marco Borreguero
 */
public class MainAdminReserva { //Super Controlador o controlador principal del Administrador de Reserva

    private static GestionaAdminReservas modeloAR; //Modelo de la aplicación (cliente) del A. Reserva
    private static ControladorConsolaAReservas controladorAR; //Controlador de la vista principal del A. Reserva

    /**
     * Constructor del super controlador del A. Reserva
     */
    public MainAdminReserva() {
    }

    /**
     * Main ejecutable de la APP del A. Reserva
     *
     * @param args
     */
    public static void main(String[] args) {

        MainAdminReserva main = new MainAdminReserva();
        main.launch(); // Inicia la APP

    }

    /**
     * Muestra la vista principal del A. Reservas
     */
    public void muestraConsolaAReservas() {
        if (controladorAR != null) {
            controladorAR.cargarListaCasos();
            controladorAR.actualizar();
        }
    }

    /**
     * Muestra el dialogo con los campos de un email en modo lectura
     *
     * @param correo Correo electrónico a leer
     */
    public void muestraDialogoCamposCorreoLectura(String[] correo) {

        ControladorDialogoCamposCorreo dcc = new ControladorDialogoCamposCorreo(modeloAR, this, correo);
        dcc.abrir();

    }

    /**
     * Muestra el dialogo con los campos de un email en modo editable para
     * enviar un nuevo email
     *
     */
    public void muestraDialogoCamposCorreoEnvio() {

        ControladorDialogoCamposCorreo dcc = new ControladorDialogoCamposCorreo(modeloAR, this, null);
        dcc.abrir();

    }

    /**
     * Muestra el dialogo con los campos de un ticket en modo lectura
     *
     *
     * @param t ticket a leer
     */
    public void muestraDialogoCamposTicketLectura(Ticket t) {

        ControladorDialogoCamposTicket dct = new ControladorDialogoCamposTicket(modeloAR, this, false, false, t);
        dct.abrir();

    }

    /**
     *
     * Muestra el dialogo con los campos de un nuevo caso a rellenar
     *
     *
     */
    public void muestraDialogoCamposTicketNuevoCaso() {

        Ticket t = new Ticket(modeloAR.getIdAdminR(), modeloAR.getN_casos() + 1, 1, "", Prioridad.MEDIA, "");
        ControladorDialogoCamposTicket dct = new ControladorDialogoCamposTicket(modeloAR, this, true, true, t);
        dct.abrir();

    }

    /**
     *
     * Muestra el dialogo con los campos de un ticket nuevo para un caso ya
     * existente
     *
     *
     * @param t nuevo ticket con datos previos del Caso
     */
    public void muestraDialogoCamposTicketContinuaCaso(Ticket t) {

        ControladorDialogoCamposTicket dct = new ControladorDialogoCamposTicket(modeloAR, this, true, false, t);
        dct.abrir();

    }

    /**
     * Método que inicia la APP del A. Reservas. Inicia la vista, y todas las
     * clases que van a participiar inicialmente en el proceso de ejecu´ción:
     * Modelo, Controlador, Vista, etc.
     */
    public void launch() {

        String idString = JOptionPane.showInputDialog("Introduzca su nº de Administrador:");

        try {
            Integer idAdminR = Integer.valueOf(idString);
            modeloAR = new GestionaAdminReservas(idAdminR);
            this.controladorAR = new ControladorConsolaAReservas(this, modeloAR);
            controladorAR.abrir();

            HiloRecibeRespuesta hrr = new HiloRecibeRespuesta(modeloAR, controladorAR);
            new Thread(hrr).start();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Identificador no válido", "Error", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        } catch (ExcepcionConsola ex) {
            JOptionPane.showMessageDialog(null, "No se ha podido conectar con el servidor. Por favor inténtelo de nuevo más tarde", "Error", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }
    }
}
