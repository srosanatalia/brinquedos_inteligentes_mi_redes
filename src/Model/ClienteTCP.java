/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.awt.HeadlessException;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
    
    public void leituraTag() throws ClassNotFoundException{
        try {
            Socket cliente = new Socket(this.url, this.porta);
            System.out.println("Conexão iniciada");
            DataOutputStream dos = new DataOutputStream(cliente.getOutputStream());
            String rotaPOST = "POST /rfid/config\n{\"serial\":\"tmr:///dev/ttyUSB0\", \"baudrate\":\"230400\", \"region\":\"NA2\", \"protocol\":\"GEN2\", \"antenna\":\"1\", \"frequency\":\"1500\"}";
            byte[] rota = rotaPOST.getBytes();
            dos.write(rota);
            dos.flush();
            
            BufferedReader entrada = new BufferedReader (new InputStreamReader(cliente.getInputStream()));
            
//            DataInputStream mensagemRecebida = new DataInputStream(cliente.getInputStream());
//            int length = mensagemRecebida.read();
//            System.out.println(length);// read length of incoming message
//            if(length>0) {
//                byte[] message = new byte[length];
//                mensagemRecebida.readFully(message, 0, message.length); // read the message
//            }
            
            System.out.println(entrada.readLine());
            
            String rotaGET = "GET /rfid/tags\\n";
            byte[] rota2 = rotaPOST.getBytes();
            dos.write(rota2);
            dos.flush();
            
            DataInputStream mensagemRecebida2 = new DataInputStream(cliente.getInputStream());
            
          }
          catch(HeadlessException | IOException e) {
            System.out.println("Erro: " + e.getMessage());
          }
      }
}
