package com.equipo.ubertaxi.includes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.equipo.ubertaxi.R;

public class MyToolbar {

    public static void show(AppCompatActivity activity,String title,boolean upButton){
       Toolbar toolbar= activity.findViewById(R.id.toolbar);
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setTitle(title);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(upButton);

    }
}
