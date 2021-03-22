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
    public void leituraTag(String url, int porta){
        try {
            Socket cliente = new Socket(url,porta);
            try (ObjectInputStream entrada = new ObjectInputStream(cliente.getInputStream())) {
               //LEITURA TAG
            }
            System.out.println("Conexão encerrada");
          }
          catch(HeadlessException | IOException e) {
            System.out.println("Erro: " + e.getMessage());
          }
      }
}
