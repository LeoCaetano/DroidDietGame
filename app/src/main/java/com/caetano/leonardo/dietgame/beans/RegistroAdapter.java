package com.caetano.leonardo.dietgame.beans;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.caetano.leonardo.bd.BDHorarioRefeicao;
import com.caetano.leonardo.dietgame.NovoHorarioActivity;
import com.caetano.leonardo.dietgame.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Leonardo on 13/11/2016.
 */
public class RegistroAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Registro> lista;
    DateFormat df = new SimpleDateFormat("HH:mm");



    public RegistroAdapter(Context pContext, ArrayList<Registro> pLista){
        this.context = pContext;
        this.lista = pLista;
    }

    @Override
    public int getCount() {
        return lista.size();
    }

    @Override
    public Object getItem(int position) {
        return lista.get(position);
    }

    @Override
    public long getItemId(int position) {
        return lista.get(position).getId();
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {

        Registro registro = lista.get(position);
        View layout;

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout = inflater.inflate(R.layout.registrorefeicoes, null);
        }
        else{
            layout = convertView;
        }

        TextView refeicao = (TextView) layout.findViewById(R.id.txtHoraCorreta);
        refeicao.setText("Refeição: "+ registro.getHorarioRefeicao().getRefeicao().getTitulo()+" - " + df.format(registro.getHorarioRefeicao().getHorarioConsumo()));

        TextView horario = (TextView) layout.findViewById(R.id.txtHoraRegistro);
        horario.setText( "Horario do Registro: " + df.format(registro.getDataHoraRegistro()));

        ImageView img = (ImageView) layout.findViewById(R.id.imgTrofeu);

        long horaRegistro = registro.getDataHoraRegistro().getHours();
        horaRegistro = (horaRegistro * 60) + registro.getDataHoraRegistro().getMinutes();


        long horaRefeicao = registro.getHorarioRefeicao().getHorarioConsumo().getHours();
        horaRefeicao = (horaRefeicao* 60) + registro.getHorarioRefeicao().getHorarioConsumo().getMinutes();

        long horaFinal = ComparaHora(horaRefeicao, horaRegistro);

        if(horaFinal <= 20)
            img.setImageResource(R.drawable.ouro);
        else if(horaFinal >20 && horaFinal <= 30)
            img.setImageResource(R.drawable.prata);
        else
            img.setImageResource(R.drawable.bronze);

        if(position % 2 == 0){
            layout.setBackgroundColor(Color.LTGRAY);
        }

        return layout;

    }

    public long ComparaHora(long HoraDoBanco, long HoraDoRegistro ){

        long diffMinutos;

        if(HoraDoBanco > HoraDoRegistro)
            diffMinutos = HoraDoBanco - HoraDoRegistro;
        else
            diffMinutos = HoraDoRegistro - HoraDoBanco;

        return  diffMinutos;
    }

}
