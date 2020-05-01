package com.wheretogo.data.remote.geocoder;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wheretogo.data.remote.RemoteApi;
import com.wheretogo.data.remote.RemoteClient;
import com.wheretogo.data.remote.responses.DefaultResponse;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class GeocoderRemoteClient {
    private static final String REMOTE_URL = "https://geocode-maps.yandex.ru";
    private GeocoderRemoteApi remoteApi;
    private Executor executor = Executors.newSingleThreadExecutor();

    public GeocoderRemoteClient(){
        OkHttpClient okClient = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder()
                .client(okClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(REMOTE_URL)
                .build();
        remoteApi = retrofit.create(GeocoderRemoteApi.class);
    }

    GeocoderRemoteApi getRemoteApi() {
        return remoteApi;
    }


}