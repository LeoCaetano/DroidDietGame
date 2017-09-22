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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.caetano.leonardo.bd.BDRefeicao;
import com.caetano.leonardo.dietgame.beans.Refeicao;

import java.util.Calendar;
import java.util.Date;

public class Relatorio extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Date DataInicio;
    private Date DataFim;
    private LinearLayout linData = null;
    private LinearLayout lnResumo = null;
    private Button btnPesquisa = null;
    private TextView txtTotalCalorias, txtMediaCalorias, txtMediaRefeicoes;
    int dia, mes, ano;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relatorio);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


    }

    protected void onResume(){
        super.onResume();
        linData = (LinearLayout)findViewById(R.id.lnData);
        btnPesquisa = (Button) findViewById(R.id.btnGeraRelatorio);

        linData.setVisibility(View.INVISIBLE);
        btnPesquisa.setVisibility(View.INVISIBLE);

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
        getMenuInflater().inflate(R.menu.relatorio, menu);
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
        } else if(id == R.id.MenuRelatorio){
            Intent it = new Intent(this, Relatorio.class);
            startActivity(it);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void selecionaCombo(View view){
        boolean checked = ((RadioButton) view).isChecked();

        linData = (LinearLayout)findViewById(R.id.lnData);
        btnPesquisa = (Button)findViewById(R.id.btnGeraRelatorio);

        switch (view.getId()){
            case R.id.radioData:
                if(checked){ habilitaData(view);}
                break;

            case R.id.radioMes:
                if(checked){habilitaMes(view);}
                break;

            case R.id.radioSemana:
                if(checked){habilitaSemana(view);}
                break;
        }
    }

    public void habilitaData(View view){
        linData.setVisibility(View.VISIBLE);
        btnPesquisa.setVisibility(view.VISIBLE);
    }

    public void habilitaMes(View view){
        linData.setVisibility(View.INVISIBLE);
        btnPesquisa.setVisibility(view.INVISIBLE);
    }

    public void habilitaSemana(View view){
        linData.setVisibility(View.INVISIBLE);
        btnPesquisa.setVisibility(view.INVISIBLE);
    }

    public void MudaDataInicio(View v){

        initDateTimeData();
        Calendar cDefault = Calendar.getInstance();
        cDefault.set(ano, mes, dia);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                dateListInicio,
                cDefault.get(Calendar.YEAR),
                cDefault.get(Calendar.MONTH),
                cDefault.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    public void MudaDataFim(View v){

        initDateTimeData();
        Calendar cDefault = Calendar.getInstance();
        cDefault.set(ano, mes, dia);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                dateListFim,
                cDefault.get(Calendar.YEAR),
                cDefault.get(Calendar.MONTH),
                cDefault.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void initDateTimeData(){
            Calendar c = Calendar.getInstance();
            ano = c.get(Calendar.YEAR);
            mes = c.get(Calendar.MONTH);
            dia = c.get(Calendar.DAY_OF_MONTH);
    }


    public void PesquisaRelatorio(View view){
        txtTotalCalorias = (TextView)findViewById(R.id.txtTotalCalorias);
        txtMediaCalorias = (TextView)findViewById(R.id.txtMediaCalorias);
        txtMediaRefeicoes = (TextView)findViewById(R.id.txtMediaRefeicoes);

        txtTotalCalorias.setText("Total de calorias consumidas no período: "+3500);
        txtMediaCalorias.setText("Média de calorias consumidas por dia no período: "+ 1000);
        txtMediaRefeicoes.setText("Média de refeições feitas por dia, durante o período: "+ 4);
    }

    DatePickerDialog.OnDateSetListener dateListInicio = new DatePickerDialog.OnDateSetListener() {
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
            DataInicio = calendar.getTime();
        }
    };

    DatePickerDialog.OnDateSetListener dateListFim = new DatePickerDialog.OnDateSetListener() {
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
            DataFim = calendar.getTime();
        }
    };

}
