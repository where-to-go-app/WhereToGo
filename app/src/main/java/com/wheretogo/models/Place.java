package com.wheretogo.models;

public class Place {
    private int id;
    private String placeName;
    private String placeDesc;
    private float latitude;
    private float longitude;
    private String country;
    private String address;

    public Place(int id, String placeName, String placeDesc, float latitude, float longitude, String country, String address) {
        this.id = id;
        this.placeName = placeName;
        this.placeDesc = placeDesc;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public String getPlaceName() {
        return placeName;
    }

    public String getPlaceDesc() {
        return placeDesc;
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public String getCountry() {
        return country;
    }

    public String getAddress() {
        return address;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public void setPlaceDesc(String placeDesc) {
        this.placeDesc = placeDesc;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
