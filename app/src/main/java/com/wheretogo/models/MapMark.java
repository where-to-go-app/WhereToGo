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

    public MapMark(Point pt, int type){
        this.pt = pt;
        this.type = type;
    }
    public int getType(){
        return type;
    }



}
