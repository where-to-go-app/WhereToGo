package com.wheretogo.data.remote.responses;

import com.google.gson.annotations.SerializedName;
import com.wheretogo.models.SimplePlace;

import java.util.List;

public class PlacesResponse extends DefaultResponse{
    @SerializedName("places")
    private List<SimplePlace> simplePlaces;

    public PlacesResponse(int code, String message) {
        super(code, message);
    }

    public PlacesResponse(int code) {
        super(code);
    }

    public List<SimplePlace> getSimplePlaces() {
        return simplePlaces;
    }
}
