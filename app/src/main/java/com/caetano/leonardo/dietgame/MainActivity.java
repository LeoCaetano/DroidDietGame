package com.caetano.leonardo.dietgame;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.caetano.leonardo.bd.BDCalorias;
import com.caetano.leonardo.bd.BDRegistro;
import com.caetano.leonardo.bd.BDUsuario;
import com.caetano.leonardo.dietgame.beans.HorarioRefeicao;
import com.caetano.leonardo.dietgame.beans.Refeicao;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ProgressBar pgbRefeicoes = null;
    private ProgressBar pgbCalorias = null;
    private TextView txtRefeicoes = null;
    private TextView txtCalorias = null;
    private BDUsuario bdUsuario;
    private BDRegistro bdRegistro;
    private BDCalorias bdCalorias;

    int maxRefeicoes;
    int progressRefeicoes;
    int maxCalorias;
    int progressCalorias;

    int dia, mes, ano;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

        Date data = new Date(System.currentTimeMillis());
        carregaProgressBar(data);
        calculaMedalha();
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
        getMenuInflater().inflate(R.menu.main, menu);
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
        }else if(id == R.id.MenuRelatorio) {
            Intent it = new Intent(this, Relatorio.class);
            startActivity(it);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void carregaProgressBar(Date pData){

        Log.i("Teste",pData.toString());

        //=========================construção das progress Bar=========================
        bdUsuario = new BDUsuario(this);
        bdRegistro = new BDRegistro(this);
        bdCalorias = new BDCalorias(this);

        //declara progressBar
        pgbRefeicoes = (ProgressBar)findViewById(R.id.pgbHorarios);
        pgbCalorias = (ProgressBar)findViewById(R.id.pgbCalorias);

        //declara TXTs
        txtRefeicoes =  (TextView)findViewById(R.id.txtQTDRefeicao);
        txtCalorias = (TextView)findViewById(R.id.txtQTDCalorias);

        //preenche pgb refeições
        maxRefeicoes = bdUsuario.buscaQtdRefeicoes();
        progressRefeicoes = bdRegistro.registrosFeitos(pData);
        pgbRefeicoes.setMax(maxRefeicoes);
        pgbRefeicoes.setProgress(progressRefeicoes);
        txtRefeicoes.setText("Refeições feitas hoje "+progressRefeicoes+" de "+maxRefeicoes);

        //preenche pgb refeições
        maxCalorias = bdUsuario.buscaQtdCalorias();
        progressCalorias = bdCalorias.buscaPorData(pData);
        pgbCalorias.setMax(maxCalorias);
        pgbCalorias.setProgress(progressCalorias);
        txtCalorias.setText("Calorias consumidas hoje "+progressCalorias+" de "+maxCalorias);

        Button btnData = (Button) findViewById(R.id.btnPesquisaData);
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        btnData.setText(df.format(pData));
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

    public void calculaMedalha(){

        double rCaloria;
        double rRefeicoes;
        double totalFinal;

        rCaloria = (progressCalorias * 100/ maxCalorias);
        rRefeicoes = (progressRefeicoes * 100/ maxRefeicoes);

        totalFinal = (rCaloria + rRefeicoes) / 2;
        ImageView img = (ImageView)findViewById(R.id.imgTrodeuMain);

        if(totalFinal <= 30 )
            img.setImageResource(R.drawable.bronze_grande);
        else if(totalFinal > 30 && totalFinal <80)
            img.setImageResource(R.drawable.prata_grande);
        else
            img.setImageResource(R.drawable.ouro_grande);
    }

    public void ChamaAlgumaCoisa(View view){
        Intent it = new Intent(this, BuscaAlimento.class);
        startActivity(it);
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
            carregaProgressBar(date);
        }
    };
}
