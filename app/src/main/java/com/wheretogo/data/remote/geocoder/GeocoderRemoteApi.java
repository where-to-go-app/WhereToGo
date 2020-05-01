package com.wheretogo.data.remote.geocoder;

import com.wheretogo.data.remote.responses.PlaceResponse;
import com.wheretogo.models.geocoderModel.GeocodeModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GeocoderRemoteApi {
    @GET("/1.x/")
    Call<GeocodeModel> getGocode(
            @Query("apikey") String key,
            @Query("geocode") String geocode,
            @Query("format") String format,
            @Query("results") Integer limit
    );
}