package com.wheretogo.data.remote;

import android.graphics.PointF;

import com.wheretogo.models.Place;

import java.util.List;

public class RemoteActions {
    private RemoteClient client;

    public RemoteActions(RemoteClient client) {
        this.client = client;
    }

    public void getPlacesAround(PointF location, PlacesAroundCallback callback) {
        client.enqueue(client.getRemoteApi().placesAround(), new RemoteClient.Request<List<Place>>() {
            @Override
            public void onSuccess(List<Place> places) {
                if (callback != null) {
                    callback.onSuccess(places);
                }
            }

            @Override
            public void onFailure(String error) {
                if (callback != null) {
                    callback.onError(error);
                }
            }
        });
    }
}
