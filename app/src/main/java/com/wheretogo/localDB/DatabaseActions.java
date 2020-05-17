package com.wheretogo.localDB;

import android.util.Log;

import java.util.List;

public class DatabaseActions {
    private AppDatabase db;

    public DatabaseActions(AppDatabase db){
        this.db = db;
    }

    public List<LocalPlace> getPlacesAround(double left, double top, double right, double bottom, boolean isFavorites){
        return  db.getPlacesDao().getPlacesAround(left, top, right, bottom, isFavorites);
    }

    public void addPlace(LocalPlace... places){
        for (LocalPlace place: places) {
            if (db.getPlacesDao().getPlaceById(place.id) == null){
                db.getPlacesDao().insertAll(place);
            }else{
                db.getPlacesDao().update(place);
            }
        }
    }

    public LocalPlace getPlaceById(int id){
        return db.getPlacesDao().getPlaceById(id);
    }

    public List<LocalPhoto> getPhotosToPlace(int id){
        return db.getPhotosDAO().getPhotosToPlace(id);
    }

    public  List<LocalPhoto> getAll(){
        return db.getPhotosDAO().getAll();

    }
    public void addPhoto(LocalPhoto photo){
        if (db.getPhotosDAO().getPhotoById(photo.id) == null){
            Log.d("db", String.valueOf(photo.id));
            db.getPhotosDAO().insertAll(photo);
        }else{
            db.getPhotosDAO().update(photo);
        }
    }
}
