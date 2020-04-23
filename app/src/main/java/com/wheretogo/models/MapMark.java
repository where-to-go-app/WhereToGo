package com.wheretogo.models;

import com.yandex.mapkit.geometry.Point;

public class MapMark {
    private int id;
    private Point pt;
    private int type;
    private String place_name;
    private String description;
    private String creator_name;
    private String country;
    private String province;
    public static int USER_LOCATION = 1;
    public static int PLACES_TO_SHOW = 2;

    public MapMark(Point pt, int id, int type, String place_name){
        this.id = id;
        this.pt = pt;
        this.type = type;
        this.place_name = place_name;
    }
    public int getType(){
        return type;
    }


    public Point getPt() {
        return pt;
    }

    public String getPlace_name() {
        return place_name;
    }
}
