package com.wheretogo.data.remote;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wheretogo.data.remote.responses.DefaultResponse;

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
    private static final String REMOTE_URL = "http://valer14356.pythonanywhere.com/";
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

    <T extends DefaultResponse> void performRequest(final Call<T> call, final Request<T> req) {
        executor.execute(() -> {
            try {
                Response<T> response = call.execute();
                if (response.isSuccessful()) {
                    req.onResult(response.body());
                } else {
                    req.onResult((T) new DefaultResponse(DefaultResponse.RESPONSE_UNKNOWN_ERROR));
                }
            } catch (IOException e) {
                req.onResult((T) new DefaultResponse(DefaultResponse.RESPONSE_UNKNOWN_ERROR));
            }
        });
    }

    public interface Request<T extends DefaultResponse> {
        void onResult(T response);
    }
}
