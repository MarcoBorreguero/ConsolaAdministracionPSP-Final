package consolaadministracion.controller.dialogos;

import consolaadministracion.controller.MainAdminReserva;
import consolaadministracion.controller.MainAdminSistema;
import consolaadministracion.model.GestionaAdminReservas;
import consolaadministracion.model.GestionaAdminSistema;
import consolaadministracion.model.excepciones.ExcepcionConsola;
import consolaadministracion.model.types.Estado;
import consolaadministracion.model.types.Prioridad;
import consolaadministracion.model.types.Ticket;
import consolaadministracion.vista.DialogoCamposTicket;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.DefaultComboBoxModel;

/**
 * Clase controladora de los diálogos en los que se pueden leer y crear nuevos
 * tickets o casos
 *
 * @author Marco Borreguero
 */
public class ControladorDialogoCamposTicket implements ActionListener {

    //VISTA A CONTROLAR
    private DialogoCamposTicket dialogo;

    //MODELOS SEGÚN EL ADMIN
    private GestionaAdminReservas gcar;
    private GestionaAdminSistema gsas;

    //CONTROLADORES PRINCIPALES SEGÚN EL ADMIN
    private MainAdminReserva carapp;
    private MainAdminSistema casapp;

    //VARIABLES QUE CONFIGURAN LA MODALIDAD DEL DIÁLOGO
    private boolean editable;
    private boolean nuevocaso;

    //TICKET A MOFICAR O CREAR
    private Ticket t;

    /**
     *
     * Constructor para los A. Reserva
     *
     * @param gcar MODELO
     * @param carapp MAIN APP
     * @param editable indica si los datos serán editables
     * @param nuevocaso indica si se trata de un nuevo caso o un nuevo ticket
     * @param t ticket a modificar o leer
     */
    public ControladorDialogoCamposTicket(GestionaAdminReservas gcar, MainAdminReserva carapp, boolean editable, boolean nuevocaso, Ticket t) {

        this.gsas = null;
        this.gcar = gcar;
        this.carapp = carapp;
        this.casapp = null;
        this.editable = editable;
        this.nuevocaso = nuevocaso;
        this.t = t;

        this.dialogo = new DialogoCamposTicket();
        initConfig();
    }

    /**
     *
     * Constructor para los A. Sistema
     *
     *
     * @param gsas MODELO
     * @param casapp MAIN APP
     * @param editable indica si será editable la información mostrada
     * @param t ticket a leer o modificar
     */
    public ControladorDialogoCamposTicket(GestionaAdminSistema gsas, MainAdminSistema casapp, boolean editable, Ticket t) {

        this.gsas = gsas;
        this.gcar = null;
        this.carapp = null;
        this.casapp = casapp;
        this.editable = editable;
        this.nuevocaso = false;
        this.t = t;

        this.dialogo = new DialogoCamposTicket();
        initConfig();
    }

    /**
     * Muestra la vista
     */
    public void abrir() {
        this.dialogo.setVisible(true);
    }

    private void initConfig() { //Inicia los elementos de la vista y su modalidad

        this.dialogo.setModal(true);
        this.dialogo.getOkButton().addActionListener(this);
        this.dialogo.getCancelButton().addActionListener(this);

        DefaultComboBoxModel<String> modelEstado = new DefaultComboBoxModel<>();
        DefaultComboBoxModel<String> modelPrioridad = new DefaultComboBoxModel<>();

        if (gsas == null) {
            modelEstado.addElement(Estado.ABIERTO.toString());
            modelEstado.addElement(Estado.CERRADO.toString());

            modelPrioridad.addElement(Prioridad.ALTA.toString());
            modelPrioridad.addElement(Prioridad.BAJA.toString());
            modelPrioridad.addElement(Prioridad.MEDIA.toString());
        } else if (gsas != null && !editable) {
            modelEstado.addElement(Estado.ABIERTO.toString());
            modelEstado.addElement(Estado.CERRADO.toString());

            modelPrioridad.addElement(Prioridad.ALTA.toString());
            modelPrioridad.addElement(Prioridad.BAJA.toString());
            modelPrioridad.addElement(Prioridad.MEDIA.toString());
        } else if (gsas != null && editable) {
            modelEstado.addElement(Estado.ABIERTO.toString());
            modelPrioridad.addElement(Prioridad.RESPUESTA.toString());
            this.dialogo.getSelecPrioridad().setEnabled(false);
        }

        this.dialogo.getSelecEstado().setModel(modelEstado);
        this.dialogo.getSelecPrioridad().setModel(modelPrioridad);

        if (t != null) {
            this.dialogo.getTxtAsunto().setText(t.getAsunto());
            this.dialogo.getTxtFecha().setText(t.getFecha());
            this.dialogo.getTextDesc().setText(t.getDescripcion());
            this.dialogo.getSelecEstado().setSelectedItem(t.getEstado().toString());
            this.dialogo.getSelecPrioridad().setSelectedItem(t.getPrioridad().toString());
        }

        this.dialogo.getSelecEstado().setEnabled(false);
        this.dialogo.getTxtFecha().setEditable(false);

        this.dialogo.getSelecPrioridad().setEnabled(editable);
        this.dialogo.getTextDesc().setEditable(editable);
        this.dialogo.getTxtAsunto().setEditable(editable);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if (cmd.equals("OK")) {

            if (editable) {

                //Creo un nuevo ticket
                t.setAsunto(this.dialogo.getTxtAsunto().getText());
                t.setDescripcion(this.dialogo.getTextDesc().getText());
                t.setPrioridad(Prioridad.valueOf((String) this.dialogo.getSelecPrioridad().getSelectedItem()));

                if (nuevocaso) {
                    if (gcar != null) {
                        try {
                            gcar.nuevoCaso(t);
                            gcar.enviaTicket(t);
                        } catch (ExcepcionConsola ex) {
                            ex.printStackTrace();
                        }
                    }
                } else {
                    if (gcar != null) {
                        try {
                            gcar.nuevoTicket(t);
                            gcar.enviaTicket(t);
                        } catch (ExcepcionConsola ex) {
                            ex.printStackTrace();
                        }
                    }
                    if (gsas != null) {
                        try {
                            gsas.nuevoTicket(t);
                            gsas.enviaTicketRespuesta(t);
                        } catch (ExcepcionConsola ex) {
                            ex.printStackTrace();
                        }
                    }
                }
                if (carapp != null) {
                    carapp.muestraConsolaAReservas();
                } else {
                    casapp.actualizaConsolaASistemas();
                }
                this.dialogo.setVisible(false);

                //Actualizo los casos de este administrador en GestionaAdminReservas
                //Envío el ticket y el caso nuevo al Admin de Sistemas
                //Actualizo la lista de la Consola
            } else {
                this.dialogo.setVisible(false);
            }

        } else if (cmd.equals("Cancel")) {
            this.dialogo.setVisible(false);
        }

    }

}
