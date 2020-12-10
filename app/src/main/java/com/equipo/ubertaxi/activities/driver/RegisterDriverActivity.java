package com.equipo.ubertaxi.activities.driver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.equipo.ubertaxi.R;
import com.equipo.ubertaxi.activities.client.RegisterActivity2;
import com.equipo.ubertaxi.includes.MyToolbar;
import com.equipo.ubertaxi.models.Client;
import com.equipo.ubertaxi.models.Driver;
import com.equipo.ubertaxi.providers.AuthProvider;
import com.equipo.ubertaxi.providers.ClientProvider;
import com.equipo.ubertaxi.providers.DriverProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import dmax.dialog.SpotsDialog;

public class RegisterDriverActivity extends AppCompatActivity {



    AuthProvider mAuthProvider;
    DriverProvider mDriverProvider;

    AlertDialog mDialog;

    //views
    Button mButtonRegister;
    TextInputEditText mTextInputEmail;
    TextInputEditText mTextInputName;
    TextInputEditText mTextInputVehicleBrand;
    TextInputEditText mTextInputVehiclePlate;
    TextInputEditText mTextInputCirculationCard;
    TextInputEditText mTextInputOfficialIdentification;
    TextInputEditText mTextInputPassword;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_driver);
        MyToolbar.show(RegisterDriverActivity.this,"Registro conductor", true);
        mDialog = new SpotsDialog.Builder().setContext(RegisterDriverActivity.this).setMessage("espere un momento").build();



        mAuthProvider = new AuthProvider();
        mDriverProvider = new DriverProvider();







        mButtonRegister = findViewById(R.id.btnRegistrar);
        mTextInputEmail = findViewById(R.id.textInputEmail);
        mTextInputName = findViewById(R.id.textInputName);
        mTextInputVehicleBrand = findViewById(R.id.textInputVehicleBrand);
        mTextInputVehiclePlate = findViewById(R.id.textInputVehiclePlate);
        mTextInputCirculationCard = findViewById(R.id.textInputCirculationCard);
        mTextInputOfficialIdentification= findViewById(R.id.textInputOfficialIdentification);
        mTextInputPassword = findViewById(R.id.textInputPassword);



        mButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickRegister();
            }
        });
    }
    void clickRegister(){
        String name = mTextInputName.getText().toString();
        String email = mTextInputEmail.getText().toString();
        String password = mTextInputPassword.getText().toString();
        String vehicleBrand =mTextInputVehicleBrand.getText().toString();
        String vehiclePlate =mTextInputVehiclePlate.getText().toString();
        String circulationCard =mTextInputCirculationCard.getText().toString();
        String officialIdentification = mTextInputOfficialIdentification.getText().toString();


        if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty() && !vehicleBrand.isEmpty() && !vehiclePlate.isEmpty() && !circulationCard.isEmpty() && !officialIdentification.isEmpty()){
            if (password.length() >=6 ){
                mDialog.show();
                register(name,email,password,vehicleBrand,vehiclePlate,circulationCard,officialIdentification);
            }
            else {
                Toast.makeText(RegisterDriverActivity.this, "la contraseña debe tener 6 caracteres", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(RegisterDriverActivity.this, "Ingrese todos los campos", Toast.LENGTH_SHORT).show();
        }
    }
    void register(String name,String email, String password,String vehicleBrand,String vehiclePlate,String circulationCard,String officialIdentification){
        mAuthProvider.register(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                mDialog.hide();
                if (task.isSuccessful()){
                    String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    Driver driver = new Driver(id,name,email,vehicleBrand,vehiclePlate,circulationCard,officialIdentification);
                    create(driver);
                }
                else {
                    Toast.makeText(RegisterDriverActivity.this, "No se pudo registrar el usuario", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    void create(Driver driver){
        mDriverProvider.create(driver).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    //Toast.makeText(RegisterDriverActivity.this, "El registro se realizo exitosamente", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterDriverActivity.this,MapDriverActivity.class);
                    intent .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(RegisterDriverActivity.this, "No se pudo crear el cliente", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}