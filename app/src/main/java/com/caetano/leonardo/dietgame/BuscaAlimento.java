package com.caetano.leonardo.dietgame;

import android.content.DialogInterface;
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
import android.widget.TextView;
import android.widget.Toast;

import com.caetano.leonardo.bd.BDAlimentos;
import com.caetano.leonardo.bd.BDHorarioRefeicao;
import com.caetano.leonardo.bd.BDRegistro;
import com.caetano.leonardo.dietgame.beans.Alimento;
import com.caetano.leonardo.dietgame.beans.AlimentoAdapter;
import com.caetano.leonardo.dietgame.beans.HorarioRefeicao;
import com.caetano.leonardo.dietgame.beans.Registro;

import java.util.ArrayList;
import java.util.Date;

public class BuscaAlimento extends AppCompatActivity {
    public static final int TELA_LISTAGEM = 1;
    public ArrayList<Alimento> alimentosSelecionados = new ArrayList<Alimento>();
    public HorarioRefeicao HorarioRefeicaoNovo;
    public ListView lv;
    public AlimentoAdapter adapter;
    double totalCaloria = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busca_alimento);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        HorarioRefeicaoNovo = (HorarioRefeicao) bundle.getSerializable("horario");

    }

    public void BuscaAlimentos(View view){
        TextView txtAlimentoBusca = (TextView) findViewById (R.id.txtBuscaAlimento);

        if(txtAlimentoBusca.getText().toString() == null)
            return;

        Intent intent = new Intent(BuscaAlimento.this, Seleciona_Alimento.class);

        String txt = "";
        txt = txtAlimentoBusca.getText().toString();
        Bundle bundle = new Bundle();

        bundle.putString("txt", txt);
        intent.putExtras(bundle);

        startActivityForResult(intent, TELA_LISTAGEM);

    }

    protected void onActivityResult(int codigoTela, int resultado, Intent intent){

        if(codigoTela == TELA_LISTAGEM){
            Bundle params = intent.getExtras();
            if(params != null){

                Alimento alimentoPraAdicionar = (Alimento) params.getSerializable("objetoAlimento");
                alimentosSelecionados.add(alimentoPraAdicionar);
                Toast.makeText(this, "Alimento Adicionado", Toast.LENGTH_LONG).show();
                AtualizaTexto();

            }
        }

    }

    @Override
    protected void onResume(){
        super.onResume();

        lv = (ListView) findViewById(R.id.lstSelecionados);
        adapter = new AlimentoAdapter(this, alimentosSelecionados);
        lv.setAdapter(adapter);

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            // setting onItemLongClickListener and passing the position to the function
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                DeletaDaLista(position);

                return true;
            }
        });

    }

    public void DeletaDaLista(int i){

        final int deletePosition = i;

        AlertDialog.Builder alert = new AlertDialog.Builder(BuscaAlimento.this);

        alert.setTitle("Excluir!");
        alert.setMessage("Deseja Excluir esse alimento?");
        alert.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                alimentosSelecionados.remove(deletePosition);
                adapter.notifyDataSetChanged();
                adapter.notifyDataSetInvalidated();
                AtualizaTexto();
            }
        });
        alert.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                dialog.dismiss();
            }
        });
        alert.show();

    }

    public void ConfirmaRegistro(View view){
        Registro registroNovo = new Registro();
        registroNovo.setHorarioRefeicao(HorarioRefeicaoNovo);
        registroNovo.setTotalConsumido(totalCaloria);
        registroNovo.setAlimentosRefeicao(alimentosSelecionados);

        BDRegistro bd = new BDRegistro(this);
        bd.inserirNovo(registroNovo);

        Intent inten = new Intent();

        Toast.makeText(this, "Registrado com sucesso", Toast.LENGTH_LONG).show();
        Intent it = new Intent(this, MainActivity.class);
        startActivity(it);
    }

    public void AtualizaTexto(){


        for (Alimento item : alimentosSelecionados ) {
            totalCaloria += item.getTotalRefeicao();
        }

        TextView txtTotalCaloria = (TextView)findViewById(R.id.txtTotal);
        txtTotalCaloria.setText("Total de Calorias na refeição: "+String.valueOf(totalCaloria));
        Log.i("verificador", "verificador");

    }

}
