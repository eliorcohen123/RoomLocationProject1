package com.eliorcohen123456.locationprojectroom.DataProviderPackage;

import android.os.AsyncTask;

import java.util.ArrayList;

import com.eliorcohen123456.locationprojectroom.RoomSearchPackage.PlacesSearch;
import com.eliorcohen123456.locationprojectroom.RoomSearchPackage.IPlacesDataReceived;
import com.eliorcohen123456.locationprojectroom.DataAppPackage.PlaceModel;
import com.eliorcohen123456.locationprojectroom.RoomSearchPackage.PlaceRepositorySearch;
import com.eliorcohen123456.locationprojectroom.MainAndOtherPackage.NearByApplication;

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

        public GetPlacesByLocationAsyncTask(IPlacesDataReceived iPlacesDataReceived) {
            mIPlacesDataReceived = iPlacesDataReceived;
        }

        @Override
        protected IPlacesDataReceived doInBackground(String... urls) {
            mPlaceModels = new ArrayList<PlaceModel>();
            PlaceRepositorySearch placeRepositorySearch = new PlaceRepositorySearch(NearByApplication.getApplication());
            ArrayList<PlacesSearch> listPlaces = new ArrayList<>();
            for (PlaceModel placeModel : mPlaceModels) {
                try {
                    PlacesSearch place = new PlacesSearch(placeModel.getName(), placeModel.getVicinity(), placeModel.getGeometry().getLocation().getLat(), placeModel.getGeometry().getLocation().getLng(), placeModel.getPhotos().get(0).getPhoto_reference(), placeModel.getOpening_hours());
                    listPlaces.add(place);
                } catch (Exception e) {

                }
            }
            placeRepositorySearch.insertPlace(listPlaces);

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
