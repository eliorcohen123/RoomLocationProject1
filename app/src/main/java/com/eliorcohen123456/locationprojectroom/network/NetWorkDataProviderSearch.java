package com.eliorcohen123456.locationprojectroom.network;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.eliorcohen123456.locationprojectroom.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.eliorcohen123456.locationprojectroom.MapsDataPackage.FragmentSearch;
import com.eliorcohen123456.locationprojectroom.RoomSearchPackage.PlacesSearch;
import com.eliorcohen123456.locationprojectroom.RoomSearchPackage.IPlacesDataReceived;
import com.eliorcohen123456.locationprojectroom.DataAppPackage.PlaceModel;
import com.eliorcohen123456.locationprojectroom.RoomSearchPackage.PlaceRepositorySearch;
import com.eliorcohen123456.locationprojectroom.MainAndOtherPackage.NearByApplication;
import com.eliorcohen123456.locationprojectroom.RoomSearchPackage.PlaceViewModelSearch;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NetWorkDataProviderSearch {

    public void getPlacesByLocation(String url, double radius, String type, IPlacesDataReceived resultListener_) {

        //go get data from google API
        //take time...
        //more time...
        //Data received -> resultListener_

        GetPlacesByLocationAsyncTask getPlacesByLocationAsyncTask = new GetPlacesByLocationAsyncTask(resultListener_);
        getPlacesByLocationAsyncTask.execute(url, String.valueOf(radius), type);
    }

    private class GetPlacesByLocationAsyncTask extends AsyncTask<String, Integer, IPlacesDataReceived> {

        private ArrayList<PlaceModel> mPlaceModels;
        private IPlacesDataReceived mIPlacesDataReceived;
        private PlaceViewModelSearch placeViewModelSearch;
        private Location location;
        private LocationManager locationManager;
        private Criteria criteria;
        private String urlQuery;
        private double diagonalInches;

        // startShowingProgressBar of FragmentSearch
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Tablet/Phone mode
            DisplayMetrics metrics = new DisplayMetrics();
            ((WindowManager) NearByApplication.getApplication().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(metrics);

            float yInches = metrics.heightPixels / metrics.ydpi;
            float xInches = metrics.widthPixels / metrics.xdpi;
            diagonalInches = Math.sqrt(xInches * xInches + yInches * yInches);
            if (diagonalInches < 6.5) {
                FragmentSearch.startShowingProgressBar();
            }
        }

        public GetPlacesByLocationAsyncTask(IPlacesDataReceived iPlacesDataReceived) {
            mIPlacesDataReceived = iPlacesDataReceived;
        }

        @Override
        protected IPlacesDataReceived doInBackground(String... urls) {
            OkHttpClient client = new OkHttpClient();
            locationManager = (LocationManager) NearByApplication.getApplication().getSystemService(Context.LOCATION_SERVICE);
            criteria = new Criteria();
            String provider = locationManager.getBestProvider(criteria, true);
            if (ActivityCompat.checkSelfPermission(NearByApplication.getApplication(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.checkSelfPermission(NearByApplication.getApplication(), Manifest.permission.ACCESS_COARSE_LOCATION);
            }// TODO: Consider calling
//    ActivityCompat#requestPermissions
// here to request the missing permissions, and then overriding
//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                                          int[] grantResults)
// to handle the case where the user grants the permission. See the documentation
// for ActivityCompat#requestPermissions for more details.
            if (provider != null) {
                location = locationManager.getLastKnownLocation(provider);
                // Search maps from that URL and put them in the SQLiteHelper
                if (location != null) {
                    urlQuery = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + location.getLatitude() + "," + location.getLongitude() + "&radius=" + urls[1] + "&rankby=prominence&types=" + urls[2] + "&keyword=" + urls[0] + "&key=" + NearByApplication.getApplication().getString(R.string.api_key_search);
                    Request request = new Request.Builder()
                            .url(urlQuery)
                            .build();
                    Response response = null;
                    try {
                        response = client.newCall(request).execute();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (!response.isSuccessful())
                        try {
                            throw new IOException("Unexpected code " + response);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    try {
                        mPlaceModels = getLocationListFromJson(response.body().string());
                        PlaceRepositorySearch placeRepositorySearch = new PlaceRepositorySearch(NearByApplication.getApplication());
                        ArrayList<PlacesSearch> listPlaces = new ArrayList<>();
                        for (PlaceModel placeModel : mPlaceModels) {
                            try {
                                PlacesSearch place = new PlacesSearch(placeModel.getName(), placeModel.getGeometry().getLocation().getLat(), placeModel.getGeometry().getLocation().getLng(), placeModel.getVicinity(), placeModel.getPhotos().get(0).getPhoto_reference(), false, true, placeModel.getDistance(), placeModel.getRating(), placeModel.getUser_ratings_total());
                                listPlaces.add(place);
                            } catch (Exception e) {

                            }
                        }
                        placeViewModelSearch = new PlaceViewModelSearch(NearByApplication.getApplication());
                        placeViewModelSearch.deleteAll();
                        placeRepositorySearch.insert(listPlaces);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return mIPlacesDataReceived;
        }

        private ArrayList<PlaceModel> getLocationListFromJson(String jsonResponse) {
            List<PlaceModel> stuLocationData = new ArrayList<PlaceModel>();
            Gson gson = new GsonBuilder().create();
            LocationResponse response = gson.fromJson(jsonResponse, LocationResponse.class);
            stuLocationData = response.results;
            ArrayList<PlaceModel> arrList = new ArrayList<>();
            arrList.addAll(stuLocationData);
            return arrList;
        }

        public class LocationResponse {
            private List<PlaceModel> results;

            // public constructor is necessary for collections
            public LocationResponse() {
                results = new ArrayList<PlaceModel>();
            }
        }

        @Override
        protected void onPostExecute(IPlacesDataReceived iPlacesDataReceived_) {
            // Tablet/Phone mode
            DisplayMetrics metrics = new DisplayMetrics();
            ((WindowManager) NearByApplication.getApplication().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(metrics);

            float yInches = metrics.heightPixels / metrics.ydpi;
            float xInches = metrics.widthPixels / metrics.xdpi;
            diagonalInches = Math.sqrt(xInches * xInches + yInches * yInches);
            if (diagonalInches <= 6.5) {
                FragmentSearch.stopShowingProgressBar();
            }
            try {
                iPlacesDataReceived_.onPlacesDataReceived(mPlaceModels);
            } catch (Exception e) {
                iPlacesDataReceived_.onPlacesDataReceived(mPlaceModels);
            }
        }
    }

}
