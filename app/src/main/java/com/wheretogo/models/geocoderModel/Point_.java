
package com.wheretogo.models.geocoderModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Point_ {

    @SerializedName("pos")
    @Expose
    private String pos;

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

}
