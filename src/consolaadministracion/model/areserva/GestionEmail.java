package consolaadministracion.model.areserva;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Writer;
import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import org.apache.commons.net.pop3.POP3MessageInfo;
import org.apache.commons.net.pop3.POP3SClient;
import org.apache.commons.net.smtp.*;

/**
 *
 * Clase encargada de gestionar el envío y la recepción de emails para el A.
 * Reserva
 *
 * @author Marco Borreguero
 */
public class GestionEmail {

    /**
     *
     */
    public GestionEmail() {
    }

    /**
     *
     * Método encargado de leer un correo y transformalo a datos manejables por
     * java. En este caso, se representa un correo como un array de String
     *
     * @param reader
     * @param id
     * @return el email en un array de Strings --> [0] = destinatario, [1] =
     * fecha, [2] = asunto, [3] = mensajeº
     * @throws IOException
     */
    private String[] printMessageInfo(BufferedReader reader, int id) throws IOException {
        String from = "";
        String fecha = "";
        String subject = "";
        String mensaje = "";
        String line;
        String[] correo = new String[4];
        boolean esMensaje = false;
        while ((line = reader.readLine()) != null) { //Lee cada línea una a una (separa por los saltos de línea)
            String lower = line.toLowerCase(Locale.ENGLISH);
            if (lower.startsWith("from: ")) { //Busca las etiquetas "from", "subject" etc...
                from = line.substring(6).trim();
                correo[0] = from;
            } else if (lower.startsWith("date: ")) {
                fecha = line.substring(5).trim();
                correo[1] = fecha;
            } else if (lower.startsWith("subject: ")) {
                subject = line.substring(9).trim();
                correo[2] = subject;
                esMensaje = true;
            } else if (esMensaje) { //Para leer el mensaje, se ha acordado añadir unas etiquetas <INICIO> y <FINAL> en los emails de esta empresa. Para que puedan ser procesados
                if (lower.contains("<inicio>")) {
                    mensaje += line.substring(8) + "\n";
                } else if (lower.contains("<final>")) {
                    esMensaje = false;
                    correo[3] = mensaje;
                    break;
                } else {
                    mensaje += line + "\n";
                }
            }
        }
        return correo;
    }

    /**
     *
     * Método encargado de leer todos los emails existententes y etiquetados
     * como "nuevos" en la bandeja de entrada del correo introducido
     *
     * @return
     */
    public List<String[]> recibeCorreos() {
        String server = "pop.gmail.com", username = "proyectoPSPvalleinclan@gmail.com", password = "pspproyect2019"; //se debería introducir un correo y contraseña diferentes para cada persona
        List<String[]> correos = new ArrayList<>();
        POP3SClient pop3 = new POP3SClient(true); //Se crea el cliente POP3
        try {
            pop3.connect(server); //Se realiza la conexión con el servidor de GMAIL
            System.out.println("Conectado");
            if (!pop3.login(username, password)) { //Se realiza el login
                System.out.println("Error al hacer login");
            } else {
                POP3MessageInfo[] men = pop3.listMessages();
                if (men.length == 0) {
                    System.out.println("No se han podido recuperar mensajes");
                } else {
                    for (int i = 0; i < men.length; i++) {
                        POP3MessageInfo msginfo = men[i];
                        BufferedReader reader = (BufferedReader) pop3.retrieveMessage(msginfo.number);
                        String[] correo = printMessageInfo(reader, msginfo.number);
                        correos.add(correo);
                        reader.close();
                    }
                }
            }
            pop3.logout();
            pop3.disconnect();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return correos;
    }

    /**
     *
     * @param correo
     */
    public void enviaCorreo(String[] correo) {
        // se crea cliente SMTP seguro
        AuthenticatingSMTPClient client = new AuthenticatingSMTPClient();

        // datos del usuario y del servidor
        String server = "smtp.gmail.com";
        String username = "proyectoPSPvalleinclan@gmail.com";
        String password = "pspproyect2019";
        int puerto = 587;
        String remitente = "proyectoPSPvalleinclan@gmail.com";

        try {
            int respuesta;

            // Creaci�n de la clave para establecer un canal seguro
            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(null, null);
            KeyManager km = kmf.getKeyManagers()[0];

            // nos conectamos al servidor SMTP
            client.connect(server, puerto);
            // se establece la clave para la comunicaci�n segura
            client.setKeyManager(km);

            respuesta = client.getReplyCode();
            if (!SMTPReply.isPositiveCompletion(respuesta)) {
                client.disconnect();
                System.err.println("CONEXI�N RECHAZADA.");
            }

            // se env�a el commando EHLO
            client.ehlo(server);// necesario

            // NECESITA NEGOCIACI�N TLS - MODO NO IMPLICITO
            // Se ejecuta el comando STARTTLS y se comprueba si es true
            if (client.execTLS()) {

                // se realiza la autenticaci�n con el servidor
                if (client.auth(AuthenticatingSMTPClient.AUTH_METHOD.LOGIN, username, password)) {

                    String destino = correo[0];
                    String asunto = correo[2];
                    String mensaje = correo[3];
                    // se crea la cabecera
                    SimpleSMTPHeader cabecera = new SimpleSMTPHeader(remitente, destino, asunto);

                    // el nombre de usuario y el email de origen coinciden
                    client.setSender(remitente);
                    client.addRecipient(destino);

                    // se envia DATA
                    Writer writer = client.sendMessageData();
                    if (writer == null) { // fallo
                        System.out.println("FALLO AL ENVIAR DATA.");
                    }

                    writer.write(cabecera.toString()); // cabecera
                    writer.write(mensaje);// luego mensaje
                    writer.close();

                    boolean exito = client.completePendingCommand();

                    if (!exito) { // fallo
                        System.out.println("FALLO AL FINALIZAR TRANSACCI�N.");
                        System.exit(1);
                    } else {
                        System.out.println("MENSAJE ENVIADO CON EXITO......");
                    }

                } else {
                    System.out.println("USUARIO NO AUTENTICADO.");
                }
            } else {
                System.out.println("FALLO AL EJECUTAR  STARTTLS.");
            }

        } catch (IOException e) {
            System.err.println("Could not connect to server.");
            e.printStackTrace();
            System.exit(1);

        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(GestionEmail.class
                    .getName()).log(Level.SEVERE, null, ex);

        } catch (KeyStoreException ex) {
            Logger.getLogger(GestionEmail.class
                    .getName()).log(Level.SEVERE, null, ex);

        } catch (UnrecoverableKeyException ex) {
            Logger.getLogger(GestionEmail.class
                    .getName()).log(Level.SEVERE, null, ex);

        } catch (InvalidKeyException ex) {
            Logger.getLogger(GestionEmail.class
                    .getName()).log(Level.SEVERE, null, ex);

        } catch (InvalidKeySpecException ex) {
            Logger.getLogger(GestionEmail.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        try {
            client.disconnect();

        } catch (IOException ex) {
            Logger.getLogger(GestionEmail.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }
}
