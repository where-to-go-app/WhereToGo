package com.wheretogo.data.remote;

import com.wheretogo.models.responseModels.AuthUserModel;
import com.wheretogo.models.responseModels.SimpleResponse;

import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface RemoteApi {
    @POST("/api/users/auth")
    Call<AuthUserModel> authUser(@Query("secret_string") String secretString,
                                 @Query("client_id") Integer clientId,
                                 @Query("first_name") String firstName,
                                 @Query("last_name") String lastName);


    @Multipart  // TODO научиться добавлять фотографии
    @POST("/api/places/create")
    Call<SimpleResponse> createPlace(@Part("place_name") String placeName,
                                     @Part("place_desc") String placeDesc,
                                     @Part("latitude") Double latitude,
                                     @Part("longitude") Double longitude,
                                     @Part("address") String address,
                                     @Part("user_token") String userToken);

    @POST("/api/places/update")  // обновить информацию о месте
    Call<SimpleResponse> updatePlace(@Query("place_id") Integer id,
                                     @Query("place_name") String placeName,
                                     @Query("place_desc") String placeDesc,
                                     @Query("latitude") Double latitude,
                                     @Query("longitude") Double longitude,
                                     @Query("country") String country,
                                     @Query("address") String address,
                                     @Query("user_token") String userToken);

    @POST("/api/places/delete") // удалить место
    Call<SimpleResponse> deletePlace(@Query("place_id") Integer id,
                                     @Query("user_token") String userToken);
}
