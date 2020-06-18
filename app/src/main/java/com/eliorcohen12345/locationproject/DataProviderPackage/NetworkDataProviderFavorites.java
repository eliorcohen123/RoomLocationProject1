package com.eliorcohen12345.locationproject.DataProviderPackage;

import android.os.AsyncTask;

import java.util.ArrayList;

import com.eliorcohen12345.locationproject.RoomFavoritesPackage.PlaceViewModelFavorites;
import com.eliorcohen12345.locationproject.RoomFavoritesPackage.PlacesFavorites;
import com.eliorcohen12345.locationproject.DataAppPackage.PlaceModel;
import com.eliorcohen12345.locationproject.MainAndOtherPackage.NearByApplication;
import com.eliorcohen12345.locationproject.RoomSearchPackage.PlacesSearch;

public class NetworkDataProviderFavorites {

    public void getPlacesByLocation() {

        //go get data from google API
        // take time....
        //more time...
        //Data received -> resultListener_

        GetPlacesByLocationAsyncTask getPlacesByLocationAsyncTask = new GetPlacesByLocationAsyncTask();
        getPlacesByLocationAsyncTask.execute();
    }

    private static class GetPlacesByLocationAsyncTask extends AsyncTask<Void, Integer, ArrayList<PlacesFavorites>> {

        private ArrayList<PlacesFavorites> mPlaceModels;
        private PlaceViewModelFavorites placeViewModelFavorites;

        @Override
        protected ArrayList<PlacesFavorites> doInBackground(Void... voids) {
            mPlaceModels = new ArrayList<PlacesFavorites>();
            ArrayList<PlacesFavorites> listPlaces = new ArrayList<>();
            for (PlacesFavorites placeModel : mPlaceModels) {
                try {
                    PlacesFavorites place = new PlacesFavorites(placeModel.getName(), placeModel.getAddress(), placeModel.getLat(), placeModel.getLng(), placeModel.getPhoto());
                    listPlaces.add(place);
                } catch (Exception e) {

                }
            }
            placeViewModelFavorites = new PlaceViewModelFavorites(NearByApplication.getApplication());
            placeViewModelFavorites.insertPlace(listPlaces);

            return mPlaceModels;
        }

        @Override
        protected void onPostExecute(ArrayList<PlacesFavorites> iPlacesDataReceived_) {

        }
    }

}
