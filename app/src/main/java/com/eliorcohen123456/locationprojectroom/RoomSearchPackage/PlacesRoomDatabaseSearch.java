package com.eliorcohen123456.locationprojectroom.RoomSearchPackage;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;

@Database(entities = {PlacesSearch.class}, version = 1, exportSchema = false)
//@TypeConverters(Converter.class)
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
