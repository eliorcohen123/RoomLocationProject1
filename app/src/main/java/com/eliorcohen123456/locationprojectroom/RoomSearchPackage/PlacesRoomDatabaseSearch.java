package com.eliorcohen123456.locationprojectroom.RoomSearchPackage;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {PlacesSearch.class}, version = 1, exportSchema = false)
public abstract class PlacesRoomDatabaseSearch extends RoomDatabase {

    public abstract PlacesDaoSearch placesDao();
    private static volatile PlacesRoomDatabaseSearch INSTANCE;

    public static PlacesRoomDatabaseSearch getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (PlacesRoomDatabaseSearch.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            PlacesRoomDatabaseSearch.class, "places_database_search")
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}
