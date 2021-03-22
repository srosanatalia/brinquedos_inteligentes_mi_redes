/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 *
 * @author nati_
 */
class Equipe {
    private String id; 
    private String nome;
    private String apelido;
    private String nacionalidade;
    private String ano;
    private BufferedImage logo;
    private ArrayList <Piloto> listaPilotos;
    private ArrayList <Carro> listaCarros;
    private int pontos;

    public Equipe(String id, String nome, String apelido, String nacionalidade, String ano, BufferedImage logo) {
        this.id = id;
        this.nome = nome;
        this.apelido = apelido;
        this.nacionalidade = nacionalidade;
        this.ano = ano;
        this.logo = logo;
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

    public String getApelido() {
        return apelido;
    }

    public void setApelido(String apelido) {
        this.apelido = apelido;
    }

    public String getNacionalidade() {
        return nacionalidade;
    }

    public void setNacionalidade(String nacionalidade) {
        this.nacionalidade = nacionalidade;
    }

    public String getAno() {
        return ano;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }

    public BufferedImage getLogo() {
        return logo;
    }

    public void setLogo(BufferedImage logo) {
        this.logo = logo;
    }

    public ArrayList<Piloto> getListaPilotos() {
        return listaPilotos;
    }

    public void setListaPilotos(ArrayList<Piloto> listaPilotos) {
        this.listaPilotos = listaPilotos;
    }

    public ArrayList<Carro> getListaCarros() {
        return listaCarros;
    }

    public void setLisatCarros(ArrayList<Carro> listaCarros) {
        this.listaCarros = listaCarros;
    }

    public int getPontos() {
        return pontos;
    }

    public void setPontos(int pontos) {
        this.pontos = pontos;
    }
    
    
}
