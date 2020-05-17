package com.wheretogo.data.remote.responses;

import com.google.gson.annotations.SerializedName;
import com.wheretogo.models.onePlace.Place;

public class PlaceResponse extends DefaultResponse {
    @SerializedName("place")
    private Place place;

    public PlaceResponse(int code, String message) {
        super(code, message);
    }

    public PlaceResponse(int code) {
        super(code);
    }

    public Place getPlace() {
        return place;
    }
}
