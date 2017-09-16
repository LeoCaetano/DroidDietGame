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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 */
public class BDRelatorio {

    private SQLiteDatabase bd;
    private SimpleDateFormat sdf;

    public BDRelatorio(Context context){
        BDCore auxBd = new BDCore(context);
        bd = auxBd.getWritableDatabase();
        sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);
    }

    public String mediaRefeicoesPorData(Date pDataInicio, Date pDataFim ){

        //transforma Data
        String dataInicio = sdf.format(pDataInicio);
        String dataFim = sdf.format(pDataFim);

        //pega diferenca data
        Calendar data1 = Calendar.getInstance();
        Calendar data2 = Calendar.getInstance();
        data1.setTime(pDataInicio);
        data2.setTime(pDataFim);

        int totalDias = data2.get(Calendar.DAY_OF_YEAR) -
                data1.get(Calendar.DAY_OF_YEAR);

        Cursor mCount= bd.rawQuery("select count(*) from registro where DATE(data_hora_registro) >= DATE('" + dataInicio + "') BETWEEN DATE(data_hora_registro) <= DATE('" + dataFim+ "')", null);
        mCount.moveToFirst();
        int count= mCount.getInt(0);
        mCount.close();

        String totalString = String.valueOf(count/totalDias);

        return totalString;
    }

    public void mediaRefeicoesAtrasadasPorData(Date pDataInicio, Date pDataFim ){

        String dataInicio = sdf.format(pDataInicio);
        String dataFim = sdf.format(pDataFim);
    }

    public String mediaCaloriasPorData(Date pDataInicio, Date pDataFim ){

        //transforma Data
        String dataInicio = sdf.format(pDataInicio);
        String dataFim = sdf.format(pDataFim);

        //pega diferenca data
        Calendar data1 = Calendar.getInstance();
        Calendar data2 = Calendar.getInstance();
        data1.setTime(pDataInicio);
        data2.setTime(pDataFim);

        int totalDias = data2.get(Calendar.DAY_OF_YEAR) - data1.get(Calendar.DAY_OF_YEAR);

        Cursor mCount= bd.rawQuery("select sum(caloriaConsumida) from registro_has_alimento regA, registro reg where  regA.registro_id = reg.id and DATE(reg.data_hora_registro) >= DATE('" + dataInicio + "') BETWEEN DATE(reg.data_hora_registro) <= DATE('" + dataFim+ "')", null);
        mCount.moveToFirst();
        int count= mCount.getInt(0);
        mCount.close();

        String totalString = String.valueOf(count/totalDias);

        return totalString;
    }

    public String totalCaloriasConsumidasPorData(Date pDataInicio, Date pDataFim ){
        String dataInicio = sdf.format(pDataInicio);
        String dataFim = sdf.format(pDataFim);

        Cursor cursor = bd.rawQuery("select sum(caloriaConsumida) from registro_has_alimento regA, registro reg where  regA.registro_id = reg.id and DATE(reg.data_hora_registro) >= DATE('" + dataInicio + "') BETWEEN DATE(reg.data_hora_registro) <= DATE('" + dataFim+ "')", null);
        cursor.moveToFirst();
        double qtdConsumida = cursor.getDouble(0);

        cursor.close();
        String retorno = String.valueOf(qtdConsumida);

        if (qtdConsumida>0) {

            return retorno;
        }
        else{
            return "Não foi possível encontrar informações com os parâmetros informados";
        }

    }
}
