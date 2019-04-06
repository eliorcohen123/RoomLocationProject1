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

        DeleteLastSearch(PlacesDaoSearch dao) {
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

    private static class updateWordAsyncTask extends AsyncTask<PlacesSearch, Void, Void> {

        private PlacesDaoSearch mAsyncTaskDao;

        updateWordAsyncTask(PlacesDaoSearch dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final PlacesSearch... params) {
            mAsyncTaskDao.update(params[0]);
            return null;
        }
    }

    void updateWord(PlacesSearch word) {
        new updateWordAsyncTask(mPLacesDaoSearch).execute(word);
    }

    private static class deleteWordAsyncTask extends AsyncTask<PlacesSearch, Void, Void> {

        private PlacesDaoSearch mAsyncTaskDao;

        deleteWordAsyncTask(PlacesDaoSearch dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final PlacesSearch... params) {
            mAsyncTaskDao.deleteNew(params[0]);
            return null;
        }
    }

    void deleteWord(PlacesSearch word) {
        new deleteWordAsyncTask(mPLacesDaoSearch).execute(word);
    }

    private static class insertAsyncTask extends AsyncTask<PlacesSearch, Void, Void> {

        private PlacesDaoSearch mAsyncTaskDao;

        insertAsyncTask(PlacesDaoSearch dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final PlacesSearch... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    public void insert(PlacesSearch place_) {
        new insertAsyncTask(mPLacesDaoSearch).execute(place_);
    }

    public void insert(List<PlacesSearch> placeList_) {
        for (PlacesSearch p : placeList_) {
            insert(p);
        }
    }

}
