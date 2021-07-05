/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Model.Mqtt.Publisher;
import Model.Mqtt.Subscriber;
import View.TelaCorrida;
import View.TelaQualificacao;
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
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;
import org.eclipse.paho.client.mqttv3.MqttException;

/**
 *
 * @author nati_
 */
public class Cliente {
    private String url;
    private int porta;
    public Socket cliente;
    public Subscriber subscriber;
    public Publisher publisher;
    public BufferedReader entrada;
    public String topico;
    public ArrayList tags;
    private ArrayList resultadoGeral;
    private ArrayList resultadoGeralQualificacao;
    private ArrayList carros;
    private ArrayList pilotos;
    private TelaCorrida frameCorrida = new TelaCorrida();

    public Cliente(String url, int porta) {
        this.url = url;
        this.porta = porta;
        this.tags = new ArrayList();
        this.carros = new ArrayList();
        this.pilotos = new ArrayList();
        this.resultadoGeral = new ArrayList();
        this.resultadoGeralQualificacao = new ArrayList();
    }
    
    //Método utilizado para leitura das tags
    public void leituraTag() throws ClassNotFoundException, MqttException, InterruptedException{
            this.subscriber = new Subscriber("tcp://"+this.url+":"+porta, "response/");
            if(subscriber.isConected()){
                System.out.println("O cliente se inscreveu no tópico: "+subscriber.topic);
            } else{
                System.out.println("Sem conexão.");
            }
            TimeUnit.SECONDS.sleep(5);
            this.subscriber.setTopic("response/rfid/config");
            this.subscriber.enviaMensagem("{\"serial\":\"tmr:///dev/ttyUSB0\", \"baudrate\":\"230400\", \"region\":\"NA2\", \"protocol\":\"GEN2\", \"antenna\":\"1\", \"frequency\":\"1800\"}", "autorama/rfid/config");
            TimeUnit.SECONDS.sleep(5);
            this.subscriber.setTopic("response/rfid/config");
            this.subscriber.enviaMensagem("{\"teste\":false}", "autorama/rfid/tags");
            TimeUnit.SECONDS.sleep(5);
            System.out.println("Iniciando thread de leitura");
            new Thread(leituraTagMqtt).start();
    }
    
