package com.wheretogo.localDB;

import androidx.room.*;

import java.util.List;

@Dao
public interface PhotosDAO {
    @Insert
    void insertAll(LocalPhoto... photos);

    @Delete
    void delete(LocalPhoto photo);

    @Update
    void update(LocalPhoto photo);

    @Query("SELECT * FROM LocalPhoto where :id == id")
    LocalPhoto getPhotoById(int id);

    @Query("SELECT * FROM LocalPhoto")
    List<LocalPhoto> getAll();

    @Query("SELECT * FROM LocalPhoto where :id == placeId")
    List<LocalPhoto> getPhotosToPlace(int id);
}
