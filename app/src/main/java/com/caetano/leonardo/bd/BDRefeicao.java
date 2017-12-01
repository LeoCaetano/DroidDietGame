package com.caetano.leonardo.bd;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.caetano.leonardo.dietgame.beans.Refeicao;

/**
 * Created by Leonardo on 24/10/2016.
 */
public class BDRefeicao {
    private SQLiteDatabase bd;
    public BDRefeicao(Context context){
        BDCore auxBd = new BDCore(context);
        bd = auxBd.getWritableDatabase();
    }

    public void inserir(Refeicao refeicao){
        ContentValues valores = new ContentValues();
        valores.put("Titulo", refeicao.getTitulo());
        valores.put("Descricao", refeicao.getDescricao());

        bd.insert("refeicao", null, valores);
    }

    public void atualizar(Refeicao refeicao){
        ContentValues valores = new ContentValues();
        valores.put("Titulo", refeicao.getTitulo());
        valores.put("Descricao", refeicao.getDescricao());

        bd.update("refeicao", valores, "id = ?", new String[]{""+refeicao.getId()});
    }

    public void deletar(Refeicao refeicao){
        bd.delete("refeicao", "id = "+refeicao.getId(), null);
    }

    public List<Refeicao> buscar(){
        List<Refeicao> list = new ArrayList<Refeicao>();
        String[] colunas = new String[]{"id", "Titulo", "Descricao"};

        Cursor cursor = bd.query("refeicao", colunas, null, null, null, null, "titulo ASC");

        if(cursor.getCount() > 0){
            cursor.moveToFirst();

            do{

                Refeicao ref = new Refeicao();
                ref.setId(cursor.getInt(0));
                ref.setTitulo(cursor.getString(1));
                ref.setDescricao(cursor.getString(2));
                list.add(ref);

            }while(cursor.moveToNext());
        }

        return(list);
    }

    public  Refeicao buscaPorId(int id){

        List<Refeicao> list = new ArrayList<Refeicao>();
        String[] colunas = new String[]{"id", "Titulo", "Descricao"};

        Cursor cursor = bd.query("refeicao", colunas, null, null, null, null, "where id="+id);
        Refeicao ref = new Refeicao();

        if(cursor.getCount() > 0){
            cursor.moveToFirst();
                ref.setId(cursor.getInt(0));
                ref.setTitulo(cursor.getString(1));
                ref.setDescricao(cursor.getString(2));
                list.add(ref);
        }

        return ref;


    }


}
