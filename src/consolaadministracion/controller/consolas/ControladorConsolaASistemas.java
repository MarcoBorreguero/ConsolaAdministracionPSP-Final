package consolaadministracion.controller.consolas;

import consolaadministracion.controller.MainAdminSistema;
import consolaadministracion.model.GestionaAdminSistema;
import consolaadministracion.model.excepciones.ExcepcionConsola;
import consolaadministracion.model.types.Caso;
import consolaadministracion.model.types.Estado;
import consolaadministracion.model.types.Prioridad;
import consolaadministracion.model.types.Ticket;
import consolaadministracion.vista.ConsolaASistemas;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * @author Marco Borreguero
 */
public class ControladorConsolaASistemas implements ActionListener, WindowListener, Runnable { //Es el controlador de la vista y además es el hilo encargado de recibir los tickets y mostrarlos por pantalla

    private final GestionaAdminSistema modeloAS;
    private final MainAdminSistema mainAS;
    private final ConsolaASistemas vistaAS;

    private Map<Integer, List<Caso>> adminsYCasos;
    private Set<Integer> idAdminsR;
    private List<Caso> casosAdmin;
    private List<Ticket> ticketsCaso;

    private ObjectInputStream fentrada;
    private Socket socket;

    private Map<Integer, Integer> indiceGestorCasosPendientes;
    private boolean disponible;

    /**
     *
     * @param modeloAS modelo de la aplicación, donde se gestiona la lógica de
     * negocio
     * @param mainAS controlador principal o super controlador
     */
    public ControladorConsolaASistemas(GestionaAdminSistema modeloAS, MainAdminSistema mainAS) {
        this.modeloAS = modeloAS;
        this.adminsYCasos = new HashMap<>();
        this.mainAS = mainAS;
        this.vistaAS = new ConsolaASistemas();
        this.indiceGestorCasosPendientes = new HashMap<>();
        this.disponible = true;
        iniciarVista();
    }

    //INICIO PRODUCTOR-CONSUMIDOR
    /**
     *
     * @throws InterruptedException
     */
    public void produce() throws InterruptedException {
        Integer id;

        if (indiceGestorCasosPendientes.size() == 0) {
            id = 1;
        } else {
            id = indiceGestorCasosPendientes.size();
        }
        while (true) {
            synchronized (this) {
                // El hilo productor espera a que estén disponibles los recursos
                while (id == adminsYCasos.size() || !disponible) {
                    wait();
                }
                int value = 0;
                while (id < adminsYCasos.size() && disponible) {
                    for (Caso c : adminsYCasos.get(id)) {
                        if (c.getEstado().equals(Estado.PENDIENTE)) {
                            value++;
                        }
                    }
                    indiceGestorCasosPendientes.put(id, value);
                    id++;
                }
                this.disponible = false;
                System.out.println("Productor: " + value);

                // Una vez calculados los casos pendientes por administrador, 
                //se notifica al consumidor de que ya puede consumir los datos
                notifyAll();

                // Se duerme el hilo mientras se ejecuta el consumidor
                Thread.sleep(1000);
            }
        }
    }

    /**
     *
     * @throws InterruptedException
     */
    public void consume() throws InterruptedException {
        while (true) {
            synchronized (this) {
                // el hilo espera a que termine el productor
                while (disponible) {
                    wait();
                }

                //se leen los datos que se han producido
                int val = 0;
                for (int id = 1; id < indiceGestorCasosPendientes.size(); id++) {
                    if (this.indiceGestorCasosPendientes.containsKey(id)) {
                        val = this.indiceGestorCasosPendientes.get(id);
                        System.out.println("Consumidor: " + val);
                        DefaultListModel<String> model = (DefaultListModel<String>) vistaAS.getListaNCasosPendientes().getModel();
                        model.addElement("Admin_" + id + ":" + val);
                    }
                }
                this.disponible = true;
                //Se llama al productor para que siga produciendo
                notifyAll();

                // and sleep 
                Thread.sleep(1000);
            }
        }
    }

