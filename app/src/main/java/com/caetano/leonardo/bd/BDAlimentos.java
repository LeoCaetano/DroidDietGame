package com.caetano.leonardo.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.caetano.leonardo.dietgame.beans.Alimento;
import com.caetano.leonardo.dietgame.beans.Registro;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 */
public class BDAlimentos {
    private SQLiteDatabase bd;
    public BDAlimentos(Context context){
        BDCore auxBd = new BDCore(context);
        bd = auxBd.getWritableDatabase();
    }

    public List<Alimento> buscaAlimento(String nomeAlimento){

        List<Alimento> alimentos = null;

        String[] colunas = new String[]{"id", "descricao","kcal"};

        if (!nomeAlimento.isEmpty()) {
            alimentos = new ArrayList<Alimento>();
            Cursor cursor = bd.query("Alimento", colunas,"descricao like '%" + nomeAlimento + "%'",null, null, null, null);

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Alimento ali = new Alimento();
                ali.setId(cursor.getInt(0));
                ali.setDescricao(cursor.getString(1));
                ali.setQtdCaloria(cursor.getInt(2));
                alimentos.add(ali);
                cursor.moveToNext();
            }
            cursor.close();

        }
        return alimentos;

    }

}
