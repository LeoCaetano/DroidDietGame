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

    public void atualizarQtdRefeicoes(boolean Insere){

        int quantidadeR;

        if (Insere)
            quantidadeR = buscaQtdCalorias()+1;
        else
            quantidadeR = buscaQtdCalorias()-1;

        ContentValues valores = new ContentValues();
        valores.put("qtd_refeicoes_dia",  quantidadeR);
        bd.update("usuario", valores, null, null);
    }

    public void atualizarQtdCalorias(){

        int quantidadeC = buscaQtdCalorias();

        ContentValues valores = new ContentValues();
        valores.put("qtd_calorias_dia",  quantidadeC);
        bd.update("usuario", valores, null, null);
    }


    public int buscaQtdRefeicoes() {

        Cursor mCount= bd.rawQuery("select count(*) from horario_refeicao where ativo = 'S'", null);
        mCount.moveToFirst();
        int count= mCount.getInt(0);
        mCount.close();

        return count;
    }


    public int buscaQtdCalorias(){
        int qtdCalorias = 0;
        String[] colunas = new String[]{"id", "qtd_calorias_dia"};

        Cursor cursor = bd.query("usuario", colunas, null, null, null, null, "id ASC");
        if(cursor.getCount() > 0){
            cursor.moveToFirst();
            do{
                qtdCalorias = cursor.getInt(1);
                Log.i("sql",String.valueOf(cursor.getLong(1)));
            }while(cursor.moveToNext());
        }
        return(qtdCalorias);
    }

}
