package com.wheretogo.localDB;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class LocalPhoto {
    @PrimaryKey(autoGenerate = true)
    int id;
    int placeId;
    String photoUrl;
    boolean isMain;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPlaceId() {
        return placeId;
    }

    public void setPlaceId(int placeId) {
        this.placeId = placeId;
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

    public void setMain(boolean main) {
        isMain = main;
    }
}
