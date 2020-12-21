package com.equipo.ubertaxi.activities.client;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.equipo.ubertaxi.R;
import com.equipo.ubertaxi.models.FCMBody;
import com.equipo.ubertaxi.models.FCMResponse;
import com.equipo.ubertaxi.providers.GeofireProvider;
import com.equipo.ubertaxi.providers.NotificationProvider;
import com.equipo.ubertaxi.providers.TokenProvider;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestDriverActivity extends AppCompatActivity {

    private LottieAnimationView mAnimation;
    private TextView mTextViewLookingFor;
    private Button mButtonCancelRequest;
    private GeofireProvider mGeofireProvider;

    private double mExtraOriginLat;
    private double mExtraOriginLng;
    private LatLng mOriginLatLng;

    private double mRadius=0.1;
    private boolean mDriverFound = false;
    private String mIdDriverFound = "";
    private LatLng mDriverFoundLatLng;

    private NotificationProvider mNotificationProvider;
    private TokenProvider mTokenProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_driver);

        mAnimation = findViewById(R.id.animation);
        mTextViewLookingFor= findViewById(R.id.textViewLookingFor);
        mButtonCancelRequest = findViewById(R.id.btnCancelRequest);

        mAnimation.playAnimation();

        mExtraOriginLat = getIntent().getDoubleExtra("origin_lat",0);
        mExtraOriginLng = getIntent().getDoubleExtra("origin_lng",0);
        mOriginLatLng = new LatLng(mExtraOriginLat,mExtraOriginLng);


        mGeofireProvider = new GeofireProvider();
        mNotificationProvider= new NotificationProvider();
        mTokenProvider = new TokenProvider();

        getClosestDriver();

    }
     private void getClosestDriver(){
        mGeofireProvider.getActiveDrivers(mOriginLatLng,mRadius).addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {

                if (!mDriverFound){
                    mDriverFound=true;
                    mIdDriverFound=key;
                    mDriverFoundLatLng=new LatLng(location.latitude,location.longitude);
                    mTextViewLookingFor.setText("CONDUCTOR ENCONTRADO\nESPERANDO RESPUESTA");
                    sendNotification();
                    Log.d("DIRVER","ID" + mIdDriverFound);
                }

            }

            @Override
            public void onKeyExited(String key) {

            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {

            }

            @Override
            public void onGeoQueryReady() {
                //INGRESAR CUANDO TERMINA LA BUSQUEDA DEL CONDUCTOR EN UN RADIO DE 0.1 KM
                if (!mDriverFound){
                    mRadius = mRadius + 0.1f;
                    //NO ENCONTRO UN CONDUCTOR
                    if (mRadius > 10){
                        mTextViewLookingFor.setText("NO SE ENCONTRO UN CONDUCTOR");
                        Toast.makeText(RequestDriverActivity.this, "NO SE ENCONTRO UN CONDUCTOR", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    else{
                        getClosestDriver();
                    }
                }
            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });

     }

    private void sendNotification() {
        mTokenProvider.getToken(mIdDriverFound).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String token=snapshot.child("token").getValue().toString();
                    Map<String,String> map =new HashMap<>();
                    map.put("title","SOLICITUD DE SERVICIO");
                    map.put("body","Un cliente esta solicitando el servicio");
                    FCMBody fcmBody = new FCMBody(token,"high",map);
                    mNotificationProvider.sendNotification(fcmBody).enqueue(new Callback<FCMResponse>() {
                        @Override
                        public void onResponse(Call<FCMResponse> call, Response<FCMResponse> response) {
                            if (response.body() != null){
                                if (response.body().getSuccess() == 1){
                                    Toast.makeText(RequestDriverActivity.this, "La notificacion se ha enviado correctamente", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(RequestDriverActivity.this, "No se pudo enviar la notificación", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else {
                                Toast.makeText(RequestDriverActivity.this, "No se pudo enviar la notificación", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<FCMResponse> call, Throwable t) {
                            Log.d("Error","Error "+ t.getMessage());
                        }
                    });
                }
                else {
                    Toast.makeText(RequestDriverActivity.this, "No se pudo enviar la notificacion no tiene un token de sesion", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}