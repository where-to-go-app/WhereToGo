package com.wheretogo.localDB;

import androidx.room.*;

import java.util.List;
@Dao
public interface LocalPlacesDAO {
    @Insert
    void insertAll(LocalPlace... place);

    @Delete
    void delete(LocalPlace place);

    @Update
    void update(LocalPlace place);

    @Query("SELECT * FROM localplace")
    List<LocalPlace> getAllPlaces();

    @Query("SELECT * FROM localplace where isLovePlace" )
    List<LocalPlace> getFavoritePlaces();

    @Query("SELECT * FROM localplace where :id == id")
    LocalPlace getPlaceById(int id);
}

