package com.eliorcohen12345.locationproject.RoomFavoritesPackage;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class PlaceViewModelFavorites extends AndroidViewModel {

    private PlaceRepositoryFavorites placeRepositoryFavorites;

    public PlaceViewModelFavorites(Application application) {
        super(application);

        placeRepositoryFavorites = new PlaceRepositoryFavorites(application);
    }

    public LiveData<List<PlacesFavorites>> getAllPlaces() {
        return placeRepositoryFavorites.getAllPlaces();
    }

    public void insertPlace(List<PlacesFavorites> placesFavorites) {
        placeRepositoryFavorites.insertPlace(placesFavorites);
    }

    public void insertPlace(PlacesFavorites placesFavorites) {
        placeRepositoryFavorites.insertPlace(placesFavorites);
    }

    public void deleteAll() {
        placeRepositoryFavorites.deleteLastSearch();
    }

    public void deletePlace(PlacesFavorites places) {
        placeRepositoryFavorites.deletePlace(places);
    }

    public void updatePlace(PlacesFavorites places) {
        placeRepositoryFavorites.updatePlace(places);
    }

    public PlacesFavorites exist(String name, double lat, double lng) {
        return placeRepositoryFavorites.getExist(name, lat, lng);
    }

}
