package consolaadministracion.controller.consolas;

import consolaadministracion.controller.MainAdminReserva;
import consolaadministracion.model.GestionaAdminReservas;
import consolaadministracion.model.excepciones.ExcepcionConsola;
import consolaadministracion.model.types.Caso;
import consolaadministracion.model.types.Prioridad;
import consolaadministracion.model.types.Ticket;
import consolaadministracion.vista.ConsolaAReservas;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * @author Marco Borreguero Trigueros
 */
public class ControladorConsolaAReservas implements ActionListener, WindowListener {//, Runnable {

    private final GestionaAdminReservas modeloAR; //Modelo
    private final MainAdminReserva mainAR; //Super controlador
    private final ConsolaAReservas vistaAR; //Vista a controlar

    private List<Ticket> ticketsCaso;
    private List<Caso> casos;

    private List<String[]> correos;

    /**
     *
     * @param mainAR
     * @param modeloAR
     */
    public ControladorConsolaAReservas(MainAdminReserva mainAR, GestionaAdminReservas modeloAR) {
        this.modeloAR = modeloAR;
        this.mainAR = mainAR;
        this.vistaAR = new ConsolaAReservas();
        iniciarVista();
    }

    private void iniciarVista() {
        vistaAR.getCerrarcasoButton().addActionListener(this);
        vistaAR.getLeerTicketButton().addActionListener(this);
        vistaAR.getNuevoCasoButton().addActionListener(this);
        vistaAR.getNuevoTicketButton().addActionListener(this);
        vistaAR.getNuevoCorreo().addActionListener(this);
        vistaAR.getLeerCorreo().addActionListener(this);
        vistaAR.getActualizaLista().addActionListener(this);
        vistaAR.addWindowListener(this);
        vistaAR.setTitle("AdministradoR - " + modeloAR.getIdAdminR());
        cargarListaCasos();
        cargarListaCorreos();
    }

    /**
     *
     */
    public void abrir() {
        vistaAR.setVisible(true);
    }

    /**
     *
     */
    public void actualizar() {
        this.vistaAR.repaint();
    }

    public void cargarListaCorreos() {

        DefaultListModel<String> model = new DefaultListModel<>();

        this.correos = modeloAR.sincronizaCorreos();
        for (String[] c : correos) {
            model.addElement("Asunto: " + c[2] + " || De: " + c[0] + " || Fecha: " + c[1]);
        }
        vistaAR.getListaCorreos().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        vistaAR.getListaCorreos().setModel(model);
    }

    public void actualizarListaCorreos() {

        DefaultListModel<String> model = (DefaultListModel<String>) vistaAR.getListaCorreos().getModel();

        List<String[]> nuevosCorreos = modeloAR.sincronizaCorreos();
        for (String[] c : nuevosCorreos) {
            model.addElement("Asunto: " + c[2] + " || De: " + c[0] + " || Fecha: " + c[1]);
        }
        vistaAR.getListaCorreos().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        vistaAR.getListaCorreos().setModel(model);
        this.correos.addAll(nuevosCorreos);

    }

