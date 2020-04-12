package com.wheretogo.data.remote;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RemoteClient {
    private static final String REMOTE_URL = "";
    private RemoteApi remoteApi;
    private Executor executor = Executors.newSingleThreadExecutor();

    public RemoteClient() {
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
        remoteApi = retrofit.create(RemoteApi.class);
    }

    RemoteApi getRemoteApi() {
        return remoteApi;
    }

    <T> void enqueue(Call<T> call, Request<T> request) {
        executor.execute(() -> {
            try {
                Response response = call.execute();
                if (response.isSuccessful()) {
                    request.onSuccess((T) response.body());
                } else {
                    request.onFailure(String.valueOf(response.code()));
                }
            } catch (IOException e) {
                request.onFailure(e.getMessage());
            }
        });
    }

    interface Request<T> {
        void onSuccess(T data);

        void onFailure(String error);
    }
}
