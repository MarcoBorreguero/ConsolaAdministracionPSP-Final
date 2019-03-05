package consolaadministracion.vista;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;

/**
 *
 * @author Usuario
 */
public class ConsolaASistemas extends javax.swing.JFrame {

    /**
     * Creates new form ConsolaASistemas
     */
    public ConsolaASistemas() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        listaCasos = new javax.swing.JList<>();
        leerTicketButton = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        listaTickets = new javax.swing.JList<>();
        nuevoTicketButton = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        listaAdminsR = new javax.swing.JList<>();
        desconectar = new javax.swing.JButton();
        labelAviso = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        listaNCasosPendientes = new javax.swing.JList<>();

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

        listaTickets.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Selección de Tickets" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane2.setViewportView(listaTickets);

        nuevoTicketButton.setText("Nuevo Ticket");

        listaAdminsR.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Selección de Admin de Reservas" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane3.setViewportView(listaAdminsR);

        desconectar.setText("Desconectar");

        labelAviso.setText("¡Nuevo!");

        listaNCasosPendientes.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane4.setViewportView(listaNCasosPendientes);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(labelAviso))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 455, Short.MAX_VALUE)
                            .addComponent(jScrollPane1))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(nuevoTicketButton)
                            .addComponent(leerTicketButton, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(desconectar))
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 112, Short.MAX_VALUE)))
                .addGap(19, 19, 19))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane3)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(desconectar))
                        .addGap(21, 21, 21)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(leerTicketButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(nuevoTicketButton))))
                    .addComponent(jScrollPane4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelAviso)
                .addContainerGap(12, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public JButton getDesconectar() {
        return desconectar;
    }

    public JButton getLeerTicketButton() {
        return leerTicketButton;
    }

    public JList<String> getListaAdminsR() {
        return listaAdminsR;
    }

    public JList<String> getListaCasos() {
        return listaCasos;
    }

    public JList<String> getListaTickets() {
        return listaTickets;
    }

    public JButton getNuevoTicketButton() {
        return nuevoTicketButton;
    }

    public JLabel getLabelAviso() {
        return labelAviso;
    }

    public JList<String> getListaNCasosPendientes() {
        return listaNCasosPendientes;
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton desconectar;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JLabel labelAviso;
    private javax.swing.JButton leerTicketButton;
    private javax.swing.JList<String> listaAdminsR;
    private javax.swing.JList<String> listaCasos;
    private javax.swing.JList<String> listaNCasosPendientes;
    private javax.swing.JList<String> listaTickets;
    private javax.swing.JButton nuevoTicketButton;
    // End of variables declaration//GEN-END:variables
}
