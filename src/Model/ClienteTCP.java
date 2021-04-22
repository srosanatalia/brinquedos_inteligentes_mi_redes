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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author nati_
 */
public class ClienteTCP {
    private String url;
    private int porta;
    public Socket cliente;
    public BufferedReader entrada;
    public ArrayList tags;

    public ClienteTCP(String url, int porta) {
        this.url = url;
        this.porta = porta;
        this.tags = new ArrayList();
    }
    
    public void leituraTag() throws ClassNotFoundException{
        try {
            this.cliente = new Socket(this.url, this.porta);
            this.entrada = new BufferedReader (new InputStreamReader(this.cliente.getInputStream(), "UTF-8"));
            if(cliente.isConnected()){
                System.out.println("Conexão iniciada");
                String tags=null;
                DataOutputStream dos = new DataOutputStream(this.cliente.getOutputStream());
                
                String rotaPOST = "POST /rfid/config\n{\"serial\":\"tmr:///dev/ttyUSB0\", \"baudrate\":\"230400\", \"region\":\"NA2\", \"protocol\":\"GEN2\", \"antenna\":\"1\", \"frequency\":\"1500\"}";
                byte[] rota = rotaPOST.getBytes();
                dos.write(rota);
                dos.flush();

                String rotaGET = "GET /rfid/tags\n";
                byte[] rota2 = rotaGET.getBytes();
                dos.write(rota2);
                dos.flush();
                
                new Thread(leituraTag).start();
            }
          }
          catch(HeadlessException | IOException e) {
            System.out.println("Erro: " + e.getMessage());
          }
    }

    public ArrayList getTags() {
        return tags;
    }
    
    private Runnable leituraTag = new Runnable() {
        public void run() {
            String tag = "";
            while(true){
                try {
                    if(entrada.ready()){
                        char c = (char)entrada.read();
                        tag += c;
                        if(c == 33){
                           if(tag.contains("tags")){
                                String[] textoSeparado = tag.split("'");
                                for (int i = 0; i < textoSeparado.length; i++) {
                                    if(textoSeparado[i].length() == 24){
                                        tags.add(textoSeparado[i]);
                                        System.out.println(textoSeparado[i]);
                                    }
                                }
                           }
                        }
                    }
                } catch (IOException ex) {
                    Logger.getLogger(ClienteTCP.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
       }
    };
}
