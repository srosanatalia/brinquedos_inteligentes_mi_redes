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
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author nati_
 */
public class AutoramaController {
    private final ArrayList <Pais> paises;
    private final ArrayList <Piloto> pilotos;
    private final ArrayList <Equipe> equipes;
    private final ArrayList <Carro> carros;
    public String url;
    public int porta;
    public String serial;
    public String baudrate;
    public String region;
    public String protocol;
    public String antenna;
    public String frequency;
    
    public AutoramaController() {
        this.pilotos = new ArrayList ();
        this.paises = new ArrayList ();
        this.equipes = new ArrayList ();
        this.carros = new ArrayList ();
        
        try {
            this.lerPaises();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(AutoramaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public Piloto cadastrarPiloto(String id, String nome, String apelido, String nacionalidade, boolean emAtividade) {
        Piloto piloto = new Piloto(id, nome, apelido,  nacionalidade, emAtividade);
        pilotos.add(piloto);
        return piloto;
    }
    
    public Carro cadastrarCarro(String id, String tag, String modelo, String marca, String numeroCarro) {
        Carro carro = new Carro(id, tag, modelo,  marca, numeroCarro);
        carros.add(carro);
        return carro;
    }
    
    public ArrayList getPilotos() {
        return pilotos;
    }
    
    public ArrayList getEquipes() {
        return equipes;
    }
    
    public ArrayList getCarros() {
        return carros;
    }
    
    public ArrayList getPaises() {
        return paises;
    }
    
    public void iniciarservidor(String url, int porta) throws Exception{
        try{
            this.porta = porta;
            this.url = url;
            new Thread(cliente).start();
        } catch (Exception e){System.out.println("Não foi possível estabelecer uma conexão.");}
        
    }

    private Runnable cliente = new Runnable() {
        public void run() {
            try{
                ClienteTCP Cliente = new ClienteTCP(url, porta);
                Cliente.leituraTag();
            } catch (Exception e){}
       }
    };
    
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
}
