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

    private static class DeleteLastSearch extends AsyncTask<Void, Void, Void> {

        private PlacesDaoFavorites placesDaoFavorites;

        private DeleteLastSearch(PlacesDaoFavorites dao) {
            placesDaoFavorites = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            placesDaoFavorites.deleteAll();
            return null;
        }
    }

    void deleteLastSearch() {
        DeleteLastSearch deleteLastSearch = new DeleteLastSearch(mPLacesDaoFavorites);
        deleteLastSearch.execute();
    }

    private static class updateWordAsyncTask extends AsyncTask<PlacesFavorites, Void, Void> {

        private PlacesDaoFavorites placesDaoFavorites;

        private updateWordAsyncTask(PlacesDaoFavorites dao) {
            placesDaoFavorites = dao;
        }

        @Override
        protected Void doInBackground(final PlacesFavorites... params) {
            placesDaoFavorites.update(params[0]);
            return null;
        }
    }

    void updateWord(PlacesFavorites places) {
        new updateWordAsyncTask(mPLacesDaoFavorites).execute(places);
    }

    private static class deleteWordAsyncTask extends AsyncTask<PlacesFavorites, Void, Void> {

        private PlacesDaoFavorites placesDaoFavorites;

        private deleteWordAsyncTask(PlacesDaoFavorites dao) {
            placesDaoFavorites = dao;
        }

        @Override
        protected Void doInBackground(final PlacesFavorites... params) {
            placesDaoFavorites.deleteNew(params[0]);
            return null;
        }
    }

    void deleteWord(PlacesFavorites places) {
        new deleteWordAsyncTask(mPLacesDaoFavorites).execute(places);
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

    public void insert(PlacesFavorites place_) {
        new insertAsyncTask(mPLacesDaoFavorites).execute(place_);
    }

    public void insert(List<PlacesFavorites> placeList_) {
        for (PlacesFavorites p : placeList_) {
            insert(p);
        }
    }

}