    public void leituraTagTCP() throws ClassNotFoundException, MqttException, InterruptedException{
        try {
            this.cliente = new Socket(this.url, this.porta);
            this.entrada = new BufferedReader (new InputStreamReader(this.cliente.getInputStream(), "UTF-8"));
            if(cliente.isConnected()){
                System.out.println("Conexão iniciada");
                String tags=null;
                DataOutputStream dos = new DataOutputStream(this.cliente.getOutputStream());
                
                String rotaPOST = "POST /rfid/config\n{\"serial\":\"tmr:///dev/ttyUSB0\", \"baudrate\":\"230400\", \"region\":\"NA2\", \"protocol\":\"GEN2\", \"antenna\":\"1\", \"frequency\":\"1800\"}";
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
    
    //Recebe e envia a rota da configuração da qualificação
    public void configurarQualificacao(String url) throws IOException{
        DataOutputStream dos = new DataOutputStream(this.cliente.getOutputStream());
        
        String rotaQUAL = url;
        byte[] rota3 = rotaQUAL.getBytes();
        dos.write(rota3);
        dos.flush();
    }
    public void configurarQualificacaoMqtt(String url) throws IOException, InterruptedException, MqttException{
        TimeUnit.SECONDS.sleep(5);
        this.subscriber.setTopic("response/race/config");
        this.subscriber.enviaMensagem(url, "autorama/race/config");
    }
    
    //Inicia qualificação
    public void iniciarQualificacao(String url, ArrayList carros, ArrayList pilotos) throws IOException{
        this.carros = carros;
        this.pilotos = pilotos;
        
        DataOutputStream dos = new DataOutputStream(this.cliente.getOutputStream());
        
        String rotaQUALINI = url;
        byte[] rota3 = rotaQUALINI.getBytes();
        dos.write(rota3);
        dos.flush();
        frameCorrida.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frameCorrida.setVisible(true);
        frameCorrida.cronometroQualificacao();
    }
    
    public void iniciarQualificacaoMqtt(ArrayList carros, ArrayList pilotos) throws IOException, MqttException{
        this.carros = carros;
        this.pilotos = pilotos;
        
        this.subscriber.setTopic("response/qualification");
        this.subscriber.enviaMensagem("", "autorama/race/qualification/start");
        
        frameCorrida.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frameCorrida.setVisible(true);
        frameCorrida.cronometroQualificacao();
    }
    
    //Inicia corrida
    public void iniciarCorrida(String url) throws IOException, MqttException{
        this.carros = carros;
        
//        DataOutputStream dos = new DataOutputStream(this.cliente.getOutputStream());
//        
//        String rotaCORINI = url;
//        byte[] rota4 = rotaCORINI.getBytes();
//        dos.write(rota4);
//        dos.flush();
          
        this.subscriber.setTopic("response/race");
        this.subscriber.enviaMensagem("", "autorama/race/start");
    }

    public ArrayList getTags() {
        return tags;
    }
    
    public String getCarroNumero(String tag){
        Iterator itr = this.carros.iterator();
        Carro carro = null;
        while(itr.hasNext()){
            Object o = itr.next();
            if(o  instanceof Carro){
                carro = (Carro)o;
                if(carro.getTag().contains(tag)){
                    return carro.getNumeroCarro();
                }
            }
        }
        return "Desconhecido";
    }
    public String getCarroModelo(String tag){
        Iterator itr = this.carros.iterator();
        Carro carro = null;
        while(itr.hasNext()){
            Object o = itr.next();
            if(o  instanceof Carro){
                carro = (Carro)o;
                if(carro.getTag().contains(tag)){
                    return carro.getModelo();
                }
            }
        }
        return "Desconhecido";
    }
    public String getCarroPiloto(String tag){
        Iterator itr = this.carros.iterator();
        Carro carro = null;
        while(itr.hasNext()){
            Object o = itr.next();
            if(o  instanceof Carro){
                carro = (Carro)o;
                if(carro.getTag().contains(tag)){
                    for (int i = 0; i < this.pilotos.size(); i++) {
                        Piloto piloto = (Piloto) this.pilotos.get(i);
                        if(piloto.getCarro().equals(carro)){
                            return piloto.getNome();
                        }
                    }
                }
            }
        }
        return "Desconhecido";
    }
    
    private Runnable leituraTagMqtt = new Runnable() {
        public void run() {
            while(true){
                if(!subscriber.mensagem.isEmpty()){
                    System.out.println(subscriber.mensagem);
                }
                else{
                }
            }
        }
    };
    
    //Thread para receber e tratar os dados recebidos do servidor
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
                               if(!tag.contains("RACE_COMPLETED!") && tag.contains("epc") && tag.contains("laps") && !tag.contains("QUALIFICATION_COMPLETED!")){
                                    String dados = tag;
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
//                                    System.out.println("Corrida: "+dados);
                                    frameCorrida.limpaTabelaCorrida();
                                    tag = "";
                                    String[] textoSeparado =dados.split("'");
                                    for (int i = 0; i <textoSeparado.length; ++i){ 
                                        if(textoSeparado[i].contains("epc")){
                                            String tagCarro = textoSeparado[i+2];
                                            resultadoCorrida.add(getCarroNumero(tagCarro));
                                            resultadoCorrida.add(getCarroPiloto(tagCarro));
                                        }
                                        else if(textoSeparado[i].contains("race_time")){
                                            String tempoFormatado = "";
                                            String tempo = textoSeparado[i+2];
                                            if(tempo.length() > 9){
                                                String seg = tempo.substring(tempo.length() - 9, tempo.length());
                                                String min = tempo.substring(0, tempo.length() - 9);
                                                tempoFormatado = min + ":" + seg;
                                            }
                                            resultadoCorrida.add(tempoFormatado);
                                        }
                                        else if(textoSeparado[i].contains("best_time")){
                                            String tempoFormatado = "";
                                            String tempo = textoSeparado[i+2];
                                            if(tempo.length() > 9){
                                                String seg = tempo.substring(tempo.length() - 9, tempo.length());
                                                String min = tempo.substring(0, tempo.length() - 9);
                                                tempoFormatado = min + ":" + seg;
                                            }
                                            resultadoCorrida.add(tempoFormatado);
                                        }
                                        else if(textoSeparado[i].contains("time_lap")){
                                            String tempoFormatado = "";
                                            String tempo = textoSeparado[i+2];
                                            if(tempo.length() > 9){
                                                String seg = tempo.substring(tempo.length() - 9, tempo.length());
                                                String min = tempo.substring(0, tempo.length() - 9);
                                                tempoFormatado = min + ":" + seg;
                                            }
                                            resultadoCorrida.add(tempoFormatado);
                                        }
                                        else if(textoSeparado[i].contains("laps")){
                                            resultadoCorrida.add(textoSeparado[i+1]);
                                            resultadoGeral.add(resultadoCorrida);
                                            frameCorrida.preencheResultado(resultadoCorrida);
                                            resultadoCorrida.clear();
                                        }
                                    } 
                               } else if(!tag.contains("RACE_COMPLETED!") && tag.contains("epc") && !tag.contains("laps") && !tag.contains("QUALIFICATION_COMPLETED!") ){
                                   String dados = tag;
                                    ArrayList <String> resultadoQualificacao = new ArrayList();
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
//                                    System.out.println("Qualificacao: "+dados);
                                    frameCorrida.limpaTabelaQualificacao();
                                    tag = "";
                                    String[] textoSeparado =dados.split("'");
                                    for (int i = 0; i <textoSeparado.length; ++i){ 
                                        if(textoSeparado[i].contains("epc")){
                                            String tagCarro = textoSeparado[i+2];
                                            resultadoQualificacao.add(getCarroNumero(tagCarro));
                                            resultadoQualificacao.add(getCarroModelo(tagCarro));
                                            resultadoQualificacao.add(getCarroPiloto(tagCarro));
                                        }
                                        else if(textoSeparado[i].contains("best_time")){
                                            String tempoFormatado = "";
                                            String tempo = textoSeparado[i+2];
                                            if(tempo.length() > 9){
                                                String seg = tempo.substring(tempo.length() - 9, tempo.length());
                                                String min = tempo.substring(0, tempo.length() - 9);
                                                tempoFormatado = min + ":" + seg;
                                            }
                                            resultadoQualificacao.add(tempoFormatado);
                                        }
                                        else if(textoSeparado[i].contains("time")){
                                            String tempoFormatado = "";
                                            String tempo = textoSeparado[i+2];
                                            if(tempo.length() > 9){
                                                String seg = tempo.substring(tempo.length() - 9, tempo.length());
                                                String min = tempo.substring(0, tempo.length() - 9);
                                                tempoFormatado = min + ":" + seg;
                                            }
                                            resultadoQualificacao.add(tempoFormatado);
                                            resultadoGeralQualificacao.add(resultadoQualificacao);
                                            frameCorrida.preencheResultadoQualificacao(resultadoQualificacao);
                                            resultadoQualificacao.clear();
                                        }
                                    } 
                               } 
                               else if(tag.contains("RACE_COMPLETED!")){
                                   tag = "";
                                   frameCorrida.sucesso();
                                   System.out.println("Completou!");
                               }   
                               else if(tag.contains("QUALIFICATION_COMPLETED!")){
                                   tag = "";
                                   frameCorrida.sucessoQualificacao();
                                   System.out.println("Completou Qualificação!");
                                   frameCorrida.setaCorrida(true);
                                   frameCorrida.setaQualificacao(false);
                                   iniciarCorrida("POST /race/start\n");
                               }   
                           }
                        }
                    }
                } catch (IOException ex) {
                    Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
                } catch (MqttException ex) {
                    Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
       }
    };
}
