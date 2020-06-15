package com.eliorcohen12345.locationproject.DataProviderPackage;

import android.os.AsyncTask;

import java.util.ArrayList;

import com.eliorcohen12345.locationproject.RoomSearchPackage.PlaceViewModelSearch;
import com.eliorcohen12345.locationproject.RoomSearchPackage.PlacesSearch;
import com.eliorcohen12345.locationproject.RoomSearchPackage.IPlacesDataReceived;
import com.eliorcohen12345.locationproject.DataAppPackage.PlaceModel;
import com.eliorcohen12345.locationproject.MainAndOtherPackage.NearByApplication;

public class NetworkDataProviderHistory {

    public void getPlacesByLocation(IPlacesDataReceived resultListener_) {

        //go get data from google API
        //take time...
        //more time...
        //Data received -> resultListener_

        GetPlacesByLocationAsyncTask getPlacesByLocationAsyncTask = new GetPlacesByLocationAsyncTask(resultListener_);
        getPlacesByLocationAsyncTask.execute();
    }

    private class GetPlacesByLocationAsyncTask extends AsyncTask<String, Integer, IPlacesDataReceived> {

        private ArrayList<PlaceModel> mPlaceModels;
        private IPlacesDataReceived mIPlacesDataReceived;
        private PlaceViewModelSearch placeViewModelSearch;

        public GetPlacesByLocationAsyncTask(IPlacesDataReceived iPlacesDataReceived) {
            mIPlacesDataReceived = iPlacesDataReceived;
        }

        @Override
        protected IPlacesDataReceived doInBackground(String... urls) {
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

            return mIPlacesDataReceived;
        }

        @Override
        protected void onPostExecute(IPlacesDataReceived iPlacesDataReceived_) {
            try {
                iPlacesDataReceived_.onPlacesDataReceived(mPlaceModels);
            } catch (Exception e) {
                iPlacesDataReceived_.onPlacesDataReceived(mPlaceModels);
            }
        }
    }

}
