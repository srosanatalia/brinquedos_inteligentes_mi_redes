/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 *
 * @author laercio
 */
public class Pais {
    private String nome;
    private String alpha2;

    public Pais(String nome, String alpha2) {
        this.nome = nome;
        this.alpha2 = alpha2;
    }

    public String getNome() {
        return nome;
    }

    public String getAlpha2() {
        return alpha2;
    }

    public String getBandeira() {
        return Pais.class.getResource("../Assets/flags_countries/" + alpha2 + ".png").toString();
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setAlpha2(String alpha2) {
        this.alpha2 = alpha2;
    }

    @Override
    public String toString() {
        return nome;
    }

}
