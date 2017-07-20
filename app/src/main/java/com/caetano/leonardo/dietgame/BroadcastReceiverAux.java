package com.caetano.leonardo.dietgame;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.LocalSocket;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.caetano.leonardo.dietgame.beans.HorarioRefeicao;
import com.caetano.leonardo.dietgame.beans.Refeicao;
import com.caetano.leonardo.dietgame.beans.Registro;

public class BroadcastReceiverAux extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {

		Log.i("Script", "-> Alarme");

		gerarNotificacao(context, intent);
	}


	public void gerarNotificacao(Context context, Intent intent){

		NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent, 0);

		Bundle bundle = intent.getExtras();
		HorarioRefeicao horaRef = (HorarioRefeicao) bundle.getSerializable("objeto");


		/*Intent it = new Intent(this, ListaRegistroHorarioActivity.class);
        startActivity(it);*/

		//Registrar intent
		Intent registrarIntent = new Intent(context, BuscaAlimento.class);
		registrarIntent.putExtra("horario", horaRef);
		registrarIntent.setAction("REGISTRAR");
		PendingIntent pRIntent = PendingIntent.getActivity(context, 0, registrarIntent, PendingIntent.FLAG_UPDATE_CURRENT);

		NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
		builder.setTicker("Nova Mensagem");
		builder.setContentTitle(horaRef.getRefeicao().getTitulo());
		builder.setContentText(horaRef.getRefeicao().getDescricao());
		builder.setSmallIcon(R.drawable.side_nav_bar);
		builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_menu_camera));
		builder.setContentIntent(pIntent);
		builder.addAction(R.drawable.ic_menu_send, "Registrar", pRIntent);
		builder.setVisibility(Notification.VISIBILITY_PUBLIC);

		Notification n = builder.build();
		n.vibrate = new long[]{150, 300, 150, 600};
		n.flags = Notification.DEFAULT_LIGHTS | Notification.FLAG_AUTO_CANCEL;

		nm.notify(R.drawable.ic_menu_manage, n);

		try{
			Uri som = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
			Ringtone toque = RingtoneManager.getRingtone(context, som);
			toque.play();
		}
		catch(Exception e){}



	}
}
