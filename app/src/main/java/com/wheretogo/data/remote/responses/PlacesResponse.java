package com.wheretogo.data.remote.responses;

import com.google.gson.annotations.SerializedName;
import com.wheretogo.models.Place;

import java.util.List;

public class PlacesResponse {
    @SerializedName("places")
    private List<Place> places;

    public List<Place> getPlaces() {
        return places;
    }
}
