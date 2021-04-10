/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.awt.image.BufferedImage;
import java.sql.Date;

/**
 *
 * @author nati_
 */
public class Piloto {
    private String id;
    private String nome;
    private String apelido;
    private BufferedImage foto;
    private String dataNascimento;
    private String nacionalidade;
    private Carro carro;
    private Equipe equipe;
    private boolean emAtividade;
    private int pontos;

    public Piloto(String id, String nome, String apelido, String nacionalidade, boolean emAtividade) {
        this.id = id;
        this.nome = nome;
        this.apelido = apelido;
        this.nacionalidade = nacionalidade;
        this.emAtividade = emAtividade;
        this.pontos = 0;
        this.carro = null;
        this.equipe = null;
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

    public BufferedImage getFoto() {
        return foto;
    }

    public void setFoto(BufferedImage foto) {
        this.foto = foto;
    }

    public String getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getNacionalidade() {
        return nacionalidade;
    }

    public void setNacionalidade(String nacionalidade) {
        this.nacionalidade = nacionalidade;
    }

    public Carro getCarro() {
        return carro;
    }

    public void setCarro(Carro carro) {
        this.carro = carro;
    }

    public Equipe getEquipe() {
        return equipe;
    }

    public void setEquipe(Equipe equipe) {
        this.equipe = equipe;
    }

    public boolean isEmAtividade() {
        return emAtividade;
    }

    public void setEmAtividade(boolean emAtividade) {
        this.emAtividade = emAtividade;
    }

    public int getPontos() {
        return pontos;
    }

    public void setPontos(int pontos) {
        this.pontos = pontos;
    }
    
    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }
    
}
