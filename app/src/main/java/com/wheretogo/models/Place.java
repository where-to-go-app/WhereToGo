package com.wheretogo.models;

public class Place {
    private int id;
    private String place_name;
    private String placeDesc;
    private float latitude;
    private float longitude;
    private String country;
    private String address;

    public Place(String placeName, String placeDesc, float latitude, float longitude, String country, String address) {
        this.place_name = placeName;
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
        return place_name;
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
        this.place_name = placeName;
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

    @Override
    public String toString() {
        return "Place{" +
                "id=" + id +
                ", placeName='" + place_name + '\'' +
                ", placeDesc='" + placeDesc + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", country='" + country + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}