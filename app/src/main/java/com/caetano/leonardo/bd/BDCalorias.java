package com.caetano.leonardo.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.caetano.leonardo.dietgame.beans.Alimento;
import com.caetano.leonardo.dietgame.beans.HorarioRefeicao;
import com.caetano.leonardo.dietgame.beans.Registro;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 */
public class BDCalorias {
    private SQLiteDatabase bd;
    public BDCalorias(Context context){
        BDCore auxBd = new BDCore(context);
        bd = auxBd.getWritableDatabase();
    }

    public void inserir(Alimento pAlimento, double pQuantidade){

        Date data = new Date(System.currentTimeMillis());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-DD HH:MM");
        String horaMin = sdf.format(data);

        ContentValues valores = new ContentValues();
        valores.put("data_hora_registro", horaMin);
        valores.put("quantidade", pQuantidade);
        valores.put("Alimento_id",pAlimento.getId());
       // bd.insert("registro_caloria", null, valores);
        Log.i("Banco", horaMin);
    }


    public void atualizar(Registro registro){
        ContentValues valores = new ContentValues();
        valores.put("data_hora_registro", String.valueOf(registro.getDataHoraRegistro()));
        valores.put("horario_refeicao_id", registro.getHorarioRefeicao().getId());

        bd.update("registro", valores, "id = ?", new String[]{"" + registro.getId()});
    }


    public int buscar(){

        Cursor cursor = bd.rawQuery("select sum(caloriaConsumida) from registro_has_alimento regA, registro reg where  regA.registro_id == reg.id and DATE(reg.data_hora_registro) == DATE('now');", null);
        cursor.moveToFirst();
        double qtdConsumida = cursor.getDouble(0);
        cursor.close();

        int finalInt;
        if (qtdConsumida>0) {
            finalInt= (int) qtdConsumida;

            return finalInt;
        }
        else{
            return 0;
        }

    }

}
