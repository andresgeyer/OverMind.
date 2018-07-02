package com.example.andre.kawaiicards2;

import android.content.Context;
import android.content.Intent;
import android.provider.CalendarContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private AnimeAdapter mAdapter;

    private static final String TAG = MainActivity.class.getSimpleName();
    public static final int WORD_EDIT = 1;
    public static final int WORD_ADD = -1;
    private AnimeOpenHelper mDB;
    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        runOnUiThread(new Runnable() {
            @Override public void run() {
                mDB = new AnimeOpenHelper(context);
                // Create recycler view.
                mRecyclerView = (RecyclerView) findViewById(R.id.reciclador);
                // Create an mAdapter and supply the data to be displayed.
                mAdapter = new AnimeAdapter(context, /* mDB.getAllEntries(),*/ mDB);
                // Connect the mAdapter with the recycler view.
                mRecyclerView.setAdapter(mAdapter);
                // Give the recycler view a default layout manager.
                mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            }
        });



    }

    public boolean onOptionsItemSelected(MenuItem item) {

        boolean interfaz = true;

        /*new Thread(new Runnable() {
            public void run() {
            }
        }).start();*/


        // Handle app bar item clicks here. The app bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.agregar:
                runOnUiThread(new Runnable() {
                    @Override public void run() {
                        mDB.insert("Tarea nueva", "Descripción", "1/1/2010", "8:00", "no");
                        mAdapter.notifyDataSetChanged();
                    }
                });
                displayToast(getString(R.string.mensaje_tarea_creada));
                return true;
            case R.id.action_todos:
                runOnUiThread(new Runnable() {
                    @Override public void run() {
                        mAdapter = new AnimeAdapter(context, /* mDB.getAllEntries(),*/ mDB);
                        // Connect the mAdapter with the recycler view.
                        mRecyclerView.setAdapter(mAdapter);
                    }
                });
                displayToast(getString(R.string.mensaje_todas_tareas));
                return true;
            case R.id.action_por_hacer:
                mAdapter.notifyItemRemoved(0);
                return true;
            default:
                // Do nothing
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void displayToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

}
