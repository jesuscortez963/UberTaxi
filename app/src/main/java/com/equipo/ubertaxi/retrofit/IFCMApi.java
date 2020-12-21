package com.equipo.ubertaxi.retrofit;

import com.equipo.ubertaxi.models.FCMBody;
import com.equipo.ubertaxi.models.FCMResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface IFCMApi {
    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAAYwwiL_U:APA91bGS0ROWBDnGaiOMtwrTTrXCVCAPbxLk0sBr0BhgDbNgzxpkl-OUJ4CetFtZU-7Wtp1iyZjKWmr8hwGjnIM41TIKIfkiNkZUU0eHzwOYDE9-5JU1yYa-h48yIAwYv1g7m2hCqAht"
    })
   @POST("fcm/send")
    Call<FCMResponse> send(@Body FCMBody body);
}
