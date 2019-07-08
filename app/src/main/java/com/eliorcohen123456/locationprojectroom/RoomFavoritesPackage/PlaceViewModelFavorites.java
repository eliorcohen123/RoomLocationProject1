package com.eliorcohen123456.locationprojectroom.RoomFavoritesPackage;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

public class PlaceViewModelFavorites extends AndroidViewModel {

    private PlaceRepositoryFavorites placeRepositoryFavorites;
    private LiveData<List<PlacesFavorites>> mAllPlacesFavorites;

    public PlaceViewModelFavorites(Application application) {
        super(application);
        placeRepositoryFavorites = new PlaceRepositoryFavorites(application);
        mAllPlacesFavorites = placeRepositoryFavorites.getAllPlaces();
    }

    public LiveData<List<PlacesFavorites>> getAllPlaces() {
        return mAllPlacesFavorites;
    }

    public void insert(PlacesFavorites placesFavorites) {
        placeRepositoryFavorites.insert(placesFavorites);
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

}
