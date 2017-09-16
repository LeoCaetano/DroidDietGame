package com.caetano.leonardo.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.caetano.leonardo.dietgame.beans.HorarioRefeicao;
import com.caetano.leonardo.dietgame.beans.Refeicao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by Leonardo on 24/10/2016.
 */
public class BDHorarioRefeicao {
    private SQLiteDatabase bd;
    private Context context;

    public BDHorarioRefeicao(Context pContext){
        context = pContext;
        BDCore auxBd = new BDCore(context);
        bd = auxBd.getWritableDatabase();
    }

    public long inserir(HorarioRefeicao horario){

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String horaMin = sdf.format(horario.getHorarioConsumo());
        Log.i("Script", sdf.format(horario.getHorarioConsumo()));


        ContentValues valores = new ContentValues();
        valores.put("horario_consumo", horaMin);
        valores.put("refeicao_id", horario.getRefeicao().getId());
        valores.put("ativo", "S");

        long id = bd.insert("horario_refeicao", null, valores);

        BDUsuario bdUsu = new BDUsuario(context);
        bdUsu.atualizarQtdRefeicoes(true );

        return id;
    }

    public void atualizar(HorarioRefeicao horario){

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String horaMin = sdf.format(horario.getHorarioConsumo());

        ContentValues valores = new ContentValues();

        valores.put("horario_consumo",  horaMin);
        valores.put("refeicao_id", horario.getRefeicao().getId());

        bd.update("horario_refeicao", valores, "id = ?", new String[]{"" + horario.getId()});
    }

    public void deletar(HorarioRefeicao horario){
       // bd.delete("horario_refeicao", "id = " + horario.getId(), null);
        ContentValues valores = new ContentValues();

        valores.put("Ativo", "N");
        bd.update("horario_refeicao", valores, "id = ?", new String[]{"" + horario.getId()});

        BDUsuario bdUsu = new BDUsuario(context);
        bdUsu.atualizarQtdRefeicoes(false);
    }

    public List<HorarioRefeicao> buscar() throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        sdf.setLenient(false);

        List<HorarioRefeicao> list = new ArrayList<HorarioRefeicao>();
        String[] colunas = new String[]{"id", "horario_consumo","ativo", "refeicao_id"};

        Cursor cursor = bd.query("horario_refeicao", colunas, "ativo = 'S'" , null, null, null, "id ASC");

       if(cursor.getCount() > 0){
            cursor.moveToFirst();

            do{
                HorarioRefeicao horarioRef = new HorarioRefeicao();

                horarioRef.setId(cursor.getInt(0));
                Date c = sdf.parse(cursor.getString(1));
                horarioRef.setHorarioConsumo(c);
                horarioRef.setAtivo(cursor.getString(2));
                horarioRef.setRefeicao(buscaRefeicaoPorId(cursor.getInt(3)));

                list.add(horarioRef);

            }while(cursor.moveToNext());
        }

        return(list);
    }

    public void atualizaUsuario (int quantidade){

        ContentValues valores = new ContentValues();

        valores.put("qtd_refeicoes_dia",  quantidade);

        bd.update("usuario", valores, null, null);

    }

    public  Refeicao buscaRefeicaoPorId(int id){

        List<Refeicao> list = new ArrayList<Refeicao>();

        String argumentoId = String.valueOf(id);

        String[] argumentos = new String[]{ argumentoId };
        String[] colunas = new String[]{"id", "Titulo", "Descricao"};

        Cursor cursor = bd.query("refeicao", colunas, "id=?", argumentos, null, null, null, null);
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

    public  HorarioRefeicao buscaPorId(int id) throws ParseException {

        String argumentoId = String.valueOf(id);
        int codigoRefeicao = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        sdf.setLenient(false);

        List<HorarioRefeicao> list = new ArrayList<HorarioRefeicao>();
        String[] argumentos = new String[]{ argumentoId };
        String[] colunas = new String[]{"id", "horario_consumo", "refeicao_id"};

        Cursor cursor = bd.query("horario_refeicao", colunas, "id=?", argumentos, null, null, null, null);
        Log.i("completoSQL",cursor.toString());
        HorarioRefeicao ref = new HorarioRefeicao();

        if(cursor.getCount() > 0) {

            cursor.moveToFirst();

            ref.setId(cursor.getInt(0));
            Date c = sdf.parse(cursor.getString(1));
            ref.setHorarioConsumo(c);
            codigoRefeicao = cursor.getInt(0);
            list.add(ref);
        }
        ref.setRefeicao(buscaRefeicaoPorId(codigoRefeicao));
        return ref;
    }


}
