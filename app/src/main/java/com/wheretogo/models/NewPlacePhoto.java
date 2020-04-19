package com.wheretogo.models;

import android.graphics.Bitmap;


public class NewPlacePhoto {
    private  boolean isMain;
    private Bitmap bitmap;

    public NewPlacePhoto(Bitmap bitmap) {
        this.bitmap = bitmap;
        isMain = false;
    }

    public boolean isMain() {
        return isMain;
    }

    public void setMain(boolean main) {
        isMain = main;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }
}
