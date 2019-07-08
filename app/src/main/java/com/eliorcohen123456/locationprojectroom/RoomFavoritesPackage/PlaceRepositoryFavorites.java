package com.eliorcohen123456.locationprojectroom.RoomFavoritesPackage;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class PlaceRepositoryFavorites {

    private PlacesDaoFavorites mPLacesDaoFavorites;
    private LiveData<List<PlacesFavorites>> mAllPlacesFavorites;

    public PlaceRepositoryFavorites(Application application) {
        PlacesRoomDatabaseFavorites db = PlacesRoomDatabaseFavorites.getDatabase(application);
        mPLacesDaoFavorites = db.placesDao();
        mAllPlacesFavorites = mPLacesDaoFavorites.getAllPlaces();
    }

    public LiveData<List<PlacesFavorites>> getAllPlaces() {
        return mAllPlacesFavorites;
    }

    private static class DeleteLastSearchAsyncTask extends AsyncTask<Void, Void, Void> {

        private PlacesDaoFavorites placesDaoFavorites;

        private DeleteLastSearchAsyncTask(PlacesDaoFavorites dao) {
            placesDaoFavorites = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            placesDaoFavorites.deleteAll();
            return null;
        }
    }

    void deleteLastSearch() {
        DeleteLastSearchAsyncTask deleteLastSearchAsyncTask = new DeleteLastSearchAsyncTask(mPLacesDaoFavorites);
        deleteLastSearchAsyncTask.execute();
    }

    private static class updatePlaceAsyncTask extends AsyncTask<PlacesFavorites, Void, Void> {

        private PlacesDaoFavorites placesDaoFavorites;

        private updatePlaceAsyncTask(PlacesDaoFavorites dao) {
            placesDaoFavorites = dao;
        }

        @Override
        protected Void doInBackground(final PlacesFavorites... params) {
            placesDaoFavorites.update(params[0]);
            return null;
        }
    }

    void updatePlace(PlacesFavorites places) {
        new updatePlaceAsyncTask(mPLacesDaoFavorites).execute(places);
    }

    private static class deletePlaceAsyncTask extends AsyncTask<PlacesFavorites, Void, Void> {

        private PlacesDaoFavorites placesDaoFavorites;

        private deletePlaceAsyncTask(PlacesDaoFavorites dao) {
            placesDaoFavorites = dao;
        }

        @Override
        protected Void doInBackground(final PlacesFavorites... params) {
            placesDaoFavorites.deleteNew(params[0]);
            return null;
        }
    }

    void deletePlace(PlacesFavorites places) {
        new deletePlaceAsyncTask(mPLacesDaoFavorites).execute(places);
    }

    private static class insertAsyncTask extends AsyncTask<PlacesFavorites, Void, Void> {

        private PlacesDaoFavorites placesDaoFavorites;

        private insertAsyncTask(PlacesDaoFavorites dao) {
            placesDaoFavorites = dao;
        }

        @Override
        protected Void doInBackground(final PlacesFavorites... params) {
            placesDaoFavorites.insert(params[0]);
            return null;
        }
    }

    public void insertPlace(PlacesFavorites place_) {
        new insertAsyncTask(mPLacesDaoFavorites).execute(place_);
    }

    public void insertPlace(List<PlacesFavorites> placeList_) {
        for (PlacesFavorites p : placeList_) {
            insertPlace(p);
        }
    }

}
