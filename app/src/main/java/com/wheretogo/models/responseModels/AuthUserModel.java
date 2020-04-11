package com.wheretogo.models.responseModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AuthUserModel {

    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("user_token")
    @Expose
    private String userToken;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

}