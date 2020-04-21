package com.wheretogo.data.remote;

import com.wheretogo.data.remote.responses.DefaultResponse;
import com.wheretogo.data.remote.responses.PlacesResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface RemoteApi {

    @POST("/api/users/auth")
    Call<PlacesResponse> authUser(
            @Part("secret_string") String secretString,
            @Part("client_id") Integer clientId,
            @Part("first_name") String firstName,
            @Part("last_name") String lastName
    );

    @Multipart
    @POST("/api/places/create")
    Call<DefaultResponse> createPlace(
            @Part("place_name") String placeName,
            @Part("place_desc") String placeDesc,
            @Part("latitude") Double latitude,
            @Part("longitude") Double longitude,
            @Part("address") String address,
            @Part("user_token") String userToken
    );

    @POST("/api/places/update")  // обновить информацию о месте
    Call<DefaultResponse> updatePlace(
            @Query("place_id") Integer id,
            @Query("place_name") String placeName,
            @Query("place_desc") String placeDesc,
            @Query("latitude") Double latitude,
            @Query("longitude") Double longitude,
            @Query("country") String country,
            @Query("address") String address,
            @Query("user_token") String userToken
    );

    @POST("/api/places/delete") // удалить место
    Call<DefaultResponse> deletePlace(
            @Query("place_id") Integer id,
            @Query("user_token") String userToken);


    @GET("/api/places/get_places_by_bounding_box")
    Call<PlacesResponse> getPlaceAround(
            @Query("place_id") Integer id,
            @Query("user_token") String userToken
    );
}
