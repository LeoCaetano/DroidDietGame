package com.caetano.leonardo.dietgame;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.caetano.leonardo.dietgame.beans.HorarioRefeicao;

import java.util.Calendar;

public class AlarmesRefeicao {

    HorarioRefeicao horarioRefeicao;
    AlarmManager Alarme;
    Context contexto;

    AlarmesRefeicao(HorarioRefeicao pHorarioRefeicao, AlarmManager pAlarme, Context pContexto ){
        this.horarioRefeicao = pHorarioRefeicao;
        this.Alarme = pAlarme;
        this.contexto = pContexto;
    }


    public void criarAlarme(boolean novoAlarme) {
        boolean alarmeAtivo = (PendingIntent.getBroadcast(contexto, 0, new Intent("ALARME_DISPARADO"), PendingIntent.FLAG_UPDATE_CURRENT) == null);

        if (novoAlarme) {
            Log.i("TesteAlarmes:", "Criação");

            Intent intent = new Intent("ALARME_DISPARADO");
            intent.putExtra("objeto", horarioRefeicao);

            int _id = this.horarioRefeicao.getId();
            PendingIntent p = PendingIntent.getBroadcast(contexto, _id, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(System.currentTimeMillis());
            c.set(Calendar.HOUR_OF_DAY, horarioRefeicao.getHorarioConsumo().getHours());
            c.set(Calendar.MINUTE, horarioRefeicao.getHorarioConsumo().getMinutes());
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MILLISECOND, 0);

            Alarme.setRepeating(AlarmManager.RTC, c.getTimeInMillis(), AlarmManager.INTERVAL_DAY  , p);

        } else if (!novoAlarme){

            Log.i("TesteAlarmes:", "Edição");

            Intent intent = new Intent("ALARME_DISPARADO");
            intent.putExtra("objeto", horarioRefeicao);

            int _id = this.horarioRefeicao.getId();
            PendingIntent p = PendingIntent.getBroadcast(contexto, _id, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(System.currentTimeMillis());
            c.set(Calendar.HOUR_OF_DAY, horarioRefeicao.getHorarioConsumo().getHours());
            c.set(Calendar.MINUTE, horarioRefeicao.getHorarioConsumo().getMinutes());
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MILLISECOND, 0);

            Alarme.setTime(c.getTimeInMillis());
        }
        else{

            Log.i("TesteAlarmes:", "Cancelamento");

            Intent intent = new Intent("ALARME_DISPARADO");
            intent.putExtra("objeto", horarioRefeicao);

            int _id = this.horarioRefeicao.getId();
            PendingIntent p = PendingIntent.getBroadcast(contexto, _id, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(System.currentTimeMillis());
            c.set(Calendar.HOUR_OF_DAY, horarioRefeicao.getHorarioConsumo().getHours());
            c.set(Calendar.MINUTE, horarioRefeicao.getHorarioConsumo().getMinutes());
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MILLISECOND, 0);

            Alarme.cancel(p);

        }
    }
}
