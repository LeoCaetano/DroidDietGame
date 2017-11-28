package com.caetano.leonardo.dietgame;

import android.app.*;
import android.content.Context;
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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.caetano.leonardo.bd.BDHorarioRefeicao;
import com.caetano.leonardo.bd.BDRefeicao;
import com.caetano.leonardo.bd.BDUsuario;
import com.caetano.leonardo.dietgame.beans.HorarioRefeicao;
import com.caetano.leonardo.dietgame.beans.Refeicao;

import java.util.Date;

public class NovoHorarioActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Spinner sp;
    private Refeicao ref = new Refeicao();
    private TimePicker horas;
    private HorarioRefeicao horaRef = new HorarioRefeicao();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_horario);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        horas = (TimePicker) findViewById(R.id.tmpHorario);
        horas.setIs24HourView(true);


        BDRefeicao bd = new BDRefeicao(this);

        ArrayAdapter<Refeicao> adapter = new ArrayAdapter<Refeicao>(this, android.R.layout.simple_spinner_dropdown_item, bd.buscar());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sp = (Spinner) findViewById(R.id.SPRefeicao);
        sp.setAdapter(adapter);

        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){


            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ref = (Refeicao) parent.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Intent intent = getIntent();
        if(intent != null){
            Bundle bundle = intent.getExtras();
            if(bundle != null){

                horaRef = (HorarioRefeicao) bundle.getSerializable("horarioRefeicao");

                Button btnSalvar = (Button) findViewById(R.id.btnNovo);
                btnSalvar.setText("Editar");
                horas.setCurrentHour(horaRef.getHorarioConsumo().getHours());
                horas.setCurrentMinute(horaRef.getHorarioConsumo().getMinutes());

                sp.setSelection(getIndex(sp, horaRef.getRefeicao().getTitulo()));
            }
        }


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
        getMenuInflater().inflate(R.menu.novo_horario, menu);
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


    public void criarHorario(View view){

        BDHorarioRefeicao bd = new BDHorarioRefeicao(this);
        BDUsuario bdUsu = new BDUsuario(this);
        HorarioRefeicao horaRefeicao;

        if(horaRef.getId() <= 0 || horaRef == null) {
            horaRefeicao = new HorarioRefeicao();
        }else{
            horaRefeicao = horaRef;
            horaRef.setRefeicao(ref);
        }

        Date dt = new Date();

        int HoraUm = horas.getCurrentHour();
        int MinUm = horas.getCurrentMinute();

        dt.setHours(HoraUm);
        dt.setMinutes(MinUm);

        horaRefeicao.setHorarioConsumo(dt);
        horaRefeicao.setRefeicao(ref);

        if(horaRefeicao.getId() <= 0) {
            Long idCad = bd.inserir(horaRefeicao);
            bdUsu.atualizarQtdRefeicoes(true);
            horaRefeicao.setId(Integer.valueOf(idCad.toString()));
        }else {
            bd.atualizar(horaRefeicao);
        }
        //importante
        AlarmManager alarme = (AlarmManager) getSystemService(ALARM_SERVICE);
        Context context = this;

        AlarmesRefeicao alarmesRefeicao = new AlarmesRefeicao(horaRefeicao,alarme,context);
        alarmesRefeicao.criarAlarme(true);

        Toast.makeText(this, "Cadastrado com sucesso", Toast.LENGTH_LONG).show();

    }

    private int getIndex(Spinner spinner, String myString)
    {
        int index = 0;

        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                index = i;
                break;
            }
        }
        return index;
    }

}
