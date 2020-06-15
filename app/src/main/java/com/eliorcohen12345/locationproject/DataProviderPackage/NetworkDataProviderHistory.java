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

    private static class GetPlacesByLocationAsyncTask extends AsyncTask<String, Integer, ArrayList<PlaceModel>> {

        private ArrayList<PlaceModel> mPlaceModels;
        private PlaceViewModelSearch placeViewModelSearch;

        public GetPlacesByLocationAsyncTask() {
        }

        @Override
        protected ArrayList<PlaceModel> doInBackground(String... urls) {
            mPlaceModels = new ArrayList<PlaceModel>();
            ArrayList<PlacesSearch> listPlaces = new ArrayList<>();
            for (PlaceModel placeModel : mPlaceModels) {
                try {
                    PlacesSearch place = new PlacesSearch(placeModel.getName(), placeModel.getVicinity(), placeModel.getGeometry().getLocation().getLat(), placeModel.getGeometry().getLocation().getLng(), placeModel.getPhotos().get(0).getPhoto_reference(), placeModel.getOpening_hours());
                    listPlaces.add(place);
                } catch (Exception e) {

                }
            }
            placeViewModelSearch = new PlaceViewModelSearch(NearByApplication.getApplication());
            placeViewModelSearch.insertPlace(listPlaces);

            return mPlaceModels;
        }

        @Override
        protected void onPostExecute(ArrayList<PlaceModel> iPlacesDataReceived_) {

        }
    }

}
