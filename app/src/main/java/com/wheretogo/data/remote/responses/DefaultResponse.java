package com.wheretogo.data.remote.responses;

import com.google.gson.annotations.SerializedName;

public class DefaultResponse {
    public static int RESPONSE_OK = 0;
    public static int RESPONSE_USER_NOT_FOUND = 1;
    public static int RESPONSE_AUTH_ERROR = 2;
    public static int RESPONSE_NO_PERMISSION = 3;
    public static int RESPONSE_ENTITY_NOT_FOUND = 4;
    public static int RESPONSE_UNKNOWN_ERROR = 5;

    @SerializedName("code")
    private int code;
    @SerializedName("message")
    private String message;

    public DefaultResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public DefaultResponse(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
