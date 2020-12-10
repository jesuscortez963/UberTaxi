package com.equipo.ubertaxi.activities.client;

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
import com.equipo.ubertaxi.activities.driver.MapDriverActivity;
import com.equipo.ubertaxi.activities.driver.RegisterDriverActivity;
import com.equipo.ubertaxi.includes.MyToolbar;
import com.equipo.ubertaxi.models.Client;
import com.equipo.ubertaxi.providers.AuthProvider;
import com.equipo.ubertaxi.providers.ClientProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import dmax.dialog.SpotsDialog;

public class RegisterActivity2 extends AppCompatActivity {



    AuthProvider mAuthProvider;
    ClientProvider mClientProvider;

    AlertDialog mDialog;

    //views
    Button mButtonRegister;
    TextInputEditText mTextInputEmail;
    TextInputEditText mTextInputName;
    TextInputEditText mTextInputPassword;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);
        mDialog = new SpotsDialog.Builder().setContext(RegisterActivity2.this).setMessage("espere un momento").build();

        MyToolbar.show(this,"Registrar cliente",true);

        mAuthProvider = new AuthProvider();
        mClientProvider = new ClientProvider();







        mButtonRegister = findViewById(R.id.btnRegistrar);
        mTextInputEmail = findViewById(R.id.textInputEmail);
        mTextInputName = findViewById(R.id.textInputName);
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

        if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty()){
            if (password.length() >=6 ){
                mDialog.show();
                register(name,email,password);
            }
            else {
                Toast.makeText(this, "la contraseña debe tener 6 caracteres", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(this, "Ingrese todos los campos", Toast.LENGTH_SHORT).show();
        }
    }
    void register(String name,String email, String password){
        mAuthProvider.register(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                mDialog.hide();
                if (task.isSuccessful()){
                    String id =FirebaseAuth.getInstance().getCurrentUser().getUid();
                    Client client = new Client(id,name,email);
                    create(client);
                }
                else {
                    Toast.makeText(RegisterActivity2.this, "No se pudo registrar el usuario", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    void create(Client client){
        mClientProvider.create(client).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    //Toast.makeText(RegisterActivity2.this, "El registro se realizo exitosamente", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity2.this, MapClientActivity.class);
                    intent .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(RegisterActivity2.this, "No se pudo crear el cliente", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    /*
    void saveUser(String id, String name, String email){
        String selectedUser = mPref.getString("user","");

        User user = new User();
        user.setEmail(email);
        user.setName(name);



        if (selectedUser.equals("driver")){
            mDatabase.child("Users").child("Drivers").child(id).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(RegisterActivity2.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(RegisterActivity2.this, "Fallo el registro ", Toast.LENGTH_SHORT).show();

                    }
                }

            });

        }
        else if (selectedUser.equals("client")){
            mDatabase.child("Users").child("Clients").child(id).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(RegisterActivity2.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(RegisterActivity2.this, "Fallo el registro ", Toast.LENGTH_SHORT).show();

                    }
                }
            });

        }

    }

     */
}