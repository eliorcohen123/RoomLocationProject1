package com.eliorcohen123456.locationprojectroom.RoomFavoritesPackage;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {PlacesFavorites.class}, version = 1, exportSchema = false)
public abstract class PlacesRoomDatabaseFavorites extends RoomDatabase {

    public abstract PlacesDaoFavorites placesDao();
    private static volatile PlacesRoomDatabaseFavorites INSTANCE;

    public static PlacesRoomDatabaseFavorites getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (PlacesRoomDatabaseFavorites.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            PlacesRoomDatabaseFavorites.class, "places_database_favorites")
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}
