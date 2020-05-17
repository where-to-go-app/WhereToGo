package com.wheretogo.data.remote;

import android.graphics.RectF;

import com.wheretogo.data.remote.responses.DefaultResponse;
import com.wheretogo.models.CreatingPlace;
import com.wheretogo.models.SimplePlace;
import com.wheretogo.models.User;
import com.wheretogo.models.onePlace.Place;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

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
                    String token = response.getMessage();
                    if (token == null) {
                        callback.onError(0);
                        return;
                    }
                    callback.onSuccess(token);
                });
    }
    public void getPlaceById(User user, int id, DefaultCallback<Place> callback) {
        client.performRequest(client.getRemoteApi().getPlaceById(id, user.getToken()),
                response -> {
                    if (response.getCode() != DefaultResponse.RESPONSE_OK) {
                        callback.onError(response.getCode());
                        return;
                    }
                    callback.onSuccess(response.getPlace());
                });
    }
    public void createPlace(byte[] photo, CreatingPlace simplePlace, User user, DefaultCallback<Boolean> callback) {
        Map<String, RequestBody> map = new HashMap<>();
        map.put("user_token", toRequestBody(user.getToken()));
        map.put("place_name", toRequestBody(simplePlace.getPlaceName()));
        map.put("place_desc", toRequestBody(simplePlace.getPlaceDesc()));
        map.put("latitude", toRequestBody(String.valueOf(simplePlace.getLatitude())));
        map.put("longitude", toRequestBody(String.valueOf(simplePlace.getLongitude())));
        map.put("country", toRequestBody(simplePlace.getCountry()));
        map.put("address", toRequestBody(simplePlace.getAddress()));
        client.performRequest(
                client.getRemoteApi().createPlace(
                        MultipartBody.Part.createFormData(simplePlace.getPlaceName(), simplePlace.getPlaceName(), RequestBody.create(MediaType.parse("image/*"), photo)),
                        map),
                response -> {
                    if (response.getCode() != DefaultResponse.RESPONSE_OK) {
                        callback.onError(response.getCode());
                        return;
                    }
                    callback.onSuccess(response.getCode() == DefaultResponse.RESPONSE_OK);
                });
    }

    private static RequestBody toRequestBody(String value) {
        return RequestBody.create(MediaType.parse("text/plain"), value);
    }

    public void getPlacesAround(User user, RectF rect, DefaultCallback<List<SimplePlace>> callback) {
        client.performRequest(client.getRemoteApi().getPlacesAround(user.getToken(), rect.left, rect.top, rect.right, rect.bottom),
                response -> {
                    if (response.getCode() != DefaultResponse.RESPONSE_OK) {
                        callback.onError(response.getCode());
                        return;
                    }
                    callback.onSuccess(response.getSimplePlaces());
                });
    }

    public void getLovePlaces() {

    }

    public void getSearchPlaces() {

    }
}