    //FINAL PRODUCTOR-CONSUMIDOR
    //INICIO MÉTODOS CONTROL DE VISTA
    private void iniciarVista() { //Inicia los elementos de la vista de forma personalizada por defecto
        vistaAS.getDesconectar().addActionListener(this);
        vistaAS.getLeerTicketButton().addActionListener(this);
        vistaAS.getNuevoTicketButton().addActionListener(this);
        vistaAS.getLabelAviso().setVisible(false);
        DefaultListModel<String> model = new DefaultListModel<>();
        vistaAS.getListaNCasosPendientes().setModel(model);
        cargarListaAdminsR();
    }

    /**
     * Abre la ventana de la vista
     */
    public void abrir() { //Abre la ventana de la vista
        this.vistaAS.setVisible(true);
    }

    /**
     *
     */
    public void actualizar() { // Actualiza la vista cuando llega un nuevo ticket o caso
        cargarListaAdminsR();
        this.vistaAS.repaint();
        this.vistaAS.getLabelAviso().setVisible(false);
    }

    private void avisar(String msg) { //Notifica de la llegada o cambio en un caso o ticket
        this.vistaAS.getLabelAviso().setText(msg);
        this.vistaAS.getLabelAviso().setVisible(true);
    }

    /**
     *
     * Se encarga de cargar la lista de los Administradores existentes en la BD
     * para que se puedan ver los tickets y casos que tienen
     *
     */
    public void cargarListaAdminsR() {

        this.adminsYCasos = modeloAS.getAdminsRCasos();
        this.idAdminsR = adminsYCasos.keySet();

        DefaultListModel<String> model = new DefaultListModel<>();

        model.addElement("|| ID Admin de Reservas ||");
        for (Integer i : idAdminsR) {
            model.addElement("Administrador_" + i);
        }
        vistaAS.getListaAdminsR().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        vistaAS.getListaAdminsR().setModel(model);

        vistaAS.getListaAdminsR().addListSelectionListener(new ListSelectionListener() { //Añade un listener a la lista para que al ser pulsado un elemento 
            //carge los casos de dicho Administrador 
            @Override
            public void valueChanged(ListSelectionEvent e) {

                if (!e.getValueIsAdjusting() && vistaAS.getListaAdminsR().getSelectedIndex() > 0) {

                    vistaAS.getLabelAviso().setVisible(false);
                    cargarListaCasos(adminsYCasos.get(vistaAS.getListaAdminsR().getSelectedIndex())); //Carga los casos dela dministrador seleccionado

                }
            }
        });

    }

    /**
     *
     * @param casosAdminX
     */
    public void cargarListaCasos(List<Caso> casosAdminX) { //Carga ña lista de casos según el administrador seleccionado
        this.casosAdmin = casosAdminX;
        DefaultListModel<String> model = new DefaultListModel<>();

        model.addElement("Caso_N || Nº de Tickets || Estado");
        for (Caso c : casosAdminX) {
            model.addElement(c.toString());
        }

        vistaAS.getListaCasos().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        vistaAS.getListaCasos().setModel(model);

        vistaAS.getListaCasos().addListSelectionListener(new ListSelectionListener() { //Le añado un listener de selección a la lista para que se carguen los datos en las otras listas simultáneamente
            @Override
            public void valueChanged(ListSelectionEvent e) {

                if (!e.getValueIsAdjusting() && vistaAS.getListaCasos().getSelectedIndex() > 0) {

                    Caso c = casosAdmin.get(vistaAS.getListaCasos().getSelectedIndex() - 1);
                    if (c.getEstado().equals(Estado.PENDIENTE)) {
                        vistaAS.getNuevoTicketButton().setEnabled(true);
                        cargarListaTickets(c.getTickets());
                    } else {
                        vistaAS.getNuevoTicketButton().setEnabled(false);
                        cargarListaTickets(c.getTickets());
                    }
                }
            }
        });

        vistaAS.getListaCasos().setSelectedIndex(0);
    }

