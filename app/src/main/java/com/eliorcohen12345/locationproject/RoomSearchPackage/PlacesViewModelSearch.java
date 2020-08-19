package com.eliorcohen12345.locationproject.RoomSearchPackage;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class PlacesViewModelSearch extends AndroidViewModel {

    private PlacesRepositorySearch mRepository;

    public PlacesViewModelSearch(Application application) {
        super(application);

        mRepository = new PlacesRepositorySearch(application);
    }

    public LiveData<List<PlacesSearch>> getAllPlaces() {
        return mRepository.getAllPlaces();
    }

    public void insertPlace(List<PlacesSearch> placesSearch) {
        mRepository.insertPlace(placesSearch);
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
