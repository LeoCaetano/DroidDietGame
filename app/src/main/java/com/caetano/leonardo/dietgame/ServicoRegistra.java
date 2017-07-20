package com.caetano.leonardo.dietgame;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.caetano.leonardo.bd.BDHorarioRefeicao;
import com.caetano.leonardo.bd.BDRegistro;
import com.caetano.leonardo.dietgame.beans.HorarioRefeicao;
import com.caetano.leonardo.dietgame.beans.Registro;

import java.util.Date;

/**
 * Created by Leonardo on 15/11/2016.
 */
public class ServicoRegistra  extends IntentService {

    public static final String ID_HORA = "HORAS";

    public ServicoRegistra() {
        super("Service_Registra");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.i("Serviço","Start");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("Servico","Stop");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Bundle bundle = intent.getExtras();
        HorarioRefeicao horaRef = (HorarioRefeicao) bundle.getSerializable("objeto");

        BDHorarioRefeicao bdREF = new BDHorarioRefeicao(this);

        Date dataRegistro = new Date(System.currentTimeMillis());
        Registro registro = new Registro();

        registro.setHorarioRefeicao(horaRef);
        registro.setDataHoraRegistro(dataRegistro);

        BDRegistro bd = new BDRegistro(this);

        bd.inserir(registro);

       this.stopSelf();

    }
}
