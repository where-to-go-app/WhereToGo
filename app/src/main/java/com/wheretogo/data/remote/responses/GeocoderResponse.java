package com.wheretogo.data.remote.responses;

import com.google.gson.annotations.SerializedName;

public class GeocoderResponse {
    public static int RESPONSE_OK = 200;
    public static int RESPONSE_PARAM_ERROR = 400;
    public static int RESPONSE_WRONG_KEY = 403;

    @SerializedName("statusCode")
    private int code;
    @SerializedName("error")
    private String error;
}
