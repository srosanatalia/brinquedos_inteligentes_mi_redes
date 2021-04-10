/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.awt.image.BufferedImage;
import java.sql.Time;

/**
 *
 * @author nati_
 */
public class Pista {
    private String id; 
    private String nome;
    private String pais;
    private BufferedImage imagem;
    private Time recordePista;
    private Piloto donoRecorde;

    public Pista(String id, String nome, String pais) {
        this.id = id;
        this.nome = nome;
        this.pais = pais;
        this.donoRecorde = null;
        this.recordePista = null;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public BufferedImage getImagem() {
        return imagem;
    }

    public void setImagem(BufferedImage imagem) {
        this.imagem = imagem;
    }

    public Time getRecordePista() {
        return recordePista;
    }

    public void setRecordePista(Time recordePista) {
        this.recordePista = recordePista;
    }

    public Piloto getDonoRecorde() {
        return donoRecorde;
    }

    public void setDonoRecorde(Piloto donoRecorde) {
        this.donoRecorde = donoRecorde;
    }
    
}
