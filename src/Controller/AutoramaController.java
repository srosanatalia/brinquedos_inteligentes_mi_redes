/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.awt.image.BufferedImage;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import Model.*;
import Model.Mqtt.Subscriber;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.paho.client.mqttv3.MqttException;

/**
 *
 * @author nati_
 */
public class AutoramaController {
    private final ArrayList <Pais> paises;
    private final ArrayList <Piloto> pilotos;
    private final ArrayList <Equipe> equipes;
    private final ArrayList <Carro> carros;
    private final ArrayList <Pista> pistas;
    public String url;
    public int porta;
    public String serial;
    public String baudrate;
    public String region;
    public String protocol;
    public String antenna;
    public String frequency;
    public Cliente Cliente;
    public Subscriber subscriber;
    
    public AutoramaController() {
        this.pilotos = new ArrayList ();
        this.paises = new ArrayList ();
        this.equipes = new ArrayList ();
        this.carros = new ArrayList ();
        this.pistas = new ArrayList ();
        
        try {
            this.lerPaises();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(AutoramaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public Piloto cadastrarPiloto(Piloto piloto) {
        if(piloto.getCarro() != null){
            int i = this.carros.indexOf(piloto.getCarro());
            this.carros.get(i).setPiloto(piloto);
        }
        if(piloto.getEquipe() != null){
            int i = this.equipes.indexOf(piloto.getEquipe());
            ArrayList<Piloto> listaPilotosEquipe = this.equipes.get(i).getListaPilotos();
            if(!listaPilotosEquipe.isEmpty()){
                listaPilotosEquipe.add(piloto);
                this.equipes.get(i).setListaPilotos(listaPilotosEquipe);
            }
            listaPilotosEquipe.add(piloto);
            this.equipes.get(i).setListaPilotos(listaPilotosEquipe);
        }
        pilotos.add(piloto);
        return piloto;
    }
    
    public Carro cadastrarCarro(Carro carro) {
        carros.add(carro);
        return carro;
    }
    
    public Equipe cadastrarEquipe(Equipe equipe){
        if(!equipe.getListaPilotos().isEmpty()){
            Iterator itr = equipe.getListaPilotos().iterator();
            while(itr.hasNext()){
                Object o = itr.next();
                if(o  instanceof Piloto){
                    Piloto piloto = (Piloto) o;
                    int index = this.pilotos.indexOf(piloto);
                    this.pilotos.get(index).setEquipe(equipe);
                }
            }
        }
        if(!equipe.getListaCarros().isEmpty()){
            Iterator itr2 = equipe.getListaCarros().iterator();
            while(itr2.hasNext()){
                Object o = itr2.next();
                if(o  instanceof Carro){
                    Carro carro = (Carro) o;
                    int index = this.carros.indexOf(carro);
                     this.carros.get(index).setEquipe(equipe);
                }
            }
        }
        equipes.add(equipe);
        return equipe;
    }
    
    public Pista cadastrarPista (String id, String nome, String pais, String tempo){
        Pista pista = new Pista (id, nome, pais, tempo);
        pistas.add(pista);
        return pista;
    }
    
    public ArrayList getPilotos() {
        return pilotos;
    }
    
    public ArrayList getEquipes() {
        return equipes;
    }
    
    public ArrayList getTags() {
        return this.Cliente.tags;
    }
    
    public ArrayList getCarros() {
        return carros;
    }
    
    public ArrayList getPaises() {
        return paises;
    }
    
    public ArrayList getPistas() {
        return pistas;
    }
    
    public void setCarroPiloto(Piloto piloto, Carro carro){
        int i = this.pilotos.indexOf(piloto);
        Piloto pilotoModificado = this.pilotos.get(i);
        pilotoModificado.setCarro(carro);
        this.pilotos.add(i, pilotoModificado);
    }
    
    public void iniciarservidor(String url, int porta) throws Exception{
        try{
            this.porta = porta;
            this.url = url;
            new Thread(cliente).start();
        } catch (Exception e){System.out.println("Não foi possível estabelecer uma conexão.");}
        
    }
    
    //Tread que inicia o cliente 
    private Runnable cliente = new Runnable() {
        public void run() {
            try{
                Cliente = new Cliente(url, porta);
                Cliente.leituraTag();
            } catch (Exception e){}
       }
    };
    
    public void configurarQualificacao(String url) throws IOException, InterruptedException, MqttException{
          Cliente.configurarQualificacaoMqtt(url);
    }
    
    public void iniciarQualificacao(String url) throws IOException{
        Cliente.iniciarQualificacao(url, getCarros(), getPilotos());
    }
    
    public void iniciarQualificacaoMqtt() throws IOException, MqttException{
        Cliente.iniciarQualificacaoMqtt(getCarros(), getPilotos());
    }
    
    public void criarJsonRfid(String serial, String baudrate, String region, String protocol, String antenna, String frequency){
        this.serial = serial;
        this.baudrate = baudrate;
        this.region = region;
        this.protocol = protocol;
        this.antenna = antenna;
        this.frequency = frequency;
        
    }
    
    private void lerPaises() throws FileNotFoundException {
        try (Scanner scanner = new Scanner(new File(AutoramaController.class.getResource("../Assets/paises.csv").toURI()));) {
            scanner.nextLine();
            while (scanner.hasNextLine()) {
                try (Scanner rowScanner = new Scanner(scanner.nextLine())) {
                    rowScanner.useDelimiter(",");
                    String name = rowScanner.next();
                    String alpha2 = rowScanner.next();
                    Pais country = new Pais(name, alpha2);
                    paises.add(country);
                }
            }
        } catch (URISyntaxException e) {
        }
    }

    public Pais getPais(String nome) {
        Iterator itr = paises.iterator();
        while(itr.hasNext()){
            Object o = itr.next();
            if(o  instanceof Pais){
                Pais pais = (Pais)o;
                if(pais.getNome() == nome)
                    return pais;
            }
        }
        return null;
    }

    public void cadastrarEntidades() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
