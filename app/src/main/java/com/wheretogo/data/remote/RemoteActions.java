package com.wheretogo.data.remote;

import android.graphics.RectF;

import com.wheretogo.data.remote.responses.DefaultResponse;
import com.wheretogo.data.remote.responses.PlaceResponse;
import com.wheretogo.data.remote.responses.PlacesResponse;
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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        client.getRemoteApi().authUser(SECRET_STRING, user.getClientId(), user.getFirstName(), user.getLastName()).enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                String token = response.body().getMessage();
                if (token == null) {
                    callback.onError(0);
                    return;
                }
                callback.onSuccess(token);
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable throwable) {

            }
        });



    }
    public void getPlaceById(User user, int id, DefaultCallback<Place> callback) {
        client.getRemoteApi().getPlaceById(id, user.getToken()).enqueue(new Callback<PlaceResponse>() {
            @Override
            public void onResponse(Call<PlaceResponse> call, Response<PlaceResponse> response) {
                if (response.body().getCode() != DefaultResponse.RESPONSE_OK) {
                    callback.onError(response.body().getCode());
                    return;
                }
                callback.onSuccess(response.body().getPlace());
            }

            @Override
            public void onFailure(Call<PlaceResponse> call, Throwable throwable) {
                callback.onError(DefaultResponse.RESPONSE_UNKNOWN_ERROR);
            }
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
        client.getRemoteApi().createPlace(
                MultipartBody.Part.createFormData(simplePlace.getPlaceName(), simplePlace.getPlaceName(), RequestBody.create(MediaType.parse("image/*"), photo)),
                map).enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                callback.onSuccess(response.body().getCode() == DefaultResponse.RESPONSE_OK);
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable throwable) {
                callback.onError(DefaultResponse.RESPONSE_UNKNOWN_ERROR);
            }
        });
    }

    private static RequestBody toRequestBody(String value) {
        return RequestBody.create(MediaType.parse("text/plain"), value);
    }

    public void getPlacesAround(User user, RectF rect, DefaultCallback<List<SimplePlace>> callback) {
        client.getRemoteApi().getPlacesAround(user.getToken(), rect.left, rect.top, rect.right, rect.bottom).enqueue(
                new Callback<PlacesResponse>() {
            @Override
            public void onResponse(Call<PlacesResponse> call, Response<PlacesResponse> response) {
                callback.onSuccess(response.body().getSimplePlaces());
            }

            @Override
            public void onFailure(Call<PlacesResponse> call, Throwable throwable) {
                callback.onError(DefaultResponse.RESPONSE_UNKNOWN_ERROR);
            }
        });
    }

    public void getLovePlaces() {

    }

    public void getSearchPlaces() {

    }
}
