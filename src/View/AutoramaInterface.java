/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import Controller.AutoramaController;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;

/**
 *
 * @author nati_
 */
public class AutoramaInterface {
    private JFrame frame = new JFrame ("Autorama");
    private final AutoramaController autoramaController;
    AutoramaInterface(AutoramaController autoramaController) {
        this.autoramaController = autoramaController;
        frame = new TelaConfiguracao(autoramaController);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
