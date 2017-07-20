package com.caetano.leonardo.dietgame;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.caetano.leonardo.bd.BDCalorias;
import com.caetano.leonardo.dietgame.beans.Alimento;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class registro_alimento extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Alimento novoAlimento = new Alimento();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_alimento);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ArrayAdapter<Alimento> adapter = new ArrayAdapter<Alimento>(this, android.R.layout.simple_spinner_dropdown_item, buscaAlimento());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner sp;
        sp = (Spinner) findViewById(R.id.SPAlimento);
        sp.setAdapter(adapter);

        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                novoAlimento = (Alimento) parent.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    public List<Alimento> buscaAlimento(){
        List<Alimento> arrAlimento = new ArrayList<Alimento>();

        return arrAlimento;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.registro_alimento, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void registrarCaloria(View view){

        BDCalorias bd = new BDCalorias(this);
        Date dt = new Date();

        TextView txtQtdConsumida =  (TextView)findViewById(R.id.txtQtd);
        TextView txtPeso = (TextView)findViewById(R.id.txtPeso);

        int QtdConsumida = Integer.parseInt(txtQtdConsumida.getText().toString());
        int Peso = Integer.parseInt(txtPeso.getText().toString());


        double total = ((Peso * QtdConsumida) * novoAlimento.getQtdCaloria())/100;

        bd.inserir(novoAlimento, total);

        Toast.makeText(this, "Registrado com sucesso", Toast.LENGTH_LONG).show();

        Log.i("IDCadastrado",String.valueOf(total));



    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.MenuNovoAlarme) {
            Intent it = new Intent(this, NovoHorarioActivity.class);
            startActivity(it);
        } else if (id == R.id.MenuRefeicoesProgramadas) {
            Intent it = new Intent(this, ListaHorarioRefeicoesActivity.class);
            startActivity(it);
        } else if (id == R.id.MenuRegistrosDoDia) {
            Intent it = new Intent(this, ListaRegistroHorarioActivity.class);
            startActivity(it);
        } else if(id == R.id.MenuHome){
            Intent it = new Intent(this, MainActivity.class);
            startActivity(it);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
