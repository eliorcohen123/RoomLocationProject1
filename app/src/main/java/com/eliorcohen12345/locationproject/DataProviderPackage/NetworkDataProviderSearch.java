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

import com.eliorcohen12345.locationproject.PagesPackage.SearchFragment;
import com.eliorcohen12345.locationproject.RoomSearchPackage.PlacesSearch;
import com.eliorcohen12345.locationproject.ModelsPackage.Results;
import com.eliorcohen12345.locationproject.OthersPackage.NearByApplication;
import com.eliorcohen12345.locationproject.RoomSearchPackage.PlacesViewModelSearch;

import eliorcohen.com.googlemapsapi.GoogleMapsApi;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NetworkDataProviderSearch {

    public void getPlacesByLocation(int radius, String page, String open, String type, String query) {

        //go get data from google API
        //take time...
        //more time...
        //Data received -> resultListener_

        GetPlacesByLocationAsyncTask getPlacesByLocationAsyncTask = new GetPlacesByLocationAsyncTask();
        getPlacesByLocationAsyncTask.execute(String.valueOf(radius), page, open, type, query);
    }

    private static class GetPlacesByLocationAsyncTask extends AsyncTask<String, Integer, ArrayList<Results>> {

        private ArrayList<Results> mResults = new ArrayList<>();
        private PlacesViewModelSearch placesViewModelSearch;
        private Location location;
        private LocationManager locationManager;
        private Criteria criteria;
        private String provider, urlQuery;
        private double diagonalInches;
        private GoogleMapsApi googleMapsApi = new GoogleMapsApi();

        @Override
        protected ArrayList<Results> doInBackground(String... urls) {
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
                        mResults = getLocationListFromJson(response.body().string());
                        ArrayList<PlacesSearch> listPlaces = new ArrayList<>();
                        for (Results results : mResults) {
                            try {
                                PlacesSearch place = new PlacesSearch(results.getName(), results.getVicinity(), results.getGeometry().getLocation().getLat(), results.getGeometry().getLocation().getLng(), results.getPhotos().get(0).getPhoto_reference(), results.getOpening_hours());
                                listPlaces.add(place);
                            } catch (Exception e) {

                            }
                        }
                        placesViewModelSearch = new PlacesViewModelSearch(NearByApplication.getApplication());
                        placesViewModelSearch.deleteAll();
                        placesViewModelSearch.insertPlace(listPlaces);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            return mResults;
        }

        private ArrayList<Results> getLocationListFromJson(String jsonResponse) {
            List<Results> stuLocationData;
            Gson gson = new GsonBuilder().create();
            LocationResponse response = gson.fromJson(jsonResponse, LocationResponse.class);
            stuLocationData = response.results;
            ArrayList<Results> arrList = new ArrayList<>();
            arrList.addAll(stuLocationData);

            return arrList;
        }

        private static class LocationResponse {

            private List<Results> results;

            // public constructor is necessary for collections
            public LocationResponse() {
                results = new ArrayList<Results>();
            }

        }

        @Override
        protected void onPostExecute(ArrayList<Results> arrayList) {
            // Tablet/Phone mode
            DisplayMetrics metrics = new DisplayMetrics();
            ((WindowManager) NearByApplication.getApplication().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(metrics);

            float yInches = metrics.heightPixels / metrics.ydpi;
            float xInches = metrics.widthPixels / metrics.xdpi;
            diagonalInches = Math.sqrt(xInches * xInches + yInches * yInches);
            if (diagonalInches <= 6.5) {
                SearchFragment.stopShowingProgressDialog();
            }
        }
    }

}
