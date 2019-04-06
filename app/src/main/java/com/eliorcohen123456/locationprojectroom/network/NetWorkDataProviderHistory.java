package com.eliorcohen123456.locationprojectroom.network;

import android.os.AsyncTask;

import java.util.ArrayList;

import com.eliorcohen123456.locationprojectroom.RoomSearchPackage.PlacesSearch;
import com.eliorcohen123456.locationprojectroom.RoomSearchPackage.IPlacesDataReceived;
import com.eliorcohen123456.locationprojectroom.DataAppPackage.LocationModel;
import com.eliorcohen123456.locationprojectroom.RoomSearchPackage.PlaceRepositorySearch;
import com.eliorcohen123456.locationprojectroom.MainAndOtherPackage.NearByApplication;

public class NetWorkDataProviderHistory {

    public void getPlacesByLocation(IPlacesDataReceived resultListener_) {

        //go get data from google API
        //take time...
        //more time...
        //Data received -> resultListener_

        GetPlacesByLocationAsyncTask getPlacesByLocationAsyncTask = new GetPlacesByLocationAsyncTask(resultListener_);
        getPlacesByLocationAsyncTask.execute();
    }

    private class GetPlacesByLocationAsyncTask extends AsyncTask<String, Integer, IPlacesDataReceived> {

        private ArrayList<LocationModel> mLocationModels;
        private IPlacesDataReceived mIPlacesDataReceived;

        public GetPlacesByLocationAsyncTask(IPlacesDataReceived iPlacesDataReceived) {
            mIPlacesDataReceived = iPlacesDataReceived;
        }

        @Override
        protected IPlacesDataReceived doInBackground(String... urls) {
            mLocationModels = new ArrayList<LocationModel>();
            PlaceRepositorySearch placeRepositorySearch = new PlaceRepositorySearch(NearByApplication.getApplication());
            ArrayList<PlacesSearch> listPlaces = new ArrayList<>();
            for (LocationModel locationModel : mLocationModels) {
                try {
                    PlacesSearch place = new PlacesSearch(locationModel.getName(), locationModel.getGeometry().getLocation().getLat(), locationModel.getGeometry().getLocation().getLng(), locationModel.getVicinity(), locationModel.getPhotos().get(0).getPhoto_reference(), false, true, locationModel.getDistance(), locationModel.getRating(), locationModel.getUser_ratings_total());
                    listPlaces.add(place);
                } catch (Exception e) {

                }
            }
            placeRepositorySearch.insert(listPlaces);
            return mIPlacesDataReceived;
        }

        @Override
        protected void onPostExecute(IPlacesDataReceived iPlacesDataReceived_) {
            try {
                iPlacesDataReceived_.onPlacesDataReceived(mLocationModels);
            } catch (Exception e) {
                iPlacesDataReceived_.onPlacesDataReceived(mLocationModels);
            }
        }
    }

}