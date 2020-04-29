package com.wheretogo.data.remote;

import com.wheretogo.data.remote.responses.DefaultResponse;
import com.wheretogo.data.remote.responses.PlaceResponse;
import com.wheretogo.data.remote.responses.PlacesResponse;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

public interface RemoteApi {

    @FormUrlEncoded
    @POST("/api/users/auth")
    Call<DefaultResponse> authUser(
            @Field("auth_secret_string") String secretString,
            @Field("client_id") Integer clientId,
            @Field("first_name") String firstName,
            @Field("last_name") String lastName
    );

    @Multipart
    @POST("/api/places/create")
    Call<DefaultResponse> createPlace(
            @Part MultipartBody.Part photo,
            @PartMap Map<String, RequestBody> params
    );

    @FormUrlEncoded
    @POST("/api/places/update")  // обновить информацию о месте
    Call<DefaultResponse> updatePlace(
            @Field("user_token") String userToken,
            @Field("place_id") Integer id,
            @Field("place_name") String placeName,
            @Field("place_desc") String placeDesc,
            @Field("latitude") Double latitude,
            @Field("longitude") Double longitude,
            @Field("country") String country,
            @Field("address") String address
    );

    @FormUrlEncoded
    @POST("/api/places/delete") // удалить место
    Call<DefaultResponse> deletePlace(
            @Field("place_id") Integer id,
            @Field("user_token") String userToken);

    @GET("/api/places/place_by_id")
    Call<PlaceResponse> getPlaceById(
            @Query("place_id") Integer id,
            @Query("user_token") String userToken
    );

    @GET("/api/places/places_around")
    Call<PlacesResponse> getPlacesAround(
            @Query("user_token") String userToken,
            @Query("up_left_x") float up_left_x,
            @Query("up_left_y") float up_left_y,
            @Query("bottom_right_x") float bottom_right_x,
            @Query("bottom_right_y") float bottom_right_y
    );
}
