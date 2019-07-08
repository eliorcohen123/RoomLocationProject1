package com.eliorcohen123456.locationprojectroom.RoomSearchPackage;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

public class PlaceViewModelSearch extends AndroidViewModel {

    private PlaceRepositorySearch mRepository;
    private LiveData<List<PlacesSearch>> mAllPlaces;

    public PlaceViewModelSearch(Application application) {
        super(application);
        mRepository = new PlaceRepositorySearch(application);
        mAllPlaces = mRepository.getAllPlaces();
    }

    public LiveData<List<PlacesSearch>> getAllPlaces() {
        return mAllPlaces;
    }

    public void insert(PlacesSearch placesSearch) {
        mRepository.insert(placesSearch);
    }

    public void deleteAll() {
        mRepository.deleteLastSearch();
    }

    public void deletePlace(PlacesSearch placesSearch) {
        mRepository.deletePlace(placesSearch);
    }

    public void updatePlace(PlacesSearch placesSearch) {
        mRepository.updatePlace(placesSearch);
    }

}
