/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import Model.Piloto;
import java.awt.Dimension;
import java.sql.Array;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author nati_
 */
public class TelaCorrida extends javax.swing.JFrame {

    /**
     * Creates new form TelaCorrida
     */
    private final Dimension dimensaoPadrao;
    public TelaCorrida() {
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

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMaximumSize(new java.awt.Dimension(817, 547));
        setPreferredSize(new java.awt.Dimension(817, 547));

        painelResultado.setBackground(new java.awt.Color(255, 255, 255));
        painelResultado.setMaximumSize(new java.awt.Dimension(817, 547));
        painelResultado.setMinimumSize(new java.awt.Dimension(817, 547));
        painelResultado.setPreferredSize(new java.awt.Dimension(817, 547));

        tabelaResultado.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Carro", "Tempo de Corrida", "Melhor Tempo", "Tempo de Volta", "Voltas"
            }
        ));
        jScrollPane2.setViewportView(tabelaResultado);

        javax.swing.GroupLayout painelResultadoLayout = new javax.swing.GroupLayout(painelResultado);
        painelResultado.setLayout(painelResultadoLayout);
        painelResultadoLayout.setHorizontalGroup(
            painelResultadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, painelResultadoLayout.createSequentialGroup()
                .addContainerGap(122, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 616, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(119, 119, 119))
        );
        painelResultadoLayout.setVerticalGroup(
            painelResultadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, painelResultadoLayout.createSequentialGroup()
                .addContainerGap(109, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 355, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(83, 83, 83))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 857, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(painelResultado, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 857, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 547, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(painelResultado, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

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
            java.util.logging.Logger.getLogger(TelaCorrida.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TelaCorrida.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TelaCorrida.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TelaCorrida.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TelaCorrida().setVisible(true);
            }
        });
    }
    
    public void preencheResultado (ArrayList <String> resultado){
        DefaultTableModel tabela = (DefaultTableModel)tabelaResultado.getModel();
        tabela.addRow (new String [] {resultado.get(0), resultado.get(1), resultado.get(2), resultado.get(3), resultado.get(4)}); 
    }
    public void sucesso (){
        JOptionPane.showMessageDialog(rootPane, "Corrida Finalizada!");
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPanel painelResultado;
    private javax.swing.JTable tabelaResultado;
    // End of variables declaration//GEN-END:variables
}