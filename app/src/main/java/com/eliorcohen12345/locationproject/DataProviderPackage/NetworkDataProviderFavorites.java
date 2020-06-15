package com.eliorcohen12345.locationproject.DataProviderPackage;

import android.os.AsyncTask;

import java.util.ArrayList;

import com.eliorcohen12345.locationproject.RoomFavoritesPackage.PlaceRepositoryFavorites;
import com.eliorcohen12345.locationproject.RoomFavoritesPackage.PlaceViewModelFavorites;
import com.eliorcohen12345.locationproject.RoomFavoritesPackage.PlacesFavorites;
import com.eliorcohen12345.locationproject.RoomSearchPackage.IPlacesDataReceived;
import com.eliorcohen12345.locationproject.DataAppPackage.PlaceModel;
import com.eliorcohen12345.locationproject.MainAndOtherPackage.NearByApplication;

public class NetworkDataProviderFavorites {

    public void getPlacesByLocation(IPlacesDataReceived resultListener_) {

        //go get data from google API
        // take time....
        //more time...
        //Data received -> resultListener_

        GetPlacesByLocationAsyncTask getPlacesByLocationAsyncTask = new GetPlacesByLocationAsyncTask(resultListener_);
        getPlacesByLocationAsyncTask.execute();
    }

    private class GetPlacesByLocationAsyncTask extends AsyncTask<String, Integer, IPlacesDataReceived> {

        private ArrayList<PlaceModel> mPlaceModels;
        private IPlacesDataReceived mIPlacesDataReceived;
        private PlaceViewModelFavorites placeViewModelFavorites;

        public GetPlacesByLocationAsyncTask(IPlacesDataReceived iPlacesDataReceived) {
            mIPlacesDataReceived = iPlacesDataReceived;
        }

        @Override
        protected IPlacesDataReceived doInBackground(String... urls) {
            mPlaceModels = new ArrayList<PlaceModel>();
            PlaceRepositoryFavorites placeRepository = new PlaceRepositoryFavorites(NearByApplication.getApplication());
            ArrayList<PlacesFavorites> listPlaces = new ArrayList<>();
            for (PlaceModel placeModel : mPlaceModels) {
                try {
                    PlacesFavorites place = new PlacesFavorites(placeModel.getName(), placeModel.getVicinity(), placeModel.getGeometry().getLocation().getLat(), placeModel.getGeometry().getLocation().getLng(), placeModel.getPhotos().get(0).getPhoto_reference());
                    listPlaces.add(place);
                } catch (Exception e) {

                }
            }
            placeViewModelFavorites = new PlaceViewModelFavorites(NearByApplication.getApplication());
            placeViewModelFavorites.insertPlace(listPlaces);

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
