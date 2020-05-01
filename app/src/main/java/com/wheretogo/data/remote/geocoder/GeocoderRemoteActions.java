package com.wheretogo.data.remote.geocoder;


import com.wheretogo.models.geocoderModel.GeocodeModel;
import retrofit2.Callback;

import static com.wheretogo.data.BuildVars.YA_GEOCODER_API_KEY;

public class GeocoderRemoteActions {
    GeocoderRemoteClient client;

    public GeocoderRemoteActions(GeocoderRemoteClient client) {
        this.client = client;
    }

    public void getGeocoding(String geocode, Callback<GeocodeModel> callback){
        client.getRemoteApi().getGocode(YA_GEOCODER_API_KEY, geocode, "json", 1).enqueue(callback);
    }
}