package com.caetano.leonardo.dietgame;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
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

    int dia, mes, ano;

    /*private UiLifecycleHelper uiHelper;
    private FacebookDialog.Callback callback = new FacebookDialog.Callback(){
        @Override
        public void onComplete(PendingCall pendingCall, Bundle data) {
            boolean success = FacebookDialog.getNativeDialogDidComplete(data);
            Log.i("Script", "Success? "+success);
        }

        @Override
        public void onError(PendingCall pendingCall, Exception error, Bundle data) {
            Log.i("Script", "ERROR: "+error.toString());
        }
    };*/
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
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void carregaProgressBar(Date pData){

        //=========================construção das progress Bar=========================
        bdUsuario = new BDUsuario(this);
        bdRegistro = new BDRegistro(this);
        bdCalorias = new BDCalorias(this);
        pgbRefeicoes = (ProgressBar)findViewById(R.id.pgbHorarios);
        pgbCalorias = (ProgressBar)findViewById(R.id.pgbCalorias);

        txtRefeicoes =  (TextView)findViewById(R.id.txtQTDRefeicao);
        txtCalorias = (TextView)findViewById(R.id.txtQTDCalorias);

        int maxRefeicoes = bdUsuario.buscaQtdRefeicoes();
        int progressRefeicoes = bdRegistro.registrosFeitos(pData);

        pgbRefeicoes.setMax(maxRefeicoes);
        pgbRefeicoes.setProgress(progressRefeicoes);
        txtRefeicoes.setText("Refeições feitas hoje "+progressRefeicoes+" de "+maxRefeicoes);

        int maxCalorias = bdUsuario.buscaQtdCalorias();
        int progressCalorias = bdCalorias.buscar();

        pgbCalorias.setMax(maxCalorias);
        pgbCalorias.setProgress(progressCalorias);
        txtCalorias.setText("Calorias consumidas hoje "+progressCalorias+" de "+maxCalorias);

        Button btnData = (Button) findViewById(R.id.btnPesquisaData);

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        btnData.setText(df.format(pData));

    }

    public void MudaData(View v){
        final Calendar c = Calendar.getInstance();
        dia = c.get(Calendar.DAY_OF_MONTH);
        mes = c.get(Calendar.MONTH);
        dia = c.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                Date data= new Date();
                data.setDate(dayOfMonth);
                data.setMonth(monthOfYear);
                data.setYear(year);
                carregaProgressBar(data);
            }
        }, dia, mes, ano);

        datePickerDialog.show();

    }

}
