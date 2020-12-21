package com.equipo.ubertaxi.providers;

import com.equipo.ubertaxi.models.FCMBody;
import com.equipo.ubertaxi.models.FCMResponse;
import com.equipo.ubertaxi.retrofit.IFCMApi;
import com.equipo.ubertaxi.retrofit.RetrofitClient;

import retrofit2.Call;

public class NotificationProvider {
    private String url= "https://fcm.googleapis.com";

    public NotificationProvider() {
    }

    public Call<FCMResponse> sendNotification (FCMBody body){
        return RetrofitClient.getClientObject(url).create(IFCMApi.class).send(body);

    }
}
