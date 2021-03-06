package com.eliorcohen12345.locationproject.RoomFavoritesPackage;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PlacesDaoFavorites {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(PlacesFavorites place_);

    @Delete
    void deleteNew(PlacesFavorites place_);

    @Query("DELETE FROM places_table_favorites")
    void deleteAll();

    @Query("DELETE FROM places_table_favorites WHERE name= :name_")
    void deleteByName(String name_);

    @Query("DELETE FROM places_table_favorites WHERE ID= :id_")
    void deleteByID(Long id_);

    @Query("SELECT * from places_table_favorites")
    LiveData<List<PlacesFavorites>> getAllPlaces();

    @Update
    void update(PlacesFavorites... place_);

    @Query("SELECT * from places_table_favorites WHERE name= :name AND lat= :lat AND lng= :lng")
    PlacesFavorites getItemById(String name, double lat, double lng);
}