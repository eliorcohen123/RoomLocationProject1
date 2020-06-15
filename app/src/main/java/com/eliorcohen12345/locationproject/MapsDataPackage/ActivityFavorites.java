package com.eliorcohen12345.locationproject.MapsDataPackage;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.eliorcohen12345.locationproject.DataProviderPackage.NetworkDataProviderFavorites;
import com.eliorcohen12345.locationproject.GeofencePackage.Constants;
import com.eliorcohen12345.locationproject.GeofencePackage.GeofenceBroadcastReceiver;
import com.eliorcohen12345.locationproject.GeofencePackage.GeofenceErrorMessages;
import com.eliorcohen12345.locationproject.MainAndOtherPackage.GoogleService;
import com.eliorcohen12345.locationproject.MainAndOtherPackage.NearByApplication;
import com.eliorcohen12345.locationproject.R;
import com.eliorcohen12345.locationproject.RoomFavoritesPackage.PlaceViewModelFavorites;
import com.eliorcohen12345.locationproject.RoomFavoritesPackage.PlacesFavorites;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

// Activity of FragmentFavorites
public class ActivityFavorites extends AppCompatActivity implements OnCompleteListener<Void> {

    private PlaceViewModelFavorites mPlacesViewModel;
    private NetworkDataProviderFavorites networkDataProviderFavorites;
    private GeofencingClient mGeofencingClient;
    private ArrayList<Geofence> mGeofenceList;
    private PendingIntent mGeofencePendingIntent;
    private PendingGeofenceTask mPendingGeofenceTask = PendingGeofenceTask.NONE;
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 33;
    private static final String TAG = "MyLocation";
    private int myRadiusGeo;
    private SharedPreferences prefsSeekGeo;
    private Location location;
    private LocationManager locationManager;
    private Criteria criteria;
    private String provider;
    private boolean boolean_permission;
    private SharedPreferences mPref;
    private SharedPreferences.Editor mEdit;

    private enum PendingGeofenceTask {
        ADD, REMOVE, NONE
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        initUI();
        frg();
    }

    // onStart
    @Override
    public void onStart() {
        super.onStart();

        if (myRadiusGeo != 0) {
            performPendingGeofenceTask();
        }
    }

    private void initUI() {
        networkDataProviderFavorites = new NetworkDataProviderFavorites();
        networkDataProviderFavorites.getPlacesByLocation();

        mPlacesViewModel = ViewModelProviders.of(this).get(PlaceViewModelFavorites.class);

        prefsSeekGeo = PreferenceManager.getDefaultSharedPreferences(this);
        myRadiusGeo = prefsSeekGeo.getInt("seek_geo", 500);

        if (myRadiusGeo == 0) {
            Toast.makeText(this, "Radius cannot be equal to 0", Toast.LENGTH_LONG).show();
        } else {
            mPlacesViewModel.getAllPlaces().observe(this, this::populateGeofenceList);
        }

        if (myRadiusGeo != 0) {
            mGeofenceList = new ArrayList<>();
            mGeofencePendingIntent = null;

            mPlacesViewModel.getAllPlaces().observe(this, this::populateGeofenceList);

            mGeofencingClient = LocationServices.getGeofencingClient(this);

            mPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            mEdit = mPref.edit();

            fn_permission();
        }
    }

