package com.caetano.leonardo.dietgame;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.caetano.leonardo.bd.BDAlimentos;
import com.caetano.leonardo.bd.BDHorarioRefeicao;
import com.caetano.leonardo.dietgame.beans.Alimento;
import com.caetano.leonardo.dietgame.beans.AlimentoAdapter;
import com.caetano.leonardo.dietgame.beans.HorarioRefeicao;
import com.caetano.leonardo.dietgame.beans.HorarioRefeicaoAdapter;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class Seleciona_Alimento extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleciona_alimento);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Intent intent = getIntent();

        String txtBusca = intent.getStringExtra("txt");

        BDAlimentos bd = new BDAlimentos(this);

        List<Alimento> lista = null;
        try {
            lista = bd.buscaAlimento(txtBusca);
        } catch ( Exception e) {
            e.printStackTrace();
        }

        ArrayList<Alimento> listaArray = new ArrayList<Alimento>();
        listaArray.addAll(lista);
        ListView lv = (ListView) findViewById(R.id.lstSelecao);

        lv.setAdapter(new AlimentoAdapter(this, listaArray));
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView adapter, View viw, int posicao,long id) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(Seleciona_Alimento.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_valores_alimento, null);

                final EditText mPorcao = (EditText) mView.findViewById(R.id.txtPorcaoComida);
                final EditText mQuantidadeP = (EditText) mView.findViewById(R.id.txtQuantidade);
                Button mAdiciona = (Button) mView.findViewById(R.id.btnAdc);
                mBuilder.setView(mView);
                final Alimento alimentoADC = (Alimento) adapter.getItemAtPosition(posicao);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();

                mAdiciona.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        double dPorcao = Double.parseDouble(mPorcao.getText().toString());
                        double dQuantidade = Double.parseDouble(mQuantidadeP.getText().toString());

                        double total = ((dPorcao * dQuantidade) * alimentoADC.getQtdCaloria())/100;

                        alimentoADC.setTotalRefeicao(total);

                        Intent intent = new Intent();
                        intent.putExtra("objetoAlimento", alimentoADC);

                        dialog.dismiss();

                        setResult(1, intent);
                        finish();

                    }
                });
            }
        });

    }

}
