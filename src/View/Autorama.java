/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import Controller.AutoramaController;
import Model.ClienteTCP;

/**
 *
 * @author nati_
 */
public class Autorama {

    public static void main (String [] args){
        AutoramaController autoramaController = new AutoramaController();
        AutoramaInterface autorama = new AutoramaInterface(autoramaController);
//        ClienteTCP cliente = new ClienteTCP("augusto.ddns.net", 5051);
//        cliente.leituraTag();
        
    }
}
