package com.eliorcohen123456.locationprojectroom.RoomSearchPackage;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class PlaceRepositorySearch {

    private PlacesDaoSearch mPLacesDaoSearch;
    private LiveData<List<PlacesSearch>> mAllPlaces;

    public PlaceRepositorySearch(Application application) {
        PlacesRoomDatabaseSearch db = PlacesRoomDatabaseSearch.getDatabase(application);
        mPLacesDaoSearch = db.placesDao();
        mAllPlaces = mPLacesDaoSearch.getAllPlaces();
    }

    public LiveData<List<PlacesSearch>> getAllPlaces() {
        return mAllPlaces;
    }

    private static class DeleteLastSearch extends AsyncTask<Void, Void, Void> {

        private PlacesDaoSearch placesDaoSearch;

        private DeleteLastSearch(PlacesDaoSearch dao) {
            placesDaoSearch = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            placesDaoSearch.deleteAll();
            return null;
        }
    }

    void deleteLastSearch() {
        DeleteLastSearch deleteLastSearch = new DeleteLastSearch(mPLacesDaoSearch);
        deleteLastSearch.execute();
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
        new updatePlaceAsyncTask(mPLacesDaoSearch).execute(placesSearch);
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
        new deletePlaceAsyncTask(mPLacesDaoSearch).execute(placesSearch);
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

    public void insertPlace(PlacesSearch place_) {
        new insertAsyncTask(mPLacesDaoSearch).execute(place_);
    }

    public void insertPlace(List<PlacesSearch> placeList_) {
        for (PlacesSearch p : placeList_) {
            insertPlace(p);
        }
    }

}
