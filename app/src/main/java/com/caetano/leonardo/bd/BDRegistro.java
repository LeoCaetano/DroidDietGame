package com.caetano.leonardo.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.caetano.leonardo.dietgame.beans.Alimento;
import com.caetano.leonardo.dietgame.beans.HorarioRefeicao;
import com.caetano.leonardo.dietgame.beans.Refeicao;
import com.caetano.leonardo.dietgame.beans.Registro;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 */
public class BDRegistro {
    private SQLiteDatabase bd;
    private BDHorarioRefeicao bdHorarioRefeicao;
    public BDRegistro(Context context){
        BDCore auxBd = new BDCore(context);
        bd = auxBd.getWritableDatabase();
        bdHorarioRefeicao = new BDHorarioRefeicao(context);
    }

    public void inserir(Registro registro){

        Date data = new Date(System.currentTimeMillis());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:MM");
        String horaMin = sdf.format(data);

        ContentValues valores = new ContentValues();
        valores.put("data_hora_registro", horaMin);
        valores.put("horario_refeicao_id", registro.getHorarioRefeicao().getId());
        bd.insert("registro", null, valores);
        Log.i("Banco", horaMin);
    }

    public void inserirNovo(Registro registro){

        Date data = new Date(System.currentTimeMillis());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String horaMin = sdf.format(data);

        ContentValues valores = new ContentValues();
        valores.put("data_hora_registro", horaMin);
        valores.put("horario_refeicao_id", registro.getHorarioRefeicao().getId());
        long id = bd.insert("registro", null, valores);

        //chama inserir nregistro_has_alimento
        inserirHasAlimento(id, registro.getAlimentosRefeicao());
        Log.i("Banco", horaMin);
    }

    public void inserirHasAlimento(long pId, ArrayList<Alimento> pAlimentos){

        for (Alimento item : pAlimentos) {
            ContentValues valores = new ContentValues();
            valores.put("registro_id", pId);
            valores.put("Alimento_id", item.getId());
            valores.put("caloriaConsumida", item.getTotalRefeicao());

            long id = bd.insert("registro_has_Alimento", null, valores);
        }


    }

    public void atualizar(Registro registro){
        ContentValues valores = new ContentValues();
        valores.put("data_hora_registro", String.valueOf(registro.getDataHoraRegistro()));
        valores.put("horario_refeicao_id", registro.getHorarioRefeicao().getId());

        bd.update("registro", valores, "id = ?", new String[]{"" + registro.getId()});
    }

    public void deletar(Registro registro){
        bd.delete("registro", "id = "+registro.getId(), null);
    }

    public List<Registro> buscar() throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-DD HH:MM");
        sdf.setLenient(false);

        List<Registro> list = new ArrayList<Registro>();
        String[] colunas = new String[]{"id", "data_hora_registro", "horario_refeicao_id"};

        Cursor cursor = bd.query("registro", colunas, null, null, null, null, "id ASC");

        Log.i("colunas",colunas[0].toString());
        if(cursor.getCount() > 0){
            cursor.moveToFirst();
            Log.i("Inicio","Busca");

            do{

                Registro registro = new Registro();
                HorarioRefeicao horarioRefeicao = new HorarioRefeicao();

                registro.setId(cursor.getInt(0));
                Date c =  sdf.parse(cursor.getString(1));
                horarioRefeicao.setId(cursor.getInt(2));
                registro.setDataHoraRegistro(c);

                registro.setHorarioRefeicao(bdHorarioRefeicao.buscaPorId(horarioRefeicao.getId()));
                list.add(registro);

                Log.i("Hora",String.valueOf(cursor.getLong(1)));

            }while(cursor.moveToNext());
        }

        return(list);
    }

    public int buscaRefeicoes() throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-DD HH:MM");
        sdf.setLenient(false);

        List<Registro> list = new ArrayList<Registro>();
        String[] colunas = new String[]{"id", "data_hora_registro", "horario_refeicao_id"};

        Cursor cursor = bd.query("registro", colunas, null, null, null, null, "id ASC");

        Log.i("colunas",colunas[0].toString());
        if(cursor.getCount() > 0){
            cursor.moveToFirst();
            Log.i("Inicio","Busca");

            do{

                Registro registro = new Registro();
                HorarioRefeicao horarioRefeicao = new HorarioRefeicao();

                registro.setId(cursor.getInt(0));
                Date c =  sdf.parse(cursor.getString(1));
                horarioRefeicao.setId(cursor.getInt(2));
                registro.setDataHoraRegistro(c);

                registro.setHorarioRefeicao(bdHorarioRefeicao.buscaPorId(horarioRefeicao.getId()));
                list.add(registro);

                Log.i("Hora",String.valueOf(cursor.getLong(1)));

            }while(cursor.moveToNext());
        }

        int qtd = list.size();
        return qtd;
    }

    public int registrosFeitos(Date pData){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);
        String dataString = sdf.format(pData);

        Cursor mCount= bd.rawQuery("select count(*) from registro where DATE(data_hora_registro) = DATE('" + dataString + "')", null);
        mCount.moveToFirst();
        int count= mCount.getInt(0);
        mCount.close();

        return count;
    }

}
