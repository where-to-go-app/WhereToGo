package com.wheretogo.data.remote.responses;

import com.google.gson.annotations.SerializedName;
import com.wheretogo.models.Place;

import java.util.List;

public class PlacesResponse extends DefaultResponse{
    @SerializedName("places")
    private List<Place> places;

    public PlacesResponse(int code, String message) {
        super(code, message);
    }

    public PlacesResponse(int code) {
        super(code);
    }

    public List<Place> getPlaces() {
        return places;
    }
}
