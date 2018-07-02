package com.example.andre.kawaiicards2;

import android.content.Context;
import android.content.Intent;
import android.provider.CalendarContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static android.content.ContentValues.TAG;

/**
 * Created by andre on 02/07/2018.
 */

public class Calendario extends AppCompatActivity {

    AnimeAdapter.AnimeViewHolder h;
    Context context;

    public Calendario(Context context, AnimeAdapter.AnimeViewHolder h){
        this.h=h;
        this.context=context;
        fecha();
    }

    public void fecha(){
        Intent calIntent = new Intent(Intent.ACTION_INSERT);
        calIntent.setType("vnd.android.cursor.item/event");
        calIntent.putExtra(CalendarContract.Events.TITLE, h.nombre.getText().toString());
        calIntent.putExtra(CalendarContract.Events.EVENT_LOCATION, "");
        calIntent.putExtra(CalendarContract.Events.DESCRIPTION, h.visitas.getText().toString());

        GregorianCalendar calDate = new GregorianCalendar(Integer.parseInt(h.fecha.getText().toString().split("/")[2]),
                Integer.parseInt(h.fecha.getText().toString().split("/")[1]),
                Integer.parseInt(h.fecha.getText().toString().split("/")[0]));
        calIntent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true);
        calIntent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,
                calDate.getTimeInMillis());
        calIntent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,
                calDate.getTimeInMillis());

        startActivity(calIntent);
    }

    public void Agregar() {
        Calendar cal = Calendar.getInstance();
        Intent intent = null;
            try {
                cal.set(Calendar.YEAR, Integer.parseInt(h.fecha.getText().toString().split("/")[2]));                 //
                cal.set(Calendar.MONTH, Integer.parseInt(h.fecha.getText().toString().split("/")[1]));   // Set de AÑO MES y Dia
                cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(h.fecha.getText().toString().split("/")[0]));       //
                cal.set(Calendar.HOUR_OF_DAY, 8);// Set de HORA y MINUTO
                cal.set(Calendar.MINUTE, 0);            //
                intent = new Intent(Intent.ACTION_EDIT);
                intent.setType("vnd.android.cursor.item/event");

                intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, cal.getTimeInMillis());
                intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, cal.getTimeInMillis() + 60 * 60 * 1000);

                intent.putExtra(CalendarContract.Events.ALL_DAY, false);
                intent.putExtra(CalendarContract.Events.TITLE, h.nombre.getText().toString());
                intent.putExtra(CalendarContract.Events.DESCRIPTION, h.visitas.getText().toString());
                intent.putExtra(CalendarContract.Events.EVENT_LOCATION, "");
                startActivity(intent);
            } catch (Exception e) {
                Log.d(TAG, "ERROR CALENDAR: " + e);
            }
    }

}
