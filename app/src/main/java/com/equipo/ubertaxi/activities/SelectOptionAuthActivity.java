package com.equipo.ubertaxi.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.equipo.ubertaxi.R;
import com.equipo.ubertaxi.activities.client.RegisterActivity2;
import com.equipo.ubertaxi.activities.driver.RegisterDriverActivity;
import com.equipo.ubertaxi.includes.MyToolbar;

public class SelectOptionAuthActivity extends AppCompatActivity {


    Button mButtonGoToLogin;
    Button mButtonGoToRegister;
    SharedPreferences mPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_option_auth);

        MyToolbar.show(this,"selecionar opcion",true);





        mButtonGoToLogin=findViewById(R.id.btnGoToLogin);
        mButtonGoToRegister=findViewById(R.id.btnGoToRegister);

        mButtonGoToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToLogin();

            }
        });
        mButtonGoToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToRegister();
            }
        });

        mPref = getApplicationContext().getSharedPreferences("typeUser",MODE_PRIVATE);


    }


    public void goToLogin(){
        Intent intent = new Intent(SelectOptionAuthActivity.this, LoginActivity.class);
        startActivity(intent);
    }
    public void goToRegister(){
        String typeUser = mPref.getString("user","");
        if (typeUser.equals("client")){
            Intent intent = new Intent(SelectOptionAuthActivity.this, RegisterActivity2.class);
            startActivity(intent);
        }
        else{
            Intent intent = new Intent(SelectOptionAuthActivity.this, RegisterDriverActivity.class);
            startActivity(intent);
        }
    }
}