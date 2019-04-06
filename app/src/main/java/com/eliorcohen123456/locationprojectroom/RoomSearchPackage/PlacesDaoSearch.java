package com.eliorcohen123456.locationprojectroom.RoomSearchPackage;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

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