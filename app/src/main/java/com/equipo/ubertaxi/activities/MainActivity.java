package com.equipo.ubertaxi.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.equipo.ubertaxi.R;
import com.equipo.ubertaxi.activities.client.MapClientActivity;
import com.equipo.ubertaxi.activities.driver.MapDriverActivity;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    Button mButtonIAmDriver;
    Button mButtonIAmClient;

    SharedPreferences mPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPref = getApplicationContext().getSharedPreferences("typeUser", MODE_PRIVATE);

        SharedPreferences.Editor editor = mPref.edit();

        mButtonIAmDriver=findViewById(R.id.btnIAmDriver);
        mButtonIAmClient=findViewById(R.id.btnIAmClient);

        mButtonIAmClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("user","client");
                editor.apply();
                
                goToSelectAuth();
            }
        });

        mButtonIAmDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("user","driver");
                editor.apply();

                goToSelectAuth();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() != null){
            String user = mPref.getString("user","");
            if (user.equals("client")){
                Intent intent = new Intent(MainActivity.this, MapClientActivity.class);
                intent .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            }
            else{
                Intent intent = new Intent(MainActivity.this, MapDriverActivity.class);
                intent .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            }
        }
    }

    private void goToSelectAuth() {
        Intent intent = new Intent(MainActivity.this, SelectOptionAuthActivity.class);
        startActivity(intent);
    }
}