package consolaadministracion.model.types;

import java.io.Serializable;
import java.util.List;

/**
 * @author Marco Borreguero
 */
public class Caso implements Serializable {

    private int id;
    private final int idAdminR;
    private List<Ticket> tickets;
    private Estado estado;

    /**
     *
     * Constructor para un nuevo caso. El estado por defecto es abierto
     *
     * @param id id del caso
     * @param idAdminR id del administrador de Reserva
     * @param tickets lista de tickets del caso
     */
    public Caso(int id, int idAdminR, List<Ticket> tickets) {
        this.id = id;
        this.idAdminR = idAdminR;
        this.tickets = tickets;
        this.estado = Estado.ABIERTO;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }

    public Estado getEstado() {
        return estado;
    }

    /**
     *
     * Pone todos los tickets del Caso al estado indicado a través de un método
     * auxiliar
     *
     * @param estado
     */
    public void setEstado(Estado estado) {
        this.estado = estado;
        calculaEstadoTickets(estado);

    }

    private void calculaEstadoTickets(Estado estado) {
        if (!tickets.isEmpty()) {
            for (Ticket t : tickets) {
                if (estado == Estado.ABIERTO || estado == Estado.PENDIENTE) {
                    t.setEstado(Estado.ABIERTO);
                } else {
                    t.setEstado(Estado.CERRADO);
                }
            }
        }
    }

    @Override
    public String toString() {
        return "Caso_" + id + " ||             " + tickets.size() + "            || " + estado;
    }

}
