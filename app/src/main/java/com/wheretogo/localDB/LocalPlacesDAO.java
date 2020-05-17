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

    @Query("SELECT * FROM localplace where :left < longitude < :right and :bottom < longitude < :top and isLovePlace == :isFavorites" )
    List<LocalPlace> getPlacesAround(double left, double top, double right, double bottom, boolean isFavorites);

    @Query("SELECT * FROM localplace where :id == id")
    LocalPlace getPlaceById(int id);
}