    private void cargarListaTickets(List<Ticket> tickets) { //Carga la lista de tickets según el caso seleccionado

        this.ticketsCaso = tickets;
        DefaultListModel<String> model = new DefaultListModel<>();

        model.addElement("Ticket_N ||      Fecha     || Prioridad ||   Estado  || Asunto");
        for (Ticket t : tickets) {
            model.addElement(t.toString());
        }

        vistaAS.getListaTickets().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        vistaAS.getListaTickets().setModel(model);
    }

    @Override
    public void actionPerformed(ActionEvent e) { //Método que escucha los eventos de acción de la vista

        String cmd = e.getActionCommand(); //Nombre del elemento del que procede el evento

        if (cmd.equals("Desconectar")) { //Evento del botón Desconectar
            modeloAS.apagarGestionaConexiones(); //Cierra el socket y la conexión
            System.exit(0); //Cierra la aplicación
        } else if (cmd.equals("Leer Ticket")) {//Evento del botón Desconectar
            if (!vistaAS.getListaTickets().isSelectionEmpty() && vistaAS.getListaTickets().getSelectedIndex() != 0) {
                mainAS.muestraDialogoCamposTicketLectura(this.ticketsCaso.get(vistaAS.getListaTickets().getSelectedIndex() - 1));
            } else {
                JOptionPane.showMessageDialog(null, "Seleccione un ticker a leer de la lista", "Sin selección de ticket", JOptionPane.INFORMATION_MESSAGE);
            }

        } else if (cmd.equals("Nuevo Ticket")) {//Evento del botón Nuevo Ticket
            if (!vistaAS.getListaCasos().isSelectionEmpty() && vistaAS.getListaCasos().getSelectedIndex() != 0) {
                if (casosAdmin.get(vistaAS.getListaCasos().getSelectedIndex() - 1).getEstado().equals(Estado.PENDIENTE)) {//Si he seleccionado un elemento que no sea el primero, genero un ticket vacío a rellenar
                    Ticket t = new Ticket(vistaAS.getListaAdminsR().getSelectedIndex(), vistaAS.getListaCasos().getSelectedIndex(), vistaAS.getListaTickets().getModel().getSize(), "", Prioridad.RESPUESTA, ""); //Ticket Vacío por defecto
                    mainAS.muestraDialogoCamposTicketRespondeCaso(t); //A través del controlador principal de la APP, abro el diálogo para leer y modificar datos de un ticket con el ticket generado
                } else {
                    JOptionPane.showMessageDialog(null, "Solo puede enviar un ticket a un caso PENDIENTE", "No permitido", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Seleccione un caso de la lista al que le desea añadir un nuevo ticket", "Sin selección de caso", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    @Override
    public void windowClosing(WindowEvent e) { //Cuando se cierra la ventana, se apaga la conexión con el servidor de forma segura
        modeloAS.apagarGestionaConexiones();
    }

    @Override
    public void windowOpened(WindowEvent e) {
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

    //FIN MÉTODOS CONTROL DE VISTA
    //-------------------------------------------------------------------------
    //-------------------------------------------------------------------------
    //INICIO MÉTODOS HILO DE ESPERA DE TICKETS
    /**
     *
     * @param idAdminR
     * @param s
     * @param fentrada
     */
    public void lanzaHilo(Integer idAdminR, Socket s, ObjectInputStream fentrada) { //Instancio los datos necesarios que va a manejar el hilo que estará a la espera de nuevos tickets

        // Crea un flujo de entrada para leer los casos
        this.socket = s;
        this.fentrada = fentrada;
        this.modeloAS.anadirSocket(idAdminR, socket);
        if (!modeloAS.getAdminsRCasos().containsKey(idAdminR)) {
            modeloAS.getAdminsRCasos().put(idAdminR, new ArrayList<Caso>());
        }
        actualizar();

        new Thread(this).start();
    }

    @Override
    public void run() { //Método RUN del hilo que está esperando la llegada de nuevos tickets o casos

        ObjectInputStream e = this.fentrada;
        Socket s = this.socket;

        while (true) {
            String cadena = "";
            Ticket t = null;
            try {
                Object o = e.readObject();
                if (o.getClass().equals(String.class)) {
                    cadena = (String) o;
                    //Se ha recibido una cadena de control                    
                    if (cadena.trim().equals("*")) {// EL CLIENTE SE DESCONECTA
                        System.out.println("Se ha desconectado un cliente");
                        e.close();
                        s.close();
                        break;
                    }
                }
                //Se ha recibido un ticket
                if (o.getClass().equals(Ticket.class)) {
                    t = (Ticket) o;
                    if (!adminsYCasos.isEmpty() && adminsYCasos != null && adminsYCasos.containsKey(t.getAdminId())) {
                        List<Caso> casos = modeloAS.getAdminsRCasos().get(t.getAdminId()); //Obtengo los casos a partir de la ID del administrador que se ha conectado
                        boolean nuevoCaso = true;
                        if (casos != null) {
                            for (Caso c : casos) {
                                if (c.getId() == t.getCasoId()) {//Se ha recibido un ticket para un caso existente
                                    for (Ticket tc : c.getTickets()) {
                                        if (tc.getCasoId().equals(t.getCasoId()) && tc.getId().equals(t.getId())) {
                                            c.setEstado(Estado.CERRADO);
                                            cargarListaAdminsR();
                                            actualizar();
                                            avisar("Se ha cerrado el Caso_" + t.getCasoId() + " del Admin_" + t.getAdminId());
                                            modeloAS.modificaEstadoTicketsCaso(c);
                                            nuevoCaso = false;
                                            break;
                                        } else {
                                            c.setEstado(Estado.PENDIENTE);
                                            c.getTickets().add(t); //añado el ticket al caso
                                            cargarListaAdminsR();
                                            actualizar();
                                            avisar("Nuevo Ticket del Admin_" + t.getAdminId() + " en el Caso_" + t.getCasoId());
                                            modeloAS.guardarTicket(t);
                                            nuevoCaso = false;
                                            break;
                                        }
                                    }
                                    break;
                                }
                            }
                            if (nuevoCaso) { //Se ha recibido un ticket para un caso nuevo
                                List<Ticket> ts = new ArrayList<Ticket>();
                                ts.add(t);
                                Caso nuevoC = new Caso(casos.size() + 1, t.getAdminId(), ts);
                                nuevoC.setEstado(Estado.PENDIENTE);
                                modeloAS.getAdminsRCasos().get(t.getAdminId()).add(nuevoC); // creo el nuevo caso y lo añado a los ya existentes
                                cargarListaAdminsR();
                                actualizar();
                                avisar("Nuevo Caso del Admin_" + t.getAdminId());
                                modeloAS.guardarTicket(t);
                            }
                        }
                    }
                }
            } catch (IOException ex) {
                lanzaExcepcion(ex.getMessage());
            } catch (ClassNotFoundException ex) {
                lanzaExcepcion(ex.getMessage());
            } catch (NullPointerException ex) {
                lanzaExcepcion(ex.getMessage());
            } catch (ExcepcionConsola ex) {
                lanzaExcepcion(ex.getMessage());
            }
        } //FIN WHILE
        try {
            socket.close();
        } catch (IOException ex) {
            //lanzaExcepcion(ex.getMessage());
            ex.printStackTrace();
        }
    }
    //FIN MÉTODOS HILO DE ESPERA DE TICKETS

    /**
     *
     * @param msg
     */
    public void lanzaExcepcion(String msg) { //Método auxiliar para lanzar excepciones en forma de diálogo
        JOptionPane.showMessageDialog(null, msg, "Error", JOptionPane.INFORMATION_MESSAGE);
    }

}
