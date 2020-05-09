
package com.wheretogo.models.onePlace;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Photo {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("photo_name")
    @Expose
    private String photoName;
    @SerializedName("photo_url")
    @Expose
    private String photoUrl;
    @SerializedName("is_main")
    @Expose
    private boolean isMain;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPhotoName() {
        return photoName;
    }

    public void setPhotoName(String photoName) {
        this.photoName = photoName;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public boolean isMain() {
        return isMain;
    }
}
