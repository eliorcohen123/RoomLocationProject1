package com.eliorcohen12345.locationproject.RoomFavoritesPackage;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class PlacesViewModelFavorites extends AndroidViewModel {

    private PlacesRepositoryFavorites placesRepositoryFavorites;

    public PlacesViewModelFavorites(Application application) {
        super(application);

        placesRepositoryFavorites = new PlacesRepositoryFavorites(application);
    }

    public LiveData<List<PlacesFavorites>> getAllPlaces() {
        return placesRepositoryFavorites.getAllPlaces();
    }

    public void insertPlace(List<PlacesFavorites> placesFavorites) {
        placesRepositoryFavorites.insertPlace(placesFavorites);
    }

    public void insertPlace(PlacesFavorites placesFavorites) {
        placesRepositoryFavorites.insertPlace(placesFavorites);
    }

    public void deleteAll() {
        placesRepositoryFavorites.deleteLastSearch();
    }

    public void deletePlace(PlacesFavorites places) {
        placesRepositoryFavorites.deletePlace(places);
    }

    public void updatePlace(PlacesFavorites places) {
        placesRepositoryFavorites.updatePlace(places);
    }

    public PlacesFavorites exist(String name, double lat, double lng) {
        return placesRepositoryFavorites.getExist(name, lat, lng);
    }

}
