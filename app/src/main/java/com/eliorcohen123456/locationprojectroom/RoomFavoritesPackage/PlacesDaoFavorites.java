package com.eliorcohen123456.locationprojectroom.RoomFavoritesPackage;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface PlacesDaoFavorites {

    @Insert
    void insert(PlacesFavorites place_);

    @Delete
    void deleteNew(PlacesFavorites place_);

    @Query("DELETE FROM places_table_favorites")
    void deleteAll();

    @Query("DELETE FROM places_table_favorites WHERE name= :name_")
    void deleteByName(String name_);

    @Query("DELETE FROM places_table_favorites WHERE ID= :id_")
    void deleteByID(Long id_);

    @Query("SELECT * from places_table_favorites ORDER BY name ASC")
    LiveData<List<PlacesFavorites>> getAllPlaces();

    @Update
    void update(PlacesFavorites... place_);
}