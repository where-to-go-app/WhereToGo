
package com.wheretogo.models.onePlace;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OnePlace {

    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("place")
    @Expose
    private Place place;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

}
