package consolaadministracion.vista;

import javax.swing.JButton;
import javax.swing.JList;

/**
 * @author Marco Borreguero
 */
public class ConsolaAReservas extends javax.swing.JFrame {

    /**
     * Creates new form ConsolaAReservas
     */
    public ConsolaAReservas() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        listaCasos = new javax.swing.JList<>();
        leerTicketButton = new javax.swing.JButton();
        nuevoCasoButton = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        listaTickets = new javax.swing.JList<>();
        nuevoTicketButton = new javax.swing.JButton();
        cerrarcasoButton = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        listaCorreos = new javax.swing.JList<>();
        leerCorreo = new javax.swing.JButton();
        nuevoCorreo = new javax.swing.JButton();
        actualizaLista = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        listaCasos.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Selección de Casos" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        listaCasos.setToolTipText("Selección de Casos");
        jScrollPane1.setViewportView(listaCasos);

        leerTicketButton.setText("Leer Ticket");
        leerTicketButton.setToolTipText("");

        nuevoCasoButton.setText("Nuevo Caso");

        listaTickets.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Selección de Tickets" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane2.setViewportView(listaTickets);

        nuevoTicketButton.setText("Nuevo Ticket");

        cerrarcasoButton.setText("Cerrar Caso");

        listaCorreos.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Selección de Correos" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        listaCorreos.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane3.setViewportView(listaCorreos);

        leerCorreo.setText("Leer Correo");

        nuevoCorreo.setText("Nuevo Correo");

        actualizaLista.setText("Sincronizar");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 335, Short.MAX_VALUE)
                    .addComponent(jScrollPane1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(nuevoTicketButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(leerTicketButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(nuevoCasoButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cerrarcasoButton, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 370, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(leerCorreo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(nuevoCorreo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(actualizaLista, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(13, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(leerCorreo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nuevoCorreo)
                        .addGap(55, 55, 55)
                        .addComponent(actualizaLista))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jScrollPane3)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(nuevoCasoButton)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(cerrarcasoButton)))
                            .addGap(21, 21, 21)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(leerTicketButton)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(nuevoTicketButton))))))
                .addContainerGap(17, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public JButton getCerrarcasoButton() {
        return cerrarcasoButton;
    }

    public JButton getLeerTicketButton() {
        return leerTicketButton;
    }

    public JList<String> getListaCasos() {
        return listaCasos;
    }

    public JList<String> getListaTickets() {
        return listaTickets;
    }

    public JButton getNuevoCasoButton() {
        return nuevoCasoButton;
    }

    public JButton getNuevoTicketButton() {
        return nuevoTicketButton;
    }

    public JButton getLeerCorreo() {
        return leerCorreo;
    }

    public JList<String> getListaCorreos() {
        return listaCorreos;
    }

    public JButton getNuevoCorreo() {
        return nuevoCorreo;
    }

    public JButton getActualizaLista() {
        return actualizaLista;
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton actualizaLista;
    private javax.swing.JButton cerrarcasoButton;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JButton leerCorreo;
    private javax.swing.JButton leerTicketButton;
    private javax.swing.JList<String> listaCasos;
    private javax.swing.JList<String> listaCorreos;
    private javax.swing.JList<String> listaTickets;
    private javax.swing.JButton nuevoCasoButton;
    private javax.swing.JButton nuevoCorreo;
    private javax.swing.JButton nuevoTicketButton;
    // End of variables declaration//GEN-END:variables
}
