package com.wheretogo.data.remote;

import android.graphics.RectF;

import com.wheretogo.data.remote.responses.DefaultResponse;
import com.wheretogo.models.Place;
import com.wheretogo.models.User;

import java.util.List;

import static com.wheretogo.data.BuildVars.SECRET_STRING;

public class RemoteActions {
    private RemoteClient client;

    public RemoteActions(RemoteClient client) {
        this.client = client;
    }

    public void auth(User user, DefaultCallback<String> callback) {
        if (user.getFirstName() == null) {
            throw new IllegalArgumentException();
        }
        if (user.getLastName() == null) {
            throw new IllegalArgumentException();
        }
        client.performRequest(client.getRemoteApi().authUser(SECRET_STRING, user.getClientId(), user.getFirstName(), user.getLastName()),
                response -> {
                    if (response.getCode() != DefaultResponse.RESPONSE_OK) {
                        callback.onError(response.getCode());
                        return;
                    }
                    callback.onSuccess(response.getMessage());
                });
    }

    public void createPlace(Place place, User user, DefaultCallback<String> callback) {
        client.performRequest(
                client.getRemoteApi().createPlace(
                        user.getToken(),
                        place.getPlaceName(),
                        place.getPlaceDesc(),
                        place.getLatitude(),
                        place.getLongitude(),
                        place.getAddress()),
                response -> {
                    if (response.getCode() != DefaultResponse.RESPONSE_OK) {
                        callback.onError(response.getCode());
                        return;
                    }
                    callback.onSuccess(response.getMessage());
                });
    }

    public void getPlacesAround(User user, RectF rect, DefaultCallback<List<Place>> callback) {
        client.performRequest(client.getRemoteApi().getPlacesAround(user.getToken(), rect.left, rect.top, rect.right, rect.bottom),
                response -> {
                    if (response.getCode() != DefaultResponse.RESPONSE_OK) {
                        callback.onError(response.getCode());
                        return;
                    }
                    callback.onSuccess(response.getPlaces());
                });
    }
}
