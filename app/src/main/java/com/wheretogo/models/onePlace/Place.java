
package com.wheretogo.models.onePlace;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Place {

    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("place_desc")
    @Expose
    private String placeDesc;
    @SerializedName("place_name")
    @Expose
    private String placeName;
    @SerializedName("creator_id")
    @Expose
    private Integer creatorId;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("latitude")
    @Expose
    private Double latitude;
    @SerializedName("likes_count")
    @Expose
    private Integer likesCount;
    @SerializedName("longitude")
    @Expose
    private Double longitude;
    @SerializedName("photos")
    @Expose
    private List<Photo> photos = null;
    @SerializedName("comments")
    @Expose
    private List<Comment> comments = null;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPlaceDesc() {
        return placeDesc;
    }

    public void setPlaceDesc(String placeDesc) {
        this.placeDesc = placeDesc;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public Integer getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Integer creatorId) {
        this.creatorId = creatorId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Integer getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(Integer likesCount) {
        this.likesCount = likesCount;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return "Place{" +
                "address='" + address + '\'' +
                ", country='" + country + '\'' +
                ", placeDesc='" + placeDesc + '\'' +
                ", placeName='" + placeName + '\'' +
                ", creatorId=" + creatorId +
                ", id=" + id +
                ", latitude=" + latitude +
                ", likesCount=" + likesCount +
                ", longitude=" + longitude +
                ", photos=" + photos +
                ", comments=" + comments +
                '}';
    }
}
