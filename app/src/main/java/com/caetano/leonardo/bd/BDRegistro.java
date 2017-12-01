package com.caetano.leonardo.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.method.DateTimeKeyListener;
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

import DTO.registrosDTO;

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
        valores.put("total_calorias", registro.getTotalConsumido());
        long id = bd.insert("registro", null, valores);

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

    public List<Registro> buscaRefeicoesPorData(Date pData) throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        sdf.setLenient(false);
        String dataString = sdf.format(pData);
        Log.i("Busca","inicio");
        List<Registro> list = new ArrayList<Registro>();

        Cursor cursor= bd.rawQuery("select * from registro where DATE(data_hora_registro) = DATE('" + dataString + "')", null);


        if(cursor.getCount() > 0){
            cursor.moveToFirst();

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
        Log.i("Busca",String.valueOf(list.size()));
        return(list);
    }

    public int registrosFeitos(Date pData){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);
        String dataString = sdf.format(pData);

        Cursor mCount= bd.rawQuery("select count(*) from registro where DATE(data_hora_registro) = DATE('" + dataString + "')", null);
        mCount.moveToFirst();
        int count= mCount.getInt(0);
        mCount.close();
        Log.i("teste r",dataString);
        return count;
    }

    public List<registrosDTO> buscaAcumulado(Date pData, int pIdServidor) throws ParseException {
        List<registrosDTO> lista = new ArrayList<registrosDTO>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        sdf.setLenient(false);
        String dataString = sdf.format(pData);

        //2
        SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");
        sdf2.setLenient(false);

        Cursor cursor= bd.rawQuery("select reg.id, reg.data_hora_registro, hor.horario_consumo, reg.total_calorias from registro reg, horario_refeicao hor where reg.horario_refeicao_id = hor.id and DATE('" + dataString + "')", null);


        if(cursor.getCount() > 0){
            cursor.moveToFirst();

            do{
                registrosDTO registro = new registrosDTO();

                //ID
                registro.setId(cursor.getInt(0));
                //data hora registro
                Date c =  sdf.parse(cursor.getString(1));
                registro.setDataHoraRegistro(cursor.getString(1));

                //horario registro
                Date d =  sdf2.parse(cursor.getString(2));
                registro.setHorarioRefeicao(cursor.getString(2));

                //calorias
                registro.setTotalCalorias(cursor.getInt(3));


                lista.add(registro);

            }while(cursor.moveToNext());
        }

        return lista;
    }

}
