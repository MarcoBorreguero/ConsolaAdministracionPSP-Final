package consolaadministracion.model;

import consolaadministracion.model.excepciones.ExcepcionConsola;
import consolaadministracion.model.types.Caso;
import consolaadministracion.model.types.Estado;
import consolaadministracion.model.types.Prioridad;
import consolaadministracion.model.types.Ticket;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Marco Borreguero
 */
public class GestionaAdminSistema {

    private final DAOJDBC dao; //DAO para el manejo de datos con la BD
    private final Map<Integer, List<Caso>> adminsRCasos; //Por cada lista de casos, la id del Admin de reserva

    private final int PUERTO = 44444; //Puerto de conexión
    private boolean encendido = true; //Variable de control del estado del servidor
    private final ServerSocket servidor; //Socket del servidor, del que se crearán los sockets de atención a los clientes
    private Map<Integer, Socket> indiceSocketsAdmins; //Índice con las IDs de los Administradores y para cada ID, el socket que atiende sus peticiones

    /**
     *
     * @throws ExcepcionConsola
     */
    public GestionaAdminSistema() throws ExcepcionConsola {
        try {
            this.dao = new DAOJDBC();
            this.adminsRCasos = new HashMap<>();
            inicializaCasosPorAdmin();

            this.servidor = new ServerSocket(PUERTO);
            this.indiceSocketsAdmins = new HashMap();

        } catch (IOException ex) {
            throw new ExcepcionConsola("Error de E/S - Mensaje: " + ex.getMessage());
        }

    }

    /**
     *
     * @param t
     * @throws ExcepcionConsola
     */
    public void enviaTicketRespuesta(Ticket t) throws ExcepcionConsola {
        Socket s = indiceSocketsAdmins.get(t.getAdminId());
        if (s != null && !s.isClosed()) {
            ObjectOutputStream fsalida = null;
            try {
                fsalida = new ObjectOutputStream(s.getOutputStream());
                fsalida.writeObject(t);
            } catch (IOException ex) {
                throw new ExcepcionConsola(ex.getMessage());
            }
        }
    }

    /**
     *
     */
    public void apagarGestionaConexiones() {
        this.encendido = false;
    }

    // INICIO MÉTODOS MANEJO DATOS EN BD
    /**
     *
     * @param t
     * @throws ExcepcionConsola
     */
    public void nuevoTicket(Ticket t) throws ExcepcionConsola { //Guardo un ticket nuevo generado, o recibido
        guardarTicket(t);
        List<Caso> casos = adminsRCasos.get(t.getAdminId());
        for (Caso c : casos) {
            if (c.getId() == t.getCasoId()) {
                c.getTickets().add(t);
                c.setEstado(Estado.ABIERTO);
                break;
            }
        }
    }

    /**
     *
     * @param t
     * @throws ExcepcionConsola
     *
     */
    public void guardarTicket(Ticket t) throws ExcepcionConsola { //Guarda un ticket recibido
        dao.guardaTicket(t);
    }

    /**
     *
     * @param c
     * @throws ExcepcionConsola
     */
    public void modificaEstadoTicketsCaso(Caso c) throws ExcepcionConsola { //Modifico el estado a Cerrado en la BD de los tickets relacionados a un caso concreto
        dao.modificaTicket(c.getTickets());
    }

    /**
     *
     * @return
     */
    private Map<Integer, List<Ticket>> inicializaTicketsPorAdmin() throws ExcepcionConsola {

        List<Ticket> tickets = dao.cargaTickets();
        Map<Integer, List<Ticket>> adminYTickets = new HashMap<>();

        if (!tickets.isEmpty()) {

            Collections.sort(tickets); //Ordeno los tickets según el id de los casos Por ej:(1, 1, 1, 2, 2, 3, 4)
            boolean fin = false;
            int idARaux = 0;

            while (!fin) {//Fin cuando no haya ningún ticket con id de admin X
                idARaux++;
                List<Ticket> ltaux = new ArrayList<>();
                for (Ticket t : tickets) {
                    if (t.getAdminId() == idARaux) {
                        ltaux.add(t);
                    }
                }
                if (ltaux.isEmpty()) {
                    fin = true;
                } else {
                    adminYTickets.put(idARaux, ltaux);
                }
            }
        }

        return adminYTickets;
    }

    /**
     *
     *
     */
    private void inicializaCasosPorAdmin() throws ExcepcionConsola {

        Map<Integer, List<Ticket>> adminTickets = inicializaTicketsPorAdmin();
        Map<Integer, List<Caso>> adminYCasos = new HashMap<>();

        if (!adminTickets.isEmpty()) {
            int idARaux = 0;

            for (List<Ticket> lt : adminTickets.values()) {
                idARaux++;
                List<Caso> casos = new ArrayList<>();
                int n_casos = 0;

                if (!lt.isEmpty()) {

                    int id_caso = 1;

                    Collections.sort(lt);

                    while (id_caso <= lt.get(lt.size() - 1).getCasoId()) {

                        Caso c = new Caso(id_caso, idARaux, null);
                        List<Ticket> aux = new ArrayList<>();
                        boolean pendiente = true;

                        for (Ticket t : lt) {
                            if (t.getCasoId() == id_caso) {
                                if (t.getEstado().equals(Estado.CERRADO)) {
                                    pendiente = false;
                                }
                                aux.add(t);
                            }
                        }
                        c.setTickets(aux);
                        if (!aux.get(aux.size() - 1).getPrioridad().equals(Prioridad.RESPUESTA) && pendiente) {
                            c.setEstado(Estado.PENDIENTE);
                        } else if (!pendiente) {
                            c.setEstado(Estado.CERRADO);
                        }

                        casos.add(c);
                        id_caso++;

                    }
                }
                adminYCasos.put(idARaux, casos);
            }
            this.adminsRCasos.putAll(adminYCasos);
        }

    }

    // FIN MÉTODOS MANEJO DATOS EN BD
    // -------------------------------------------------------------------------
    // -------------------------------------------------------------------------
    // INICIO GETTERS DATOS DEL MODELO
    /**
     *
     * @return
     */
    public Map<Integer, List<Caso>> getAdminsRCasos() { //Devuelvo la instancia de datos del modelo. Son los datos que manejará la aplicación. Primero se cargan de la BD y luego se van actualizando simultáneamente a ésta.
        return adminsRCasos;
    }

    /**
     *
     * @param idAdmin
     * @param s
     */
    public void anadirSocket(Integer idAdmin, Socket s) { //Añade un nuevo socket en el índice del id de administrador
        this.indiceSocketsAdmins.put(idAdmin, s);
    }

    /**
     *
     * @param idAdmin
     * @return
     */
    public Socket getElementoIndiceSockets(Integer idAdmin) { //Coge el socket que corresponde al índice del id de administrador introducido
        return this.indiceSocketsAdmins.get(idAdmin);
    }

    /**
     *
     * @return
     */
    public ServerSocket getServidor() { //Devuelve el Socket Servidor para crear nuevos sockets para atender a cada cliente
        return servidor;
    }

    /**
     *
     * @return
     */
    public int getPUERTO() { //Devuelve el puerto de conexión
        return PUERTO;
    }

    /**
     *
     * @return
     */
    public boolean isEncendido() { //Acceso a la variable de control del estado del servidor
        return encendido;
    }

    // FIN GETTERS DATOS DEL MODELO
}