    /**
     *
     */
    public void cargarListaCasos() {

        DefaultListModel<String> model = new DefaultListModel<>();

        this.casos = modeloAR.getListaCasos();
        model.addElement("Caso_N || Nº de Tickets || Estado");
        for (Caso c : casos) {

            model.addElement(c.toString());
        }
        vistaAR.getListaCasos().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        vistaAR.getListaCasos().setModel(model);

        vistaAR.getListaCasos().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {

                if (!e.getValueIsAdjusting() && vistaAR.getListaCasos().getSelectedIndex() > 0) {

                    Caso c = casos.get(vistaAR.getListaCasos().getSelectedIndex() - 1);

                    cargarListaTickets(c.getTickets());
                }
            }
        });
        vistaAR.getListaCasos().setSelectedIndex(0);
    }

    private void cargarListaTickets(List<Ticket> tickets) {

        this.ticketsCaso = tickets;
        DefaultListModel<String> model = new DefaultListModel<>();

        model.addElement("Ticket_N ||      Fecha     || Prioridad ||   Estado  || Asunto");
        for (Ticket t : tickets) {

            model.addElement(t.toString());
        }
        vistaAR.getListaTickets().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        vistaAR.getListaTickets().setModel(model);
    }

    // ------------------------------------------------------------------------
    // ------------------------------------------------------------------------
    // INICIO CONTROL DE EVENTOS DE BOTONES Y VENTANA EN LA VISTA
    @Override
    public void actionPerformed(ActionEvent e) {

        String cmd = e.getActionCommand();

        if (cmd.equals("Nuevo Caso")) {
            mainAR.muestraDialogoCamposTicketNuevoCaso();
        } else if (cmd.equals("Cerrar Caso")) {
            int response = JOptionPane.showConfirmDialog(null, "¿Está seguro que desea cerrar el caso?", "Confirmación",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (response == JOptionPane.YES_OPTION) {
                Caso c = modeloAR.getListaCasos().get(vistaAR.getListaCasos().getSelectedIndex() - 1);
                try {
                    modeloAR.modificaEstadoTicketsCaso(c);
                    modeloAR.enviaTicket(c.getTickets().get(c.getTickets().size() - 1));
                } catch (ExcepcionConsola ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.INFORMATION_MESSAGE);
                }
                cargarListaCasos();
            }
        } else if (cmd.equals("Leer Ticket")) {
            if (!vistaAR.getListaTickets().isSelectionEmpty() && vistaAR.getListaTickets().getSelectedIndex() != 0) {
                mainAR.muestraDialogoCamposTicketLectura(this.ticketsCaso.get(vistaAR.getListaTickets().getSelectedIndex() - 1));
            } else {
                JOptionPane.showMessageDialog(null, "Seleccione un ticket a leer de la lista", "Sin selección de ticket", JOptionPane.INFORMATION_MESSAGE);
            }
        } else if (cmd.equals("Nuevo Ticket")) {
            if (!vistaAR.getListaCasos().isSelectionEmpty() && vistaAR.getListaCasos().getSelectedIndex() != 0) {
                Ticket t = new Ticket(modeloAR.getIdAdminR(), vistaAR.getListaCasos().getSelectedIndex(), vistaAR.getListaTickets().getModel().getSize(), "", Prioridad.MEDIA, "");
                mainAR.muestraDialogoCamposTicketContinuaCaso(t);
            } else {
                JOptionPane.showMessageDialog(null, "Seleccione un caso de la lista al que le desea añadir un nuevo ticket", "Sin selección de caso", JOptionPane.INFORMATION_MESSAGE);
            }
        } else if (cmd.equals("Nuevo Correo")) {
            mainAR.muestraDialogoCamposCorreoEnvio();
        } else if (cmd.equals("Leer Correo")) {
            if (!vistaAR.getListaCorreos().isSelectionEmpty()) {
                mainAR.muestraDialogoCamposCorreoLectura(this.correos.get(vistaAR.getListaCorreos().getSelectedIndex()));
            } else {
                JOptionPane.showMessageDialog(null, "Seleccione un correo a leer de la lista", "Sin selección de ticket", JOptionPane.INFORMATION_MESSAGE);
            }
        } else if (cmd.equals("Sincronizar")) {
            actualizarListaCorreos();
        }
    }

    @Override
    public void windowClosing(WindowEvent e) {
        try {
            modeloAR.cerrarConexion();
        } catch (ExcepcionConsola ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void windowClosed(WindowEvent e) {
    }

    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }

    @Override
    public void windowOpened(WindowEvent e) {
    }

    // FIN CONTROL DE EVENTOS
    // -----------------------------------------------------------------------
    // ------------------------------------------------------------------------
    //INICIO GETTERS
    /**
     *
     * @return
     */
    public List<Caso> getCasos() {
        return casos;
    }
    //FIN GETTERS

    /**
     *
     * @param msg
     */
    public void lanzaExcepcion(String msg) { //Método auxiliar para lanzar excepciones en forma de diálogo
        JOptionPane.showMessageDialog(null, msg, "Error", JOptionPane.INFORMATION_MESSAGE);
    }
}
