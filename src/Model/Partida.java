/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.sql.Time;
import java.util.ArrayList;

/**
 *
 * @author nati_
 */
public class Partida {
    private Time duracaoQualificação;
    private int numeroVoltasCorrida;
    private Pista pistaAtual;
    private ArrayList <Piloto> listaPilotos;

    public Partida(Time duracaoQualificação, int numeroVoltasCorrida, Pista pistaAtual, ArrayList<Piloto> listaPilotos) {
        this.duracaoQualificação = duracaoQualificação;
        this.numeroVoltasCorrida = numeroVoltasCorrida;
        this.pistaAtual = pistaAtual;
        this.listaPilotos = listaPilotos;
    }

    public Time getDuracaoQualificação() {
        return duracaoQualificação;
    }

    public void setDuracaoQualificação(Time duracaoQualificação) {
        this.duracaoQualificação = duracaoQualificação;
    }

    public int getNumeroVoltasCorrida() {
        return numeroVoltasCorrida;
    }

    public void setNumeroVoltasCorrida(int numeroVoltasCorrida) {
        this.numeroVoltasCorrida = numeroVoltasCorrida;
    }

    public Pista getPistaAtual() {
        return pistaAtual;
    }

    public void setPistaAtual(Pista pistaAtual) {
        this.pistaAtual = pistaAtual;
    }

    public ArrayList<Piloto> getListaPilotos() {
        return listaPilotos;
    }

    public void setListaPilotos(ArrayList<Piloto> listaPilotos) {
        this.listaPilotos = listaPilotos;
    }
    
    
}
