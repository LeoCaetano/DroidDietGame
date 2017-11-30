package DTO;

import java.util.ArrayList;
import java.util.Date;

public class registrosDTO {

    private int Id;
    private Date DataHoraRegistro;
    private Date HorarioRefeicao;
    private int TotalCalorias;
    private int IdUsuario;

    public registrosDTO(){};

    public registrosDTO(int id, Date dataHoraRegistro, Date horarioRefeicao, int totalCalorias, int idUsuario) {
        this.Id = id;
        this.DataHoraRegistro = dataHoraRegistro;
        this.HorarioRefeicao = horarioRefeicao;
        this.TotalCalorias = totalCalorias;
        this.IdUsuario = idUsuario;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public Date getDataHoraRegistro() {
        return DataHoraRegistro;
    }

    public void setDataHoraRegistro(Date dataHoraRegistro) {
        DataHoraRegistro = dataHoraRegistro;
    }

    public Date getHorarioRefeicao() {
        return HorarioRefeicao;
    }

    public void setHorarioRefeicao(Date horarioRefeicao) {
        HorarioRefeicao = horarioRefeicao;
    }

    public int getTotalCalorias() {
        return TotalCalorias;
    }

    public void setTotalCalorias(int totalCalorias) {
        TotalCalorias = totalCalorias;
    }

    public int getIdUsuario() {
        return IdUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        IdUsuario = idUsuario;
    }
}
