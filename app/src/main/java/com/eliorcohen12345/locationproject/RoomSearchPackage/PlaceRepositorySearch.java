package com.eliorcohen12345.locationproject.RoomSearchPackage;

import android.app.Application;

import androidx.lifecycle.LiveData;

import android.os.AsyncTask;

import java.util.List;

public class PlaceRepositorySearch {

    private PlacesDaoSearch mPlacesDaoSearch;
    private LiveData<List<PlacesSearch>> mAllPlaces;

    public PlaceRepositorySearch(Application application) {
        PlacesRoomDatabaseSearch db = PlacesRoomDatabaseSearch.getDatabase(application);
        mPlacesDaoSearch = db.placesDao();
        mAllPlaces = mPlacesDaoSearch.getAllPlaces();
    }

    public LiveData<List<PlacesSearch>> getAllPlaces() {
        return mAllPlaces;
    }

    private static class DeleteLastSearchAsyncTask extends AsyncTask<Void, Void, Void> {

        private PlacesDaoSearch placesDaoSearch;

        private DeleteLastSearchAsyncTask(PlacesDaoSearch dao) {
            placesDaoSearch = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            placesDaoSearch.deleteAll();
            return null;
        }
    }

    void deleteLastSearch() {
        DeleteLastSearchAsyncTask deleteLastSearchAsyncTask = new DeleteLastSearchAsyncTask(mPlacesDaoSearch);
        deleteLastSearchAsyncTask.execute();
    }

    private static class updatePlaceAsyncTask extends AsyncTask<PlacesSearch, Void, Void> {

        private PlacesDaoSearch mAsyncTaskDao;

        private updatePlaceAsyncTask(PlacesDaoSearch dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final PlacesSearch... params) {
            mAsyncTaskDao.update(params[0]);
            return null;
        }
    }

    void updatePlace(PlacesSearch placesSearch) {
        new updatePlaceAsyncTask(mPlacesDaoSearch).execute(placesSearch);
    }

    private static class deletePlaceAsyncTask extends AsyncTask<PlacesSearch, Void, Void> {

        private PlacesDaoSearch mAsyncTaskDao;

        private deletePlaceAsyncTask(PlacesDaoSearch dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final PlacesSearch... params) {
            mAsyncTaskDao.deleteNew(params[0]);
            return null;
        }
    }

    void deletePlace(PlacesSearch placesSearch) {
        new deletePlaceAsyncTask(mPlacesDaoSearch).execute(placesSearch);
    }

    private static class insertAsyncTask extends AsyncTask<PlacesSearch, Void, Void> {

        private PlacesDaoSearch mAsyncTaskDao;

        private insertAsyncTask(PlacesDaoSearch dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final PlacesSearch... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    void insertPlace(PlacesSearch place_) {
        new insertAsyncTask(mPlacesDaoSearch).execute(place_);
    }

    public void insertPlace(List<PlacesSearch> placeList_) {
        for (PlacesSearch p : placeList_) {
            insertPlace(p);
        }
    }

}
