package com.eliorcohen12345.locationproject.DataProviderPackage;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;

import androidx.core.app.ActivityCompat;

import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.eliorcohen12345.locationproject.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.eliorcohen12345.locationproject.MapsDataPackage.FragmentSearch;
import com.eliorcohen12345.locationproject.RoomSearchPackage.PlacesSearch;
import com.eliorcohen12345.locationproject.RoomSearchPackage.IPlacesDataReceived;
import com.eliorcohen12345.locationproject.DataAppPackage.PlaceModel;
import com.eliorcohen12345.locationproject.MainAndOtherPackage.NearByApplication;
import com.eliorcohen12345.locationproject.RoomSearchPackage.PlaceViewModelSearch;

import eliorcohen.com.googlemapsapi.GoogleMapsApi;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NetworkDataProviderSearch {

    public void getPlacesByLocation(int radius, String page, String open, String type, String query, IPlacesDataReceived resultListener_) {

        //go get data from google API
        //take time...
        //more time...
        //Data received -> resultListener_

        GetPlacesByLocationAsyncTask getPlacesByLocationAsyncTask = new GetPlacesByLocationAsyncTask(resultListener_);
        getPlacesByLocationAsyncTask.execute(String.valueOf(radius), page, open, type, query);
    }

    private class GetPlacesByLocationAsyncTask extends AsyncTask<String, Integer, IPlacesDataReceived> {

        private ArrayList<PlaceModel> mPlaceModels;
        private IPlacesDataReceived mIPlacesDataReceived;
        private PlaceViewModelSearch placeViewModelSearch;
        private Location location;
        private LocationManager locationManager;
        private Criteria criteria;
        private String provider;
        private String urlQuery;
        private double diagonalInches;
        private GoogleMapsApi googleMapsApi = new GoogleMapsApi();

        // startShowingProgressDialog of FragmentSearch
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
                FragmentSearch.startShowingProgressDialog();
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
            provider = locationManager.getBestProvider(criteria, true);
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
                    urlQuery = googleMapsApi.getStringGoogleMapsApi(location.getLatitude(), location.getLongitude(), Integer.parseInt(urls[0]), urls[1], urls[2], urls[3], urls[4], NearByApplication.getApplication().getString(R.string.api_key_search));
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
                        ArrayList<PlacesSearch> listPlaces = new ArrayList<>();
                        for (PlaceModel placeModel : mPlaceModels) {
                            try {
                                PlacesSearch place = new PlacesSearch(placeModel.getName(), placeModel.getVicinity(), placeModel.getGeometry().getLocation().getLat(), placeModel.getGeometry().getLocation().getLng(), placeModel.getPhotos().get(0).getPhoto_reference(), placeModel.getOpening_hours());
                                listPlaces.add(place);
                            } catch (Exception e) {

                            }
                        }
                        placeViewModelSearch = new PlaceViewModelSearch(NearByApplication.getApplication());
                        placeViewModelSearch.deleteAll();
                        placeViewModelSearch.insertPlace(listPlaces);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            return mIPlacesDataReceived;
        }

        private ArrayList<PlaceModel> getLocationListFromJson(String jsonResponse) {
            List<PlaceModel> stuLocationData;
            Gson gson = new GsonBuilder().create();
            LocationResponse response = gson.fromJson(jsonResponse, LocationResponse.class);
            stuLocationData = response.results;
            ArrayList<PlaceModel> arrList = new ArrayList<>();
            arrList.addAll(stuLocationData);

            return arrList;
        }

        private class LocationResponse {

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
                FragmentSearch.stopShowingProgressDialog();
            }

            try {
                iPlacesDataReceived_.onPlacesDataReceived(mPlaceModels);
            } catch (Exception e) {
                iPlacesDataReceived_.onPlacesDataReceived(mPlaceModels);
            }
        }
    }

}
