package com.eliorcohen123456.locationprojectroom.MapsDataPackage;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.eliorcohen123456.locationprojectroom.MainAndOtherPackage.NearByApplication;
import com.eliorcohen123456.locationprojectroom.RoomFavoritesPackage.PlaceViewModelFavorites;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.eliorcohen123456.locationprojectroom.R;

public class CustomMarkerFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mGoogleMap;
    private MapView mMapView;
    private View mView;
    private Location location;
    private Criteria criteria;
    private LocationManager locationManager;
    private String provider;
    private Toolbar toolbar;
    private PlaceViewModelFavorites placeViewModelFavorites;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_custom_layout, container, false);

        initUI();
        initLocation();
        myUI();

        return mView;
    }

    private void initUI() {
        toolbar = mView.findViewById(R.id.toolbar);
    }

    private void initLocation() {
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, true);
    }

    private void myUI() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        mView.findViewById(R.id.myButton).setOnClickListener(v -> getActivity().onBackPressed());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMapView = view.findViewById(R.id.mapCustom);
        if (mMapView != null) {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            MapsInitializer.initialize(getContext());
            mGoogleMap = googleMap;
            addCustomMarker();
            googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setZoomControlsEnabled(true);
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION);
            }// TODO: Consider calling
//    ActivityCompat#requestPermissions
// here to request the missing permissions, and then overriding
//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                                          int[] grantResults)
// to handle the case where the user grants the permission. See the documentation
// for ActivityCompat#requestPermissions for more details.
            if (provider != null) {
                if (location != null) {
                    location = locationManager.getLastKnownLocation(provider);
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 8));
                }
            }
        } catch (Exception e) {

        }
    }

    // Add marker to the map
    private void addCustomMarker() {
        mGoogleMap.setOnMapLongClickListener(point -> {
            mGoogleMap.addMarker(new MarkerOptions().position(point).title("Custom location").icon(BitmapDescriptorFactory
                    .defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
            Toast.makeText(getContext(), point.latitude + " : " + point.longitude, Toast.LENGTH_SHORT).show();
        });

        mGoogleMap.setOnMarkerClickListener(marker -> {
            placeViewModelFavorites = new PlaceViewModelFavorites(NearByApplication.getApplication());
            if (placeViewModelFavorites.exist("", marker.getPosition().latitude, marker.getPosition().longitude) == null) {
                Intent intent = new Intent(getContext(), AddMarkerFavorites.class);
                intent.putExtra(getString(R.string.lat_marker), marker.getPosition().latitude);
                intent.putExtra(getString(R.string.lng_marker), marker.getPosition().longitude);
                startActivity(intent);
            } else {
                Toast.makeText(getContext(), "Current place already exist in your favorites", Toast.LENGTH_SHORT).show();
            }
            return true;
        });
    }

}
