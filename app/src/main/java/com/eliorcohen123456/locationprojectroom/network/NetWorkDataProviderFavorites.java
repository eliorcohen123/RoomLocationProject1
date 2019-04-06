package com.eliorcohen123456.locationprojectroom.network;

import android.os.AsyncTask;

import com.eliorcohen123456.locationprojectroom.RoomFavoritesPackage.PlaceRepositoryFavorites;
import com.eliorcohen123456.locationprojectroom.RoomFavoritesPackage.PlacesFavorites;

import java.util.ArrayList;

import com.eliorcohen123456.locationprojectroom.RoomSearchPackage.IPlacesDataReceived;
import com.eliorcohen123456.locationprojectroom.DataAppPackage.LocationModel;
import com.eliorcohen123456.locationprojectroom.MainAndOtherPackage.NearByApplication;

public class NetWorkDataProviderFavorites {

    public void getPlacesByLocation(IPlacesDataReceived resultListener_) {

        //go get data from google API
        // take time....
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
            PlaceRepositoryFavorites placeRepository = new PlaceRepositoryFavorites(NearByApplication.getApplication());
            ArrayList<PlacesFavorites> listPlaces = new ArrayList<>();
            for (LocationModel locationModel : mLocationModels) {
                try {
                    PlacesFavorites place = new PlacesFavorites(locationModel.getName(), locationModel.getVicinity(), locationModel.getGeometry().getLocation().getLat(), locationModel.getGeometry().getLocation().getLng(), locationModel.getPhotos().get(0).getPhoto_reference());
                    listPlaces.add(place);
                } catch (Exception e) {

                }
            }
            placeRepository.insert(listPlaces);
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
