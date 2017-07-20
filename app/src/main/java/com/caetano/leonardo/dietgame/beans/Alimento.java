package com.caetano.leonardo.dietgame.beans;

import java.io.Serializable;

/**
 * Created by Leonardo on 25/09/2016.
 */
public class Alimento implements Serializable {
    private int Id;
    private String Descricao;
    private int QtdCaloria;
    private double TotalRefeicao;

    public Alimento(int id, String descricao, int qtdCaloria) {
        this.Id = id;
        this.Descricao = descricao;
        this.QtdCaloria = qtdCaloria;
    }

    public Alimento(){}

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getDescricao() {
        return Descricao;
    }

    public void setDescricao(String descricao) {
        Descricao = descricao;
    }

    public int getQtdCaloria() {
        return QtdCaloria;
    }

    public void setQtdCaloria(int qtdCaloria) {
        QtdCaloria = qtdCaloria;
    }

    public double getTotalRefeicao() {
        return this.TotalRefeicao;
    }

    public void setTotalRefeicao(double totalRefeicao) {

        this.TotalRefeicao = totalRefeicao;
    }


}
