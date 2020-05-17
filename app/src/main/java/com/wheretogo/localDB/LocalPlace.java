package com.wheretogo.localDB;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.wheretogo.models.onePlace.Place;

@Entity
public class LocalPlace {
    @PrimaryKey int id;
    double latitude;
    double longitude;
    String placeName;
    String placeDescription;
    int creator_id;
    String country;
    String address;
    boolean isLovePlace;

    public void setId(int id) {
        this.id = id;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public void setPlaceDescription(String placeDescription) {
        this.placeDescription = placeDescription;
    }

    public void setCreator_id(int creator_id) {
        this.creator_id = creator_id;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setLovePlace(boolean lovePlace) {
        isLovePlace = lovePlace;
    }

    public int getId() {
        return id;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getPlaceName() {
        return placeName;
    }

    public String getPlaceDescription() {
        return placeDescription;
    }

    public int getCreator_id() {
        return creator_id;
    }

    public String getCountry() {
        return country;
    }

    public String getAddress() {
        return address;
    }

    public boolean isLovePlace() {
        return isLovePlace;
    }
}
