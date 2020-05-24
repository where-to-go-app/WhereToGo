package com.wheretogo.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SimplePlace {
    @SerializedName("id")
    private int id;
    @SerializedName("place_name")
    private String place_name;
    @SerializedName("avatar_url")
    @Expose
    private String avatar_url;
    private float latitude;
    private float longitude;

    public SimplePlace(String placeName, float latitude, float longitude, String avatar_url) {
        this.place_name = placeName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.avatar_url = avatar_url;
    }

    public int getId() {
        return id;
    }

    public String getPlaceName() {
        return place_name;
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlace_name() {
        return place_name;
    }

    @Override
    public String toString() {
        return "SimplePlace{" +
                "id=" + id +
                ", place_name='" + place_name + '\'' +
                ", avatar_url='" + avatar_url + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }

    public String getAvatar_url() {
        return avatar_url;
    }
}