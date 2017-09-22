package com.caetano.leonardo.dietgame;

import android.app.DatePickerDialog;
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
import android.view.MenuItem;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;

import com.caetano.leonardo.bd.BDHorarioRefeicao;
import com.caetano.leonardo.bd.BDRegistro;
import com.caetano.leonardo.dietgame.beans.HorarioRefeicao;
import com.caetano.leonardo.dietgame.beans.HorarioRefeicaoAdapter;
import com.caetano.leonardo.dietgame.beans.Registro;
import com.caetano.leonardo.dietgame.beans.RegistroAdapter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ListaRegistroHorarioActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    int dia, mes, ano;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_registro_horario);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Date data = new Date(System.currentTimeMillis());

        carregaRefeicoes(data);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.lista_registro_horario, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
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
        }else if(id == R.id.MenuRelatorio) {
            Intent it = new Intent(this, Relatorio.class);
            startActivity(it);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    protected void onResume(){
        super.onResume();
        Date data = new Date(System.currentTimeMillis());
        carregaRefeicoes(data);
    }

    public void MudaData(View v){
        initDateTimeData();
        Calendar cDefault = Calendar.getInstance();
        cDefault.set(ano, mes, dia);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                dateSetList,
                cDefault.get(Calendar.YEAR),
                cDefault.get(Calendar.MONTH),
                cDefault.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    public void carregaRefeicoes(Date data){

        BDRegistro bd = new BDRegistro(this);
        List<Registro> lista = null;
        try {
            lista = bd.buscaRefeicoesPorData(data);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        ArrayList<Registro> listaArray = new ArrayList<Registro>();
        listaArray.addAll(lista);

        Button btnData = (Button) findViewById(R.id.btnDataLista);
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        btnData.setText(df.format(data));

        if(lista.size() <= 0)
            return;

        ListView lv = (ListView) findViewById(R.id.lvRegistros);
        lv.setAdapter(new RegistroAdapter(this, listaArray));

        Log.i("carregaLista:","Fim");

    }

    private void initDateTimeData(){
        if( ano == 0 ){
            Calendar c = Calendar.getInstance();
            ano = c.get(Calendar.YEAR);
            mes = c.get(Calendar.MONTH);
            dia = c.get(Calendar.DAY_OF_MONTH);
        }
    }

    DatePickerDialog.OnDateSetListener dateSetList = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int pYear, int pMonthOfYear, int pDayOfMonth) {
            ano = pYear;
            mes = pMonthOfYear;
            dia = pDayOfMonth;

            Log.i("TesteAno",String.valueOf(ano));

            Calendar calendar = Calendar.getInstance();
            calendar.clear();
            calendar.set(Calendar.MONTH, mes);
            calendar.set(Calendar.YEAR, ano);
            calendar.set(Calendar.DAY_OF_MONTH,dia);
            Date date = calendar.getTime();
            carregaRefeicoes(date);
        }
    };

}
