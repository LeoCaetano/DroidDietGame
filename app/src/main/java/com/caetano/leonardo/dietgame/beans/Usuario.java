package com.caetano.leonardo.dietgame.beans;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Leonardo on 24/10/2016.
 */
public class Usuario{
    private int Id;
    private int IdServidor;
    private String Nome;
    private int QtdRefeicoesDia;
    private double QtdCaloriasDia;
    private boolean PrimeiraUtilizacao;

    public Usuario(){
        
    }

    public Usuario(String Nome, int QtdRefeicoesDia, double QtdCaloriasDia) {
        this.Nome = Nome;
        this.QtdRefeicoesDia = QtdRefeicoesDia;
        this.QtdCaloriasDia = QtdCaloriasDia;
    }

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public int getIdServidor() {
        return IdServidor;
    }

    public void setIdServidor(int IdServidor) {
        this.IdServidor = IdServidor;
    }

    public String getNome() {
        return Nome;
    }

    public void setNome(String Nome) {
        this.Nome = Nome;
    }

    public int getQtdRefeicoesDia() {
        return QtdRefeicoesDia;
    }

    public void setQtdRefeicoesDia(int QtdRefeicoesDia) {
        this.QtdRefeicoesDia = QtdRefeicoesDia;
    }

    public double getQtdCaloriasDia() {
        return QtdCaloriasDia;
    }

    public void setQtdCaloriasDia(double QtdCaloriasDia) {
        this.QtdCaloriasDia = QtdCaloriasDia;
    }

    public boolean isPrimeiraUtilizacao() {
        return PrimeiraUtilizacao;
    }

    public void setPrimeiraUtilizacao(boolean PrimeiraUtilizacao) {
        this.PrimeiraUtilizacao = PrimeiraUtilizacao;
    }
}
