package com.wheretogo.localDB;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {LocalPlace.class, LocalPhoto.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract LocalPlacesDAO getPlacesDao();
    public abstract PhotosDAO getPhotosDAO();
}
