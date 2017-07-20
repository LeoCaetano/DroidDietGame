package com.caetano.leonardo.dietgame.beans;

import java.io.Serializable;

/**
 * Created by Leonardo on 25/09/2016.
 */
public class Refeicao implements Serializable {
    private int Id;
    private String Titulo;
    private String Descricao;

    public Refeicao(int id, String titulo, String descricao) {
        this.Id = id;
        this.Titulo = titulo;
        this.Descricao = descricao;
    }

    public Refeicao(){}

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getTitulo() {
        return Titulo;
    }

    public void setTitulo(String titulo) {
        Titulo = titulo;
    }

    public String getDescricao() {
        return Descricao;
    }

    public void setDescricao(String descricao) {
        Descricao = descricao;
    }

    @Override
    public String toString() {
        return Titulo;
    }
}
