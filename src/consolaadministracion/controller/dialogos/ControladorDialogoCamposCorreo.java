package consolaadministracion.controller.dialogos;

import consolaadministracion.controller.MainAdminReserva;
import consolaadministracion.model.GestionaAdminReservas;
import consolaadministracion.vista.DialogoCamposCorreo;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import javax.swing.JOptionPane;

/**
 * @author Marco Borreguero
 */
public class ControladorDialogoCamposCorreo implements ActionListener {

    //VISTA A CONTROLAR
    private final DialogoCamposCorreo dialogo;

    //MODELOS SEGÚN EL ADMIN
    private final GestionaAdminReservas gar;

    //CONTROLADORES PRINCIPALES SEGÚN EL ADMIN
    private final MainAdminReserva carapp;

    //CORREO A LEER O CREAR
    private final String[] correo; //[0] -> Destinatario, [1] -> Fecha, [2] -> Asunto, [3] -> Mensaje

    private final boolean editable;

    /**
     * @param gar
     * @param carapp
     * @param correo
     */
    public ControladorDialogoCamposCorreo(GestionaAdminReservas gar, MainAdminReserva carapp, String[] correo) {
        this.carapp = carapp;
        this.gar = gar;

        if (correo != null) {
            this.correo = correo;
            this.editable = false;
        } else {
            this.correo = new String[4];
            this.editable = true;
        }

        this.dialogo = new DialogoCamposCorreo();
        initConfig();
    }

    /**
     *
     */
    public void abrir() {
        this.dialogo.setVisible(true);
    }

    private void initConfig() {

        this.dialogo.setModal(true);
        this.dialogo.getOkButton().addActionListener(this);
        this.dialogo.getCancelButton().addActionListener(this);

        if (!editable) {
            this.dialogo.getLabelDireccion().setText("De:");

            this.dialogo.getTxtDireccion().setText(correo[0]);
            this.dialogo.getTxtFecha().setText(correo[1]);
            this.dialogo.getTxtAsunto().setText(correo[2]);
            this.dialogo.getTxtMensaje().setText(correo[3]);

            this.dialogo.getTxtDireccion().setEditable(false);
            this.dialogo.getTxtFecha().setEditable(false);
            this.dialogo.getTxtAsunto().setEditable(false);
            this.dialogo.getTxtMensaje().setEditable(false);

        } else if (editable) {
            this.dialogo.getLabelDireccion().setText("Para:");

            this.dialogo.getTxtDireccion().setText("");
            this.dialogo.getTxtFecha().setText(LocalDate.now().toString());
            this.dialogo.getTxtAsunto().setText("");
            this.dialogo.getTxtMensaje().setText("");

            this.dialogo.getTxtFecha().setEditable(false);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if (cmd.equals("OK")) {

            if (editable) {

                if (this.dialogo.getTxtDireccion().getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "El email no ha podido enviarse: No se ha añadido destinatario", "Error en el envío", JOptionPane.INFORMATION_MESSAGE);
                } else if (this.dialogo.getTxtAsunto().getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "El email no ha podido enviarse: No se ha añadido asunto", "Error en el envío", JOptionPane.INFORMATION_MESSAGE);
                } else {

                    //Creo un nuevo correo
                    correo[0] = (this.dialogo.getTxtDireccion().getText());
                    correo[1] = (this.dialogo.getTxtFecha().getText());
                    correo[2] = (this.dialogo.getTxtAsunto().getText());
                    correo[3] = "<INICIO>" + (this.dialogo.getTxtMensaje().getText()) + "\n<FINAL>";

                    if (gar != null) {
                        if (correo[0] == "" || correo[0] == null) {
                            JOptionPane.showMessageDialog(null, "El email no ha podido enviarse: No se ha añadido destinatario", "Error en el envío", JOptionPane.INFORMATION_MESSAGE);
                        } else if (correo[2] == "" || correo[2] == null) {
                            JOptionPane.showMessageDialog(null, "El email no ha podido enviarse: No se ha añadido asunto", "Error en el envío", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            gar.enviaCorreo(correo); //Envío el correo
                        }
                        this.dialogo.setVisible(false);
                    }
                }
            } else {
                this.dialogo.setVisible(false);
            }

        } else if (cmd.equals("Cancel")) {
            this.dialogo.setVisible(false);
        }
    }
}
