package com.caetano.leonardo.dietgame.beans;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.caetano.leonardo.bd.BDHorarioRefeicao;
import com.caetano.leonardo.dietgame.NovoHorarioActivity;
import com.caetano.leonardo.dietgame.R;

/**
 * Created by Leonardo on 28/05/2017.
 */
public class AlimentoAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Alimento> lista;
    DateFormat df = new SimpleDateFormat("HH:mm");

    public AlimentoAdapter(Context pContext, ArrayList<Alimento> pLista){
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

        Alimento alimento = lista.get(position);
        View layout;

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout = inflater.inflate(R.layout.listaalimentos, null);
        }
        else{
            layout = convertView;
        }


        TextView txtAlimento = (TextView) layout.findViewById(R.id.txtAlimento);
        txtAlimento.setText("Alimento: "+alimento.getDescricao());

        TextView txtCaloria = (TextView) layout.findViewById(R.id.txtQTDCalorias);
        txtCaloria.setText("Caloria:"+ alimento.getQtdCaloria());

        if(position % 2 == 0){
            layout.setBackgroundColor(Color.LTGRAY);
        }

        return layout;

    }
}
