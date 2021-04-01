/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.awt.image.BufferedImage;
import java.sql.Date;
import java.util.ArrayList;
import Model.*;

/**
 *
 * @author nati_
 */
public class AutoramaController {
    private final ArrayList pilotos;
    public String url;
    public int porta;
    
    public AutoramaController() {
        this.pilotos = new ArrayList ();
    }
    
    public boolean cadastrarPiloto(String id, String nome, String apelido, String nacionalidade, boolean emAtividade) {
        Piloto piloto = new Piloto(id, nome, apelido,  nacionalidade, emAtividade);
        pilotos.add(piloto);
        return pilotos.contains(piloto);
    }
    
    public ArrayList getPilotos() {
        return pilotos;
    }
    
    public void iniciarservidor(String url, int porta) throws Exception{
        this.porta = porta;
        this.url = url;
        new Thread(cliente).start();
    }

    private Runnable cliente = new Runnable() {
        public void run() {
            try{
                ClienteTCP Cliente = new ClienteTCP(url, porta);
                Cliente.leituraTag();
            } catch (Exception e){}
       }
    };
}
