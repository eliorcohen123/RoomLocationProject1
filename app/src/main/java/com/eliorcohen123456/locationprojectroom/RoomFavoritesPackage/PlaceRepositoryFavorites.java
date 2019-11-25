package com.eliorcohen123456.locationprojectroom.RoomFavoritesPackage;

import android.app.Application;

import androidx.lifecycle.LiveData;

import android.os.AsyncTask;

import java.util.List;

public class PlaceRepositoryFavorites {

    private PlacesDaoFavorites mPlacesDaoFavorites;
    private LiveData<List<PlacesFavorites>> mAllPlacesFavorites;

    public PlaceRepositoryFavorites(Application application) {
        PlacesRoomDatabaseFavorites db = PlacesRoomDatabaseFavorites.getDatabase(application);
        mPlacesDaoFavorites = db.placesDao();
        mAllPlacesFavorites = mPlacesDaoFavorites.getAllPlaces();
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
        DeleteLastSearchAsyncTask deleteLastSearchAsyncTask = new DeleteLastSearchAsyncTask(mPlacesDaoFavorites);
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
        new updatePlaceAsyncTask(mPlacesDaoFavorites).execute(places);
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
        new deletePlaceAsyncTask(mPlacesDaoFavorites).execute(places);
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

    void insertPlace(PlacesFavorites place_) {
        new insertAsyncTask(mPlacesDaoFavorites).execute(place_);
    }

    public void insertPlace(List<PlacesFavorites> placeList_) {
        for (PlacesFavorites p : placeList_) {
            insertPlace(p);
        }
    }

}
