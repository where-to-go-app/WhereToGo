package com.wheretogo.data.remote;

import com.wheretogo.models.Place;

import java.util.List;

public interface PlacesAroundCallback {
    void onSuccess(List<Place> places);

    void onError(String error);
}
