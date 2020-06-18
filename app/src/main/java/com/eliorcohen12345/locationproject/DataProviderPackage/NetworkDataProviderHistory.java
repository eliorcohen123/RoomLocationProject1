package com.eliorcohen12345.locationproject.DataProviderPackage;

import android.os.AsyncTask;

import java.util.ArrayList;

import com.eliorcohen12345.locationproject.RoomSearchPackage.PlaceViewModelSearch;
import com.eliorcohen12345.locationproject.RoomSearchPackage.PlacesSearch;
import com.eliorcohen12345.locationproject.DataAppPackage.PlaceModel;
import com.eliorcohen12345.locationproject.MainAndOtherPackage.NearByApplication;

public class NetworkDataProviderHistory {

    public void getPlacesByLocation() {

        //go get data from google API
        //take time...
        //more time...
        //Data received -> resultListener_

        GetPlacesByLocationAsyncTask getPlacesByLocationAsyncTask = new GetPlacesByLocationAsyncTask();
        getPlacesByLocationAsyncTask.execute();
    }

    private static class GetPlacesByLocationAsyncTask extends AsyncTask<Void, Integer, ArrayList<PlacesSearch>> {

        private ArrayList<PlacesSearch> mPlaceModels;
        private PlaceViewModelSearch placeViewModelSearch;

        @Override
        protected ArrayList<PlacesSearch> doInBackground(Void... voids) {
            mPlaceModels = new ArrayList<PlacesSearch>();
            ArrayList<PlacesSearch> listPlaces = new ArrayList<>();
            for (PlacesSearch placeModel : mPlaceModels) {
                try {
                    PlacesSearch place = new PlacesSearch(placeModel.getName(), placeModel.getAddress(), placeModel.getLat(), placeModel.getLng(), placeModel.getPhoto(), placeModel.getIs_open());
                    listPlaces.add(place);
                } catch (Exception e) {

                }
            }
            placeViewModelSearch = new PlaceViewModelSearch(NearByApplication.getApplication());
            placeViewModelSearch.insertPlace(listPlaces);

            return mPlaceModels;
        }

        @Override
        protected void onPostExecute(ArrayList<PlacesSearch> iPlacesDataReceived_) {

        }
    }

}
