package com.wheretogo.models;

public class SimplePlace {
    private int id;
    private String place_name;
    private float latitude;
    private float longitude;

    public SimplePlace(String placeName, float latitude, float longitude) {
        this.place_name = placeName;
        this.latitude = latitude;
        this.longitude = longitude;

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



    @Override
    public String toString() {
        return "Place{" +
                "id=" + id +
                ", placeName='" + place_name + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +

                '}';
    }
}