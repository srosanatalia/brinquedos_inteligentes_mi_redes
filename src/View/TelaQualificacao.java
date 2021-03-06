/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import Controller.AutoramaController;
import java.awt.Dimension;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author nati_
 */
public class TelaQualificacao extends javax.swing.JFrame {

    /**
     * Creates new form TelaQualificacao
     */
    private final Dimension dimensaoPadrao;
    private AutoramaController autorama;
    public TelaQualificacao() {
        initComponents();
        this.dimensaoPadrao = new Dimension(817, 547);
    }


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        painelResultado = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tabelaResultado = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        iniciarCorrida = new javax.swing.JButton();
        painelCorrida = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tabelaResultado1 = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        painelResultado.setBackground(new java.awt.Color(255, 255, 255));
        painelResultado.setMaximumSize(new java.awt.Dimension(817, 547));
        painelResultado.setMinimumSize(new java.awt.Dimension(817, 547));

        tabelaResultado.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Carro", "Melhor Tempo", "Tempo de Volta"
            }
        ));
        jScrollPane2.setViewportView(tabelaResultado);

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        jLabel1.setText("QUALIFICA????O");

        iniciarCorrida.setText("Corrida");
        iniciarCorrida.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                iniciarCorridaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout painelResultadoLayout = new javax.swing.GroupLayout(painelResultado);
        painelResultado.setLayout(painelResultadoLayout);
        painelResultadoLayout.setHorizontalGroup(
            painelResultadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(painelResultadoLayout.createSequentialGroup()
                .addGap(277, 277, 277)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, painelResultadoLayout.createSequentialGroup()
                .addContainerGap(103, Short.MAX_VALUE)
                .addGroup(painelResultadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(iniciarCorrida, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 616, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(98, 98, 98))
        );
        painelResultadoLayout.setVerticalGroup(
            painelResultadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, painelResultadoLayout.createSequentialGroup()
                .addContainerGap(47, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 355, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(iniciarCorrida, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25))
        );

        painelCorrida.setBackground(new java.awt.Color(255, 255, 255));
        painelCorrida.setMaximumSize(new java.awt.Dimension(817, 547));
        painelCorrida.setMinimumSize(new java.awt.Dimension(817, 547));
        painelCorrida.setPreferredSize(new java.awt.Dimension(817, 547));

        tabelaResultado1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Carro", "Tempo de Corrida", "Melhor Tempo", "Tempo de Volta", "Voltas"
            }
        ));
        jScrollPane3.setViewportView(tabelaResultado1);

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        jLabel2.setText("CORRIDA");

        javax.swing.GroupLayout painelCorridaLayout = new javax.swing.GroupLayout(painelCorrida);
        painelCorrida.setLayout(painelCorridaLayout);
        painelCorridaLayout.setHorizontalGroup(
            painelCorridaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, painelCorridaLayout.createSequentialGroup()
                .addContainerGap(82, Short.MAX_VALUE)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 616, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(119, 119, 119))
            .addGroup(painelCorridaLayout.createSequentialGroup()
                .addGap(331, 331, 331)
                .addComponent(jLabel2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        painelCorridaLayout.setVerticalGroup(
            painelCorridaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, painelCorridaLayout.createSequentialGroup()
                .addContainerGap(47, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 355, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(83, 83, 83))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 817, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(painelResultado, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(painelCorrida, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 547, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(painelResultado, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(painelCorrida, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void iniciarCorridaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_iniciarCorridaActionPerformed
//        try {
            //            esconderTelas();
            //            painelResultado.setVisible(true);
            //            ArrayList <String> testeArray = new ArrayList();
            //            String teste = "[{'epc': b'E2000017221101321890548C', 'race_time': '01:02.159929', 'best_time': '00:19.766000', 'time_lap': '00:21.978000', 'laps': 3}]!";
            //            String[] textoSeparado = teste.split("'");
            //            System.out.println(testeArray.toString());
            //            for (int i = 0; i < textoSeparado.length; i++) {
            //                if(textoSeparado[i].length() == 24){
            //                    testeArray.add(textoSeparado[i]);
            //                    System.out.println(textoSeparado[i]);
            //                }
            //            }
            
            
//            autorama.iniciarCorrida("POST /race/start\n");
            
            //            DefaultTableModel tabela = (DefaultTableModel)tabelaPilotosQualificacao.getModel();
            //            Piloto piloto = buscaPilotoNome(selecionado);
            //            if(this.listaCarrosQualificacao.contains(piloto.getCarro())){
            //                JOptionPane.showMessageDialog(rootPane, "O carro associado a este piloto j?? est?? na pista.");
            //                selectPilotoQualificacao.removeItem((Object) selecionado);
            //                return;
            //            }
            //            tabela.addRow (new String [] {piloto.getNome(), piloto.getApelido(), piloto.getNacionalidade(), piloto.getEquipe().getNome(), piloto.getCarro().getMarca()});
            //            selectPilotoQualificacao.removeItem((Object) selecionado);
            //            listaCarrosQualificacao.add(piloto.getCarro());
//        } catch (IOException ex) {
//            Logger.getLogger(TelaQualificacao.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }//GEN-LAST:event_iniciarCorridaActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(TelaQualificacao.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TelaQualificacao.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TelaQualificacao.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TelaQualificacao.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TelaQualificacao().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton iniciarCorrida;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JPanel painelCorrida;
    private javax.swing.JPanel painelResultado;
    private javax.swing.JTable tabelaResultado;
    private javax.swing.JTable tabelaResultado1;
    // End of variables declaration//GEN-END:variables
}
