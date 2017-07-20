package com.caetano.leonardo.dietgame.beans;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Leonardo on 24/10/2016.
 */
public class HorarioRefeicao implements Serializable {
    private int Id;
    private Date horarioConsumo;
    private Refeicao refeicao;
    private String Ativo;

    public HorarioRefeicao(int pId, Date pHorarioConsumo, Refeicao pRefeicao){
        this.Id = pId;
        this.horarioConsumo = pHorarioConsumo;
        this.refeicao = pRefeicao;

    }

    public HorarioRefeicao(){}

    public String getAtivo() {
        return Ativo;
    }

    public void setAtivo(String ativo) {
        Ativo = ativo;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public Date getHorarioConsumo() {
        return horarioConsumo;
    }

    public void setHorarioConsumo(Date horarioConsumo) {
        this.horarioConsumo = horarioConsumo;
    }

    public Refeicao getRefeicao() {
        return refeicao;
    }

    public void setRefeicao(Refeicao refeicao) {
        this.refeicao = refeicao;
    }

    @Override
    public String toString() {
        return "\nHorarioRefeicao{" +
                "Id=" + Id +
                ", horarioConsumo=" + horarioConsumo +
                ",\n refeicao=" + refeicao +
                '}';
    }
}
