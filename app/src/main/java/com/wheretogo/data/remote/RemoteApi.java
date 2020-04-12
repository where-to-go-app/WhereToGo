package com.wheretogo.data.remote;

import com.wheretogo.models.Place;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RemoteApi {
    @GET
    Call<List<Place>> placesAround();
}
