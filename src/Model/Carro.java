/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 *
 * @author nati_
 */
public class Carro {
    private String id;
    private String tag;
    private String modelo;
    private String marca;
    private String numeroCarro;
    private Piloto piloto;
    private Equipe equipe;

    public Carro(String id, String tag, String modelo, String marca, String numeroCarro) {
        this.id = id;
        this.tag = tag;
        this.modelo = modelo;
        this.marca = marca;
        this.numeroCarro = numeroCarro;
        this.piloto = null;
        this.equipe = null;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getNumeroCarro() {
        return numeroCarro;
    }

    public void setNumeroCarro(String numeroCarro) {
        this.numeroCarro = numeroCarro;
    }

    public Piloto getPiloto() {
        return piloto;
    }

    public void setPiloto(Piloto piloto) {
        this.piloto = piloto;
    }

    public Equipe getEquipe() {
        return equipe;
    }

    public void setEquipe(Equipe equipe) {
        this.equipe = equipe;
    }
    
    
    
}
