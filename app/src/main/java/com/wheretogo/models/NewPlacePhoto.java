package com.wheretogo.models;

public class NewPlacePhoto {
    private  boolean isMain;
    private String res;

    public NewPlacePhoto(String res) {
        this.res = res;
        isMain = false;
    }

    public boolean isMain() {
        return isMain;
    }

    public String getRes() {
        return res;
    }
}