    private void frg() {
        FragmentFavorites fragmentFavorites = new FragmentFavorites();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragmentFavoritesContainer, fragmentFavorites).addToBackStack(null).commit();
    }

    private void fn_permission() {
        if ((ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            if ((!ActivityCompat.shouldShowRequestPermissionRationale(ActivityFavorites.this, Manifest.permission.ACCESS_FINE_LOCATION))) {
                ActivityCompat.requestPermissions(ActivityFavorites.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
            }
        } else {
            boolean_permission = true;
        }
    }

    //Return whether permissions is needed as boolean value.
    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    //Request permission from user
    private void requestPermissions() {
        Log.i(TAG, "Inside requestPermissions function");
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);

        //Log an additional rationale to the user. This would happen if the user denied the
        //request previously, but didn't check the "Don't ask again" checkbox.
        // In case you want, you can also show snackbar. Here, we used Log just to clear the concept.
        if (shouldProvideRationale) {
            Log.i(TAG, "****Inside requestPermissions function when shouldProvideRationale = true");
            startLocationPermissionRequest();
        } else {
            Log.i(TAG, "****Inside requestPermissions function when shouldProvideRationale = false");
            startLocationPermissionRequest();
        }
    }

    //Start the permission request dialog
    private void startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(ActivityFavorites.this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                MY_PERMISSIONS_REQUEST_LOCATION);
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull String[] permissions, @NonNull final int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    boolean_permission = true;
                } else {
                    Toast.makeText(getApplicationContext(), "Please allow the permission", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(mGeofenceList);
        return builder.build();
    }

    public void addGeofencesButtonHandler(View view) {
        if (!checkPermissions()) {
            mPendingGeofenceTask = PendingGeofenceTask.ADD;
            requestPermissions();
            return;
        }

        if (myRadiusGeo != 0) {
            addGeofences();
        }

        if (boolean_permission) {
            if (Objects.requireNonNull(mPref.getString("service", "")).matches("")) {
                mEdit.putString("service", "service").commit();

                Intent intent = new Intent(getApplicationContext(), GoogleService.class);
                startService(intent);
            } else {
                Log.i(TAG, "Service is already running");
            }
        } else {
            Toast.makeText(getApplicationContext(), "Please enable the gps", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressWarnings("MissingPermission")
    private void addGeofences() {
        if (!checkPermissions()) {
            showSnackbar(getString(R.string.insufficient_permissions));
            return;
        }
        mGeofencingClient.addGeofences(getGeofencingRequest(), getGeofencePendingIntent()).addOnCompleteListener(this);
    }

    public void removeGeofencesButtonHandler(View view) {
        if (!checkPermissions()) {
            mPendingGeofenceTask = PendingGeofenceTask.REMOVE;
            requestPermissions();
            return;
        }

        if (myRadiusGeo == 0) {
            Toast.makeText(this, "Radius cannot be equal to 0", Toast.LENGTH_LONG).show();
        } else {
            removeGeofences();
        }

        Intent intent = new Intent(getApplicationContext(), GoogleService.class);
        stopService(intent);
    }

    @SuppressWarnings("MissingPermission")
    private void removeGeofences() {
        if (!checkPermissions()) {
            showSnackbar(getString(R.string.insufficient_permissions));
            return;
        }
        mGeofencingClient.removeGeofences(getGeofencePendingIntent()).addOnCompleteListener(this);
    }

    @Override
    public void onComplete(@NonNull Task<Void> task) {
        mPendingGeofenceTask = PendingGeofenceTask.NONE;
        if (task.isSuccessful()) {
            updateGeofencesAdded(!getGeofencesAdded());
            FragmentMapFavorites.setButtonsEnabledState();
            int messageId = getGeofencesAdded() ? R.string.geofences_added : R.string.geofences_removed;
            Toast.makeText(this, getString(messageId), Toast.LENGTH_SHORT).show();
        } else {
            String errorMessage = GeofenceErrorMessages.getErrorString(this, task.getException());
            Log.w(TAG, errorMessage);
        }
    }

    private PendingIntent getGeofencePendingIntent() {
        if (mGeofencePendingIntent != null) {
            return mGeofencePendingIntent;
        }
        Intent intent = new Intent(this, GeofenceBroadcastReceiver.class);
        mGeofencePendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return mGeofencePendingIntent;
    }

    private void populateGeofenceList(List<PlacesFavorites> placesFavorites) {
        HashMap<String, LatLng> BAY_AREA_LANDMARKS = new HashMap<>();
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
            if (location != null) {
                BAY_AREA_LANDMARKS.put("Hi", new LatLng(location.getLatitude(), location.getLongitude()));
                for (int i = 0; i < placesFavorites.size(); i++) {
                    try {
                        BAY_AREA_LANDMARKS.put(placesFavorites.get(i).getName(), new LatLng(placesFavorites.get(i).getLat(), placesFavorites.get(i).getLng()));
                    } catch (Exception e) {

                    }
                }
            }
        }
        for (Map.Entry<String, LatLng> entry : BAY_AREA_LANDMARKS.entrySet()) {
            mGeofenceList.add(new Geofence.Builder()
                    .setRequestId(entry.getKey())
                    .setCircularRegion(
                            entry.getValue().latitude,
                            entry.getValue().longitude,
                            myRadiusGeo
                    )
                    .setExpirationDuration(Constants.GEOFENCE_EXPIRATION_IN_MILLISECONDS)
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                            Geofence.GEOFENCE_TRANSITION_EXIT)
                    .build());
        }
    }

    private void showSnackbar(final String text) {
        View container = findViewById(android.R.id.content);
        if (container != null) {
            Snackbar.make(container, text, Snackbar.LENGTH_LONG).show();
        }
    }

    public static boolean getGeofencesAdded() {
        return PreferenceManager.getDefaultSharedPreferences(NearByApplication.getApplication()).getBoolean(
                Constants.GEOFENCES_ADDED_KEY, false);
    }

    private void updateGeofencesAdded(boolean added) {
        PreferenceManager.getDefaultSharedPreferences(this)
                .edit()
                .putBoolean(Constants.GEOFENCES_ADDED_KEY, added)
                .apply();
    }

    private void performPendingGeofenceTask() {
        if (mPendingGeofenceTask == PendingGeofenceTask.ADD) {
            addGeofences();
        } else if (mPendingGeofenceTask == PendingGeofenceTask.REMOVE) {
            removeGeofences();
        }
    }

}
