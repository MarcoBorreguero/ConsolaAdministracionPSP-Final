package consolaadministracion.model;

import consolaadministracion.model.areserva.GestionEmail;
import consolaadministracion.model.excepciones.ExcepcionConsola;
import consolaadministracion.model.types.Caso;
import consolaadministracion.model.types.Estado;
import consolaadministracion.model.types.Ticket;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Marco Borreguero
 */
public class GestionaAdminReservas {

    private final int PUERTO = 44444;

    private final int idAdminR;
    private final DAOJDBC dao; //Objeto DAO para las operaciones con la BD
    private List<Caso> casos;
    private int n_casos;

    private final Socket s;
    private ObjectOutputStream fsalida;

    private static GestionEmail gmail; //Objeto de la clase GestionaEmail que gestiona el envío y la recepción de emails

    /**
     *
     * @param idAdminR
     * @throws ExcepcionConsola
     */
    public GestionaAdminReservas(int idAdminR) throws ExcepcionConsola {

        this.idAdminR = idAdminR;
        this.dao = new DAOJDBC();
        this.n_casos = 0;
        this.casos = new ArrayList<>();
        inicializaCasos();

        this.s = inicializaSocket();
        try {
            this.fsalida = new ObjectOutputStream(s.getOutputStream());
            iniciaConexionConServidor();
        } catch (IOException ex) {
            throw new ExcepcionConsola(ex.getMessage());
        }

        this.gmail = new GestionEmail();
    }

    //INICIO MÉTODOS MANEJO EMAILS

    /**
     *
     * @param correo
     */
    public void enviaCorreo(String[] correo) {
        gmail.enviaCorreo(correo);
    }

    /**
     *
     * @return
     */
    public List<String[]> sincronizaCorreos() {
        return gmail.recibeCorreos();
    }

    //FINAL MÉTODOS MANEJO EMAILS
    // -------------------------------------------------------------------------
    // -------------------------------------------------------------------------
    // INICIO MÉTODOS MANEJO DATOS EN BD
    private List<Ticket> inicializaTickets() throws ExcepcionConsola { //Obtengo los tickets de la BD y construyo los datos

        List<Ticket> tickets = dao.cargaTicketsPorID(idAdminR);
        List<Ticket> res = new ArrayList<>();

        if (!tickets.isEmpty()) {

            Collections.sort(tickets); //Ordeno los tickets según el id de los casos Por ej:(1, 1, 1, 2, 2, 3, 4)

            for (Ticket t : tickets) {
                //System.out.println(t.toString());
                //if (t.getAdminId() == this.idAdminR) { // Cojo los tickets que pertenecen al Admin de reservas que ha entrado
                res.add(t);
                if (this.n_casos == 0) { // Cuento los casos que tiene este Admin de Reservas
                    this.n_casos = t.getCasoId();
                } else {
                    if (!(this.n_casos == t.getCasoId())) {
                        this.n_casos++;
                    }
                }
                //}
            }
        }
        return res;
    }

    private void inicializaCasos() throws ExcepcionConsola { //Inicializo los casos a partir de los Tickets de la BD

        List<Ticket> tickets = inicializaTickets();
        List<Caso> casos = new ArrayList<>();

        if (!tickets.isEmpty()) {

            int id_caso = 1;

            Collections.sort(tickets);

            while (id_caso <= this.n_casos) {

                Caso c = new Caso(id_caso, this.idAdminR, new ArrayList());
                List<Ticket> aux = new ArrayList<>();

                for (Ticket t : tickets) {

                    if (t.getCasoId() == id_caso) {
                        aux.add(t);
                    }
                }
                c.setTickets(aux); //Agrupo los tickets en el caso correspondiente
                c.setEstado(aux.get(aux.size() - 1).getEstado()); //Pongo el estado del caso el del último ticket
                casos.add(c); //Añado el caso a la lista de casos de este administrador
                id_caso++;
            }
        }
        this.casos.addAll(casos);
    }

    /**
     *
     * @param t
     * @throws ExcepcionConsola
     */
    public void nuevoTicket(Ticket t) throws ExcepcionConsola { //Creo un ticket en un caso que ya existe
        casos.get(t.getCasoId() - 1).getTickets().add(t);
    }

    /**
     *
     * @param c
     * @throws ExcepcionConsola
     */
    public void modificaEstadoTicketsCaso(Caso c) throws ExcepcionConsola { //Modifico el estado a Cerrado en la BD de los tickets relacionados a un caso concreto
        c.setEstado(Estado.CERRADO);
    }

    /**
     *
     * @param t
     * @throws ExcepcionConsola
     */
    public void nuevoCaso(Ticket t) throws ExcepcionConsola { //Creo un nuevo caso y le añado un ticket nuevo 

        List<Ticket> tickets = new ArrayList<>();
        tickets.add(t);
        this.n_casos++;

        Caso c = new Caso(this.n_casos, this.idAdminR, tickets);

        this.casos.add(c);
    }

    // FINAL MÉTODOS MANEJO DATOS EN BD
    // -------------------------------------------------------------------------
    // -------------------------------------------------------------------------
    // INICIO GESTIÓN DE LA CONEXIÓN CON EL SERVIDOR
    private Socket inicializaSocket() throws ExcepcionConsola { //Inicializo el socket que construirá la conexión
        try {
            return new Socket("localhost", PUERTO);
        } catch (IOException ex) {
            throw new ExcepcionConsola("Error de E/S - Mensaje: " + ex.getMessage());
        }
    }

    private void iniciaConexionConServidor() throws ExcepcionConsola { //Se confirma la conexión con el servidor mandándole la ID de de administrador (seguridad)

        try {
            fsalida.writeObject(idAdminR);
        } catch (IOException ex) {
            throw new ExcepcionConsola(ex.getMessage());
        }
    }

    /**
     *
     * @param t
     * @throws ExcepcionConsola
     */
    public void enviaTicket(Ticket t) throws ExcepcionConsola { //Método que escribe en el Stream de salida el ticket a enviar
        try {
            fsalida.writeObject(t);
        } catch (IOException ex) {
            throw new ExcepcionConsola(ex.getMessage());
        }
    }

    /**
     *
     * @throws ExcepcionConsola
     */
    public void cerrarConexion() throws ExcepcionConsola { //Cierra la conexión y le indica al servidor que se ha cerrado esta
        try {
            fsalida.writeObject("*");
            fsalida.close();
            s.close();
        } catch (IOException ex) {
            throw new ExcepcionConsola(ex.getMessage());
        }
    }
    // FINAL GESTIÓN DE LA CONEXIÓN CON EL SERVIDOR

    // -------------------------------------------------------------------------
    // -------------------------------------------------------------------------
    // INICIO GETTERS DATOS DEL MODELO
    /**
     *
     * @return
     */
    public int getIdAdminR() {
        return idAdminR;
    }

    /*Crea los casos que aparecen en los Tickets y calcula su estado y los añade a una lista*/
    /**
     *
     * @return
     */
    public List<Caso> getListaCasos() {
        return this.casos;
    }

    /**
     *
     * @return
     */
    public Socket getS() {
        return s;
    }

    /**
     *
     * @return
     */
    public int getN_casos() {
        return n_casos;
    }
    // FINAL GETTERS DATOS DEL MODELO
}
