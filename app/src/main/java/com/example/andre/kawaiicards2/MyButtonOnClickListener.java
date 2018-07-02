package com.example.andre.kawaiicards2;

import android.view.View;

/**
 * Created by andre on 01/07/2018.
 */

public class MyButtonOnClickListener implements View.OnClickListener{

    int id;
    AnimeAdapter.AnimeViewHolder h;

    public MyButtonOnClickListener(int id, AnimeAdapter.AnimeViewHolder h) {
        this.id = id;
        this.h=h;
    }

    @Override
    public void onClick(View view) {

    }
}
