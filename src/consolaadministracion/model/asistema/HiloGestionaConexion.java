package consolaadministracion.model.asistema;

import consolaadministracion.controller.consolas.ControladorConsolaASistemas;
import consolaadministracion.model.GestionaAdminSistema;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

/**
 * @author Marco Borreguero
 */
public class HiloGestionaConexion implements Runnable {

    private GestionaAdminSistema gas;
    private ControladorConsolaASistemas ccas;

    /**
     *
     * Hilo que trata de gestionar las nuevas conexiones que se produzcan al
     * socket servidor de la aplicación
     *
     * @param gas modelo de la app del A. Sistema
     * @param ccas controlador de la vista principal del A. Sistema
     */
    public HiloGestionaConexion(GestionaAdminSistema gas, ControladorConsolaASistemas ccas) {

        this.gas = gas;
        this.ccas = ccas;

    }

    @Override
    public void run() { // Método tun del hilo

        try {
            while (gas.isEncendido()) { // Si la aplicación está encendida, corre
                Socket socket = new Socket();

                System.out.println("Esperando conexión...");

                socket = gas.getServidor().accept();// esperando cliente

                //Leer el nº de Administrador que se está conectando (/identificar cliente)
                ObjectInputStream fentrada = new ObjectInputStream(socket.getInputStream());

                Object o = fentrada.readObject();
                if (o.getClass().equals(Integer.class)) {
                    Integer idAdminR = (Integer) o;

                    System.out.println("Conectado el admin de reserva Nº: " + idAdminR);

                    ccas.lanzaHilo(idAdminR, socket, fentrada); // Se lanza el hilo que se encarga de atender a las peticiones del A. Reserva que se ha conectado
                }
            }
            gas.getServidor().close();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }

}
