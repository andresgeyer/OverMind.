package com.example.andre.kawaiicards2;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.provider.CalendarContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import static android.R.attr.button;
import static android.content.ContentValues.TAG;

/**
 * Created by andre on 27/06/2018.
 */

public class AnimeAdapter extends RecyclerView.Adapter<AnimeAdapter.AnimeViewHolder> {

    public static class AnimeViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        public ImageView imagen;
        public TextView nombre;
        public TextView visitas;
        public TextView fecha;
        public Button borrar;
        public Button editar;
        public CheckBox listo;

        public AnimeViewHolder(View v) {
            super(v);
            imagen = (ImageView) v.findViewById(R.id.imagen);
            nombre = (TextView) v.findViewById(R.id.nombre);
            visitas = (TextView) v.findViewById(R.id.visitas);
            fecha = (TextView) v.findViewById(R.id.editText2);
            borrar = (Button) v.findViewById(R.id.botonBorrar);
            editar = (Button) v.findViewById(R.id.botonGuardar);
            listo = (CheckBox) v.findViewById(R.id.checkBoxListo);
        }
    }

    private final LayoutInflater mInflater;
    public AnimeOpenHelper mDB;
    public Context mContext;
    private DatePickerDialog.OnDateSetListener mDateSetListener;


    public AnimeAdapter(Context context, AnimeOpenHelper db) {
        mInflater = LayoutInflater.from(context);
        mContext = context;
        mDB = db;
    }

    @Override
    public AnimeViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.anime_card, viewGroup, false);
        return new AnimeViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final AnimeViewHolder viewHolder, int i) {
        Anime current = mDB.query(i);
        viewHolder.nombre.setText(current.getTarea());
        viewHolder.visitas.setText(current.getDescripcion());
        viewHolder.fecha.setText(current.getFecha());
        Log.d(TAG, "terminado " + current.getTerminado().toString());
        if (current.getTerminado().toString().equals("no"))
            viewHolder.imagen.setImageResource(R.drawable.b);
        else {
            viewHolder.imagen.setImageResource(R.drawable.a);
            viewHolder.listo.setChecked(true);
        }
        AnimeViewHolder h = viewHolder; // needs to be final for use in callback

        viewHolder.borrar.setOnClickListener(new MyButtonOnClickListener(
                current.getId(), h) {
            @Override
            public void onClick(View v) {
                // You have to get the position like this, you can't hold a reference
                int deleted = mDB.delete(id);
                if (deleted >= 0)
                    notifyItemRemoved(h.getAdapterPosition());
            }
        });


        viewHolder.editar.setOnClickListener(new MyButtonOnClickListener(
                current.getId(), h) {
            @Override
            public void onClick(View v) {
                String hecho = "no";
                if (h.listo.isChecked())
                    hecho = "si";
                mDB.update(id, h.nombre.getText().toString(), h.visitas.getText().toString(), h.fecha.getText().toString(), "8:00", hecho);
                //new Calendario(mContext, h);
            }
        });

        viewHolder.listo.setOnClickListener(new MyButtonOnClickListener(
                current.getId(), h) {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                System.out.println("Current time => " + c.getTime());
                SimpleDateFormat df = new SimpleDateFormat("dd-mm-yyyy");
                String fecha = df.format(c.getTime());
                df = new SimpleDateFormat("hh:mm");
                String hora = df.format(c.getTime());
                mDB.update(id, h.nombre.getText().toString(), h.visitas.getText().toString(), fecha, hora, "si");
                viewHolder.fecha.setText(fecha);
                viewHolder.imagen.setImageResource(R.drawable.a);
            }
        });

        viewHolder.fecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(
                        mContext,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);
                String date = day + "/" + month + "/" + year;
                viewHolder.fecha.setText(date);
            }
        };
    }

    @Override
    public int getItemCount() {
        return (int) mDB.count();
    }

}