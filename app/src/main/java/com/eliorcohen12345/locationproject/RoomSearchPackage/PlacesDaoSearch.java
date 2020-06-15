package com.eliorcohen12345.locationproject.RoomSearchPackage;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PlacesDaoSearch {

    @Insert
    void insert(PlacesSearch place_);

    @Delete
    void deleteNew(PlacesSearch place_);

    @Query("DELETE FROM places_table_search")
    void deleteAll();

    @Query("DELETE FROM places_table_search WHERE name= :name_")
    void deleteByName(String name_);

    @Query("DELETE FROM places_table_search WHERE ID= :id_")
    void deleteByID(Long id_);

    @Query("SELECT * from places_table_search ORDER BY name ASC")
    LiveData<List<PlacesSearch>> getAllPlaces();

    @Update
    void update(PlacesSearch... place_);
}