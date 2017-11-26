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
import android.widget.Toast;

import com.caetano.leonardo.bd.BDHorarioRefeicao;
import com.caetano.leonardo.bd.BDRefeicao;
import com.caetano.leonardo.dietgame.NovoHorarioActivity;
import com.caetano.leonardo.dietgame.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by Leonardo on 13/11/2016.
 */
public class HorarioRefeicaoAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<HorarioRefeicao> lista;
    DateFormat df = new SimpleDateFormat("HH:mm");



    public HorarioRefeicaoAdapter(Context pContext, ArrayList<HorarioRefeicao> pLista){
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

        HorarioRefeicao horarioRefeicao = lista.get(position);
        View layout;

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout = inflater.inflate(R.layout.horariorefeicoes, null);
        }
        else{
            layout = convertView;
        }


        TextView refeicao = (TextView) layout.findViewById(R.id.txtRefeicao);
        refeicao.setText(horarioRefeicao.getRefeicao().getTitulo());

        TextView horario = (TextView) layout.findViewById(R.id.txtHorario);
        horario.setText( df.format(horarioRefeicao.getHorarioConsumo()));

        ImageView imgEditar = (ImageView) layout.findViewById(R.id.img_editar);
        imgEditar.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, NovoHorarioActivity.class);
                HorarioRefeicao horaRefeicao;
                horaRefeicao = lista.get(position);

                intent.putExtra("horarioRefeicao", horaRefeicao);
                context.startActivity(intent);

            }
        });

        ImageView imgExcluir = (ImageView) layout.findViewById(R.id.img_excluir);
        imgExcluir .setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View arg0) {

                HorarioRefeicao horaRefeicao;
                horaRefeicao = lista.get(position);
                BDHorarioRefeicao bd = new BDHorarioRefeicao(context);
                bd.deletar(horaRefeicao);

            }
        }
        );

        if(position % 2 == 0){
            layout.setBackgroundColor(Color.LTGRAY);
        }

        return layout;

    }
}
