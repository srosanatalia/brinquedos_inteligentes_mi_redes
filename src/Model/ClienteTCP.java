/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.awt.HeadlessException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

/**
 *
 * @author nati_
 */
public class ClienteTCP {
    private String url;
    private int porta;

    public ClienteTCP(String url, int porta) {
        this.url = url;
        this.porta = porta;
    }
    
    public void leituraTag(){
        try {
            Socket cliente = new Socket(this.url, this.porta);
            System.out.println("Conexão iniciada");
          }
          catch(HeadlessException | IOException e) {
            System.out.println("Erro: " + e.getMessage());
          }
      }
}
