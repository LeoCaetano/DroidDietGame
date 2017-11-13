package com.caetano.leonardo.dietgame;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
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
import android.widget.Toast;

import com.caetano.leonardo.bd.BDCalorias;
import com.caetano.leonardo.bd.BDRegistro;
import com.caetano.leonardo.bd.BDUsuario;
import com.caetano.leonardo.dietgame.beans.HorarioRefeicao;
import com.caetano.leonardo.dietgame.beans.Refeicao;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareButton;
import com.facebook.share.widget.ShareDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ProgressBar pgbRefeicoes = null;
    private ProgressBar pgbCalorias = null;
    private TextView txtRefeicoes = null;
    private TextView txtCalorias = null;
    ImageView img = null;
    private BDUsuario bdUsuario;
    private BDRegistro bdRegistro;
    private BDCalorias bdCalorias;

    private FacebookSdk facebook;
    private SharedPreferences prefs;

    int maxRefeicoes;
    int progressRefeicoes;
    int maxCalorias;
    int progressCalorias;

    private Button compartilhamento;

    int dia, mes, ano;

    //face login
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    String name;

    //face share
    ShareDialog shareDialog;
    ShareButton shareButton;
    Bitmap image;
    LoginManager manager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
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

        //facebook login
        loginButton = (LoginButton)findViewById(R.id.fbLogin);
        loginButton.setReadPermissions(Arrays.asList("public_profile, email, user_birthday, user_friends"));
        callbackManager = CallbackManager.Factory.create();
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {
                                // Application code

                                try {
                                    // URL image_value = new URL("http://graph.facebook.com/"+id+"/picture" );
                                    name = object.getString("email");
                                    Log.i("email:", object.optString("email"));
                                    Log.i("nome", object.optString("name"));
                                    Log.i("id", object.optString("id"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                // getting email of the use
                                //info.setText(name);

                                Log.v("LoginActivity", response.toString());
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender, birthday");
                request.setParameters(parameters);
                request.executeAsync();

            }

            @Override
            public void onCancel() {

               //info.setText("Login attempt canceled.");

            }

            @Override
            public void onError(FacebookException e) {

               // info.setText("Login attempt failed.");

            }
        });

        //estacia share
        shareDialog = new ShareDialog(this);
        /*shareButton = (ShareButton)findViewById(R.id.btnCompartilha);
        image = BitmapFactory.decodeResource(getResources(), R.drawable.ouro);
        SharePhoto photo = new SharePhoto.Builder()
                .setBitmap(image)
                .setCaption("#Tutorialwing")
                .build();
        SharePhotoContent content = new SharePhotoContent.Builder()
                .addPhoto(photo)
                .build();
        shareButton.setShareContent(content);*/

        Button button = (Button) findViewById(R.id.btnCompartilha);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publishImage();
            }
        });

        //pega codigo
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.caetano.leonardo.dietgame",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

    }


    protected void onResume(){
        super.onResume();
        img = (ImageView)findViewById(R.id.imgTrodeuMain);

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


        if(totalFinal <= 30 ){
            img.setImageResource(R.drawable.bronze_grande);
            image = BitmapFactory.decodeResource(getResources(), R.drawable.bronze_grande);
        }
        else if(totalFinal > 30 && totalFinal <80) {
            img.setImageResource(R.drawable.prata_grande);
            image = BitmapFactory.decodeResource(getResources(), R.drawable.prata_grande);
        }
        else {
            img.setImageResource(R.drawable.ouro_grande);
            image = BitmapFactory.decodeResource(getResources(), R.drawable.ouro_grande);
        }
    }

    //SHARE
    private boolean reauth = false;
    private static final String KEY = "reauth";


    public boolean verifyPermissions(List<String> permissions, List<String> newPermissions){
        for(String p : permissions){
            if(newPermissions.contains(p)){
                return(true);
            }
        }
        return(false);
    }

    public void CompartilhaDieta(View view){


        callbackManager = CallbackManager.Factory.create();

        List<String> permissionNeeds = Arrays.asList("publish_actions");

        //this loginManager helps you eliminate adding a LoginButton to your UI
        manager = LoginManager.getInstance();

        manager.logInWithPublishPermissions(this, permissionNeeds);

        publishImage();
    }

    private void publishImage() {
        if (ShareDialog.canShow(SharePhotoContent.class)) {
            try {

                SharePhoto photo = new SharePhoto.Builder()
                        .setBitmap(image)
                        .build();

                if (ShareDialog.canShow(SharePhotoContent.class)) {
                    SharePhotoContent sharePhotoContent = new SharePhotoContent.Builder()
                            .addPhoto(photo)
                            .build();

                    shareDialog.show(sharePhotoContent);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
