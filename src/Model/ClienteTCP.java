/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import View.TelaCorrida;
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
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;

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
    private ArrayList resultadoGeral;
    private ArrayList carros;
    private TelaCorrida frame = new TelaCorrida();

    public ClienteTCP(String url, int porta) {
        this.url = url;
        this.porta = porta;
        this.tags = new ArrayList();
        this.carros = new ArrayList();
        this.resultadoGeral = new ArrayList();
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
    
    public void configurarQualificacao(String url) throws IOException{
        DataOutputStream dos = new DataOutputStream(this.cliente.getOutputStream());
        
        String rotaQUAL = url;
        byte[] rota3 = rotaQUAL.getBytes();
        dos.write(rota3);
        dos.flush();
    }
    
    public void iniciarQualificacao(String url, ArrayList carros) throws IOException{
        this.carros = carros;
        System.out.println("tá vazio?"+this.carros.isEmpty());
        DataOutputStream dos = new DataOutputStream(this.cliente.getOutputStream());
        
        String rotaQUALINI = url;
        byte[] rota3 = rotaQUALINI.getBytes();
        dos.write(rota3);
        dos.flush();
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public ArrayList getTags() {
        return tags;
    }
    
    public String getCarro(String tag){
        Iterator itr = this.carros.iterator();
        Carro carro = null;
        while(itr.hasNext()){
            Object o = itr.next();
            if(o  instanceof Carro){
                carro = (Carro)o;
                if(carro.getTag() == tag){
                    return carro.getModelo();
                }
            }
        }
        return "Desconhecido";
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
                                tag = "";
                           }
                           else{
                               if(!tag.contains("RACE_COMPLETED!")){
                                    String dados = tag;
                                    System.out.println("dados " + dados);
                                    ArrayList <String> resultadoCorrida = new ArrayList();
                                    dados = dados.replaceAll("OK","");
                                    dados = dados.replaceAll(" ","");
                                    dados = dados.replaceAll("\\[","");
                                    dados = dados.replaceAll("\\{","");
                                    dados = dados.replaceAll("\\n","");
                                    dados = dados.replaceAll(",","");
                                    dados = dados.replaceAll("\\}","");
                                    dados = dados.replaceAll("\\]","");
                                    dados = dados.replaceAll("!","");
                                    dados = dados.replaceAll(":","");
                                    System.out.println(dados);
                                    tag = "";
                                    String[] textoSeparado =dados.split("'");
                                    for (int i = 0; i <textoSeparado.length; ++i){ 
                                        if(textoSeparado[i].contains("epc")){
                                            System.out.println(textoSeparado[i+2]);
                                            resultadoCorrida.add(getCarro(textoSeparado[i+2]));
                                        }
                                        else if(textoSeparado[i].contains("race_time")){
                                            resultadoCorrida.add(textoSeparado[i+2]);
                                        }
                                        else if(textoSeparado[i].contains("best_time")){
                                            resultadoCorrida.add(textoSeparado[i+2]);
                                        }
                                        else if(textoSeparado[i].contains("time_lap")){
                                            resultadoCorrida.add(textoSeparado[i+2]);
                                        }
                                        else if(textoSeparado[i].contains("laps")){
                                            resultadoCorrida.add(textoSeparado[i+1]);
                                            resultadoGeral.add(resultadoCorrida);
                                            frame.preencheResultado(resultadoCorrida);
                                        }
                                    } 
                               } else{
                                   frame.sucesso();
                                   System.out.println("Completou!");
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
