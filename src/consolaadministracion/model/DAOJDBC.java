package consolaadministracion.model;

import consolaadministracion.model.excepciones.ExcepcionConsola;
import consolaadministracion.model.types.Estado;
import consolaadministracion.model.types.Prioridad;
import consolaadministracion.model.types.Ticket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Objeto DAOJDBC que maneja las conexiones con la BD y los métodos para la
 * comunicación y la gestión de las peticiones con la misma.
 *
 * @author Marco Borreguero
 */
public class DAOJDBC { //Carga los tickets de cada Admin de Reserva

    private Connection conex = null;

    /**
     *
     * @throws ExcepcionConsola
     */
    public DAOJDBC() throws ExcepcionConsola {
    }

    /**
     *
     * Método encargado de cargar los tickets de la BD
     *
     * @return @throws ExcepcionConsola
     */
    public List<Ticket> cargaTickets() throws ExcepcionConsola {

        abrirConexion();

        List<Ticket> tickets = new ArrayList<>();

        try {

            Statement comando;
            comando = conex.createStatement();
            ResultSet r = comando.executeQuery("SELECT pk, idAdminR, idCaso, idTicket, fecha, asunto, prioridad, descripcion, estado  FROM tickets");

            while (r.next() == true) {

                tickets.add(new Ticket(r.getInt("pk"), r.getInt("idAdminR"), r.getInt("idCaso"), r.getInt("idTicket"), r.getString("fecha"), r.getString("asunto"), Prioridad.valueOf(r.getString("prioridad")), r.getString("descripcion"), Estado.valueOf(r.getString("estado"))));

            }
            cerrarConexion();

        } catch (SQLException ex) {
            throw new ExcepcionConsola("ERROR SQL --> Código: " + ex.getErrorCode() + " Mensaje: " + ex.getMessage());
        }
        return tickets;
    }

    /**
     *
     * Método encargado de cargar todos los tickets según una ID introducida
     *
     * @param idAdminReserva
     * @return
     * @throws ExcepcionConsola
     */
    public List<Ticket> cargaTicketsPorID(Integer idAdminReserva) throws ExcepcionConsola {

        abrirConexion();

        List<Ticket> tickets = new ArrayList<>();

        try {

            Statement comando;
            comando = conex.createStatement();
            ResultSet r = comando.executeQuery("SELECT pk, idAdminR, idCaso, idTicket, fecha, asunto, prioridad, descripcion, estado  FROM tickets WHERE idAdminR=" + idAdminReserva + ";");

            while (r.next() == true) {

                tickets.add(new Ticket(r.getInt("pk"), r.getInt("idAdminR"), r.getInt("idCaso"), r.getInt("idTicket"), r.getString("fecha"), r.getString("asunto"), Prioridad.valueOf(r.getString("prioridad")), r.getString("descripcion"), Estado.valueOf(r.getString("estado"))));

            }
            cerrarConexion();

        } catch (SQLException ex) {
            throw new ExcepcionConsola("ERROR SQL --> Código: " + ex.getErrorCode() + " Mensaje: " + ex.getMessage());
        }

        return tickets;

    }

    /**
     *
     * Guarda un ticket en la BD
     *
     * @param t
     * @throws ExcepcionConsola
     */
    public void guardaTicket(Ticket t) throws ExcepcionConsola {
        try {
            abrirConexion();
            Statement comando = conex.createStatement();
            comando.executeUpdate("INSERT INTO tickets(idAdminR,idCaso,idTicket,fecha,asunto,prioridad,descripcion,estado) VALUES (" + t.getAdminId() + "," + t.getCasoId() + ", " + t.getId() + ",'" + t.getFecha() + "','" + t.getAsunto() + "' ,'" + t.getPrioridad().toString() + "' , '" + t.getDescripcion() + "', '" + t.getEstado().toString() + "');");
            cerrarConexion();
        } catch (SQLException ex) {
            throw new ExcepcionConsola("ERROR SQL --> Código: " + ex.getErrorCode() + " Mensaje: " + ex.getMessage());
        }
    }

    /**
     *
     * Modifica los datos de un ticket ya existente
     *
     * @param tickets
     * @throws ExcepcionConsola
     */
    public void modificaTicket(List<Ticket> tickets) throws ExcepcionConsola {
        try {
            abrirConexion();
            Statement comando = conex.createStatement();
            if (!tickets.isEmpty()) {
                for (Ticket t : tickets) {
                    comando.executeUpdate("UPDATE tickets SET estado='" + t.getEstado().toString() + "' WHERE idAdminR=" + t.getAdminId() + " AND idCaso=" + t.getCasoId() + " AND idTicket=" + t.getId() + ";");
                }
            }
            cerrarConexion();
        } catch (SQLException ex) {
            throw new ExcepcionConsola("ERROR SQL --> Código: " + ex.getErrorCode() + " Mensaje: " + ex.getMessage());
        }
    }

    private void abrirConexion() throws ExcepcionConsola { //Mñetodo encargado de abrir la conexión con la BD

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conex = DriverManager.getConnection("jdbc:mysql://localhost/bd_consola", "root", ""); //Se establece la URI de la conexión y el controlador
        } catch (SQLException e) {
            throw new ExcepcionConsola("" + e.getErrorCode());
        } catch (ClassNotFoundException e) {
            System.out.println(e);
            throw new ExcepcionConsola("Class not found error: " + e.getMessage() + "");

        } catch (Exception e) {
            System.out.println(e);
            throw new ExcepcionConsola("Unexpected error: " + e.getMessage() + "");

        }

    }

    private void cerrarConexion() { //Se cierra la conexión con la BD
        conex = null;
    }

}
