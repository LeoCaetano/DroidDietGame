package com.caetano.leonardo.dietgame.beans;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Leonardo on 24/10/2016.
 */
public class Registro {
    private int Id;
    private Date dataHoraRegistro;
    private HorarioRefeicao horarioRefeicao;
    private ArrayList<Alimento> alimentosRefeicao = new ArrayList<>();

    public Registro(int pId, Date pDataHoraRegistro, HorarioRefeicao pHorarioRefeicao) {
        Id = pId;
        this.dataHoraRegistro = pDataHoraRegistro;
        this.horarioRefeicao = pHorarioRefeicao;
    }

    public Registro(){}

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public Date getDataHoraRegistro() {
        return dataHoraRegistro;
    }

    public void setDataHoraRegistro(Date dataHoraRegistro) {
        this.dataHoraRegistro = dataHoraRegistro;
    }

    public HorarioRefeicao getHorarioRefeicao() {
        return horarioRefeicao;
    }

    public void setHorarioRefeicao(HorarioRefeicao horarioRefeicao) {
        this.horarioRefeicao = horarioRefeicao;
    }

    public ArrayList<Alimento> getAlimentosRefeicao() {
        return this.alimentosRefeicao;
    }


    public void setAlimentosRefeicao(ArrayList<Alimento> listToBeSet) {
        if (listToBeSet != null)
            this.alimentosRefeicao.addAll(listToBeSet);
    }

}
