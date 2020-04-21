package com.wheretogo.data.remote;

import android.graphics.PointF;

import com.wheretogo.models.Place;

import java.util.List;

public class RemoteActions {
    private RemoteClient client;

    public RemoteActions(RemoteClient client) {
        this.client = client;
    }

    public void getPlacesAround(PointF location, PlacesCallback callback) {
        List<Place> places = client.performRequest(client.getRemoteApi().placesAround());
        callback.onSuccess();

    }
}
