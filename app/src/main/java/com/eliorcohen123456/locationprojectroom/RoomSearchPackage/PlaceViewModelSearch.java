package com.eliorcohen123456.locationprojectroom.RoomSearchPackage;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class PlaceViewModelSearch extends AndroidViewModel {

    private PlaceRepositorySearch mRepository;

    public PlaceViewModelSearch(Application application) {
        super(application);

        mRepository = new PlaceRepositorySearch(application);
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
