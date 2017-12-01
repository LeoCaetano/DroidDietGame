package com.caetano.leonardo.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.caetano.leonardo.dietgame.beans.HorarioRefeicao;
import com.caetano.leonardo.dietgame.beans.Refeicao;
import com.caetano.leonardo.dietgame.beans.Usuario;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Leonardo on 24/10/2016.
 */
public class BDUsuario {
    private SQLiteDatabase bd;

    public BDUsuario(Context context){
        BDCore auxBd = new BDCore(context);
        bd = auxBd.getWritableDatabase();
    }

    public void inserir(Usuario usuario){

        ContentValues valores = new ContentValues();
        valores.put("nome", usuario.getNome());
        valores.put("qtd_refeicoes_dia", usuario.getQtdRefeicoesDia());
        valores.put("qtd_calorias_dia", usuario.getQtdCaloriasDia());
        valores.put("primeira_utilizacao", usuario.isPrimeiraUtilizacao());
        bd.insert("usuario", null, valores);
    }

    public int buscaIdUsuario(){
        Cursor mCount= bd.rawQuery("select Ids from usuario", null);
        mCount.moveToFirst();
        int count= mCount.getInt(0);
        mCount.close();

        return count;
    }

    public void insertIdUsuario(int pID){
        ContentValues valores = new ContentValues();
        valores.put("id", pID);
        bd.insert("usuario", null, valores);
    }

    public void atualizarQtdRefeicoes(boolean Insere){

        int quantidadeR;

        if (Insere)
            quantidadeR = buscaQtdRefeicoes();
        else
            quantidadeR = buscaQtdRefeicoes()-1;

        ContentValues valores = new ContentValues();
        valores.put("qtd_refeicoes_dia",  quantidadeR);
        bd.update("usuario", valores, null, null);

        atualizaLog(quantidadeR, "R");
    }

    public void atualizarQtdCalorias(int pNovoValor){

        ContentValues valores = new ContentValues();
        valores.put("qtd_calorias_dia",  pNovoValor);
        bd.update("usuario", valores, null, null);

        atualizaLog(pNovoValor, "'C'");

    }

    public int buscaQtdRefeicoes() {

        Cursor mCount= bd.rawQuery("select count(*) from horario_refeicao where ativo = 'S'", null);
        mCount.moveToFirst();
        int count= mCount.getInt(0);
        mCount.close();

        return count;
    }

    public int buscaQtdCaloriasLog(Date pData){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);
        String dataString = sdf.format(pData);

        Cursor mCount= bd.rawQuery("select max(id), valor_novo from Log_registro WHERE DATE(data_alteracao) < DATE('" + dataString + "') and item_alterado = 'C'", null);
        mCount.moveToFirst();
        int count= mCount.getInt(1);
        mCount.close();

        if(count==0)
            count = CaloriasUsuario();

        return count;

    }

    public int buscaQtdRefeicoesLog(Date pData){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);
        String dataString = sdf.format(pData);

        Cursor mCount= bd.rawQuery( "select max(id), valor_novo from log_registro where log_registro.item_alterado = 'R' AND DATE(data_alteracao) < DATE('" + dataString + "')", null);
        mCount.moveToFirst();
        int count= mCount.getInt(1);
        mCount.close();

        if(count==0)
            count = RefeicoesUsuario();

        return count;

    }

    public int CaloriasUsuario(){
        Cursor mCount= bd.rawQuery("select qtd_calorias_dia from usuario", null);
        mCount.moveToFirst();
        int count= mCount.getInt(0);
        mCount.close();

        return count;

    }

    public int RefeicoesUsuario(){
        Cursor mCount= bd.rawQuery("select qtd_refeicoes_dia from usuario", null);
        mCount.moveToFirst();
        int count= mCount.getInt(0);
        mCount.close();

        return count;

    }

    private void atualizaLog(int pValorNovo, String pItemAlteracao){

        Date data = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String horaMin = sdf.format(data);

        ContentValues valores = new ContentValues();
        int valorAntigo;

        if(pItemAlteracao == "R"){
            valorAntigo = buscaQtdRefeicoes();

        }else{
            valorAntigo = CaloriasUsuario();

        }


        valores.put("valor_anterior", valorAntigo);
        valores.put("valor_novo", pValorNovo);
        valores.put("data_alteracao", horaMin);
        valores.put("item_alterado", pItemAlteracao);
        bd.insert("usuario", null, valores);
    }

}
