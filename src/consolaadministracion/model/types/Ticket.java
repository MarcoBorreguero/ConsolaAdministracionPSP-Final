package consolaadministracion.model.types;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * @author Marco Borreguero
 */
public class Ticket implements Comparable<Ticket>, Serializable {

    private int pkID;
    private Integer adminId;
    private Integer casoId;
    private Integer idTicket;
    private String fecha;
    private String asunto;
    private Prioridad prioridad;
    private String descripcion;
    private Estado estado;

    /*Constructor para la BD*/

    /**
     *
     * @param pkID
     * @param adminId
     * @param casoId
     * @param idTicket
     * @param fecha
     * @param asunto
     * @param prioridad
     * @param descripcion
     * @param estado
     */

    public Ticket(int pkID, Integer adminId, Integer casoId, Integer idTicket, String fecha, String asunto, Prioridad prioridad, String descripcion, Estado estado) {
        this.pkID = pkID;
        this.adminId = adminId;
        this.casoId = casoId;
        this.idTicket = idTicket;
        this.fecha = fecha;
        this.asunto = asunto;
        this.prioridad = prioridad;
        this.descripcion = descripcion;
        this.estado = estado;
    }


    /*Constructor OPCIONAL*/

    /**
     *
     * @param adminId
     * @param casoId
     * @param idTicket
     * @param asunto
     * @param prioridad
     * @param descripcion
     * @param estado
     */

    public Ticket(Integer adminId, Integer casoId, Integer idTicket, String asunto, Prioridad prioridad, String descripcion, Estado estado) {
        this.idTicket = idTicket;
        this.casoId = casoId;
        this.adminId = adminId;

        String dia = String.format("%02d", LocalDate.now().getDayOfMonth());
        String mes = String.format("%02d", LocalDate.now().getMonthValue());
        String anyo = String.format("%02d", LocalDate.now().getYear());

        this.fecha = "" + dia + "/" + mes + "/" + anyo;
        this.asunto = asunto;
        this.prioridad = prioridad;
        this.descripcion = descripcion;
        this.estado = estado;
    }

    /*Constructor para el boton "Nuevo"*/

    /**
     *
     * @param adminId
     * @param casoId
     * @param idTicket
     * @param asunto
     * @param prioridad
     * @param descripcion
     */

    public Ticket(Integer adminId, Integer casoId, Integer idTicket, String asunto, Prioridad prioridad, String descripcion) {
        this.idTicket = idTicket;
        this.casoId = casoId;
        this.adminId = adminId;

        String dia = String.format("%02d", LocalDate.now().getDayOfMonth());
        String mes = String.format("%02d", LocalDate.now().getMonthValue());
        String anyo = String.format("%02d", LocalDate.now().getYear());

        this.fecha = "" + dia + "/" + mes + "/" + anyo;
        this.asunto = asunto;
        this.prioridad = prioridad;
        this.descripcion = descripcion;
        this.estado = Estado.ABIERTO;
    }

    public int getPkID() {
        return pkID;
    }

    public Integer getId() {
        return idTicket;
    }

    public void setId(Integer idTicket) {
        this.idTicket = idTicket;
    }

    public Integer getCasoId() {
        return casoId;
    }

    public void setCasoId(Integer casoId) {
        this.casoId = casoId;
    }

    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asusnto) {
        this.asunto = asusnto;
    }

    public Prioridad getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(Prioridad prioridad) {
        this.prioridad = prioridad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Ticket_" + idTicket + " || " + fecha + " ||   " + prioridad + "     || " + estado + " || " + asunto;
    }

    @Override
    public int compareTo(Ticket o) {

        return this.casoId.compareTo(o.getCasoId());
    }

}
