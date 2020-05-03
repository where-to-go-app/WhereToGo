package com.wheretogo.models;

public class CreatingPlace {
    private int id;
    private String place_name;
    private String placeDesc;
    private float latitude;
    private float longitude;
    private String country;
    private String address;
    private String province;

    public CreatingPlace(String placeName, String placeDesc, float latitude, float longitude, String country, String address, String province) {
        this.place_name = placeName;
        this.placeDesc = placeDesc;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.address = address;
        this.province = province;
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

    public String getProvince() {
        return province;
    }

    @Override
    public String toString() {
        return "CreatingPlace{" +
                "id=" + id +
                ", place_name='" + place_name + '\'' +
                ", placeDesc='" + placeDesc + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", country='" + country + '\'' +
                ", address='" + address + '\'' +
                ", province='" + province + '\'' +
                '}';
    }
}