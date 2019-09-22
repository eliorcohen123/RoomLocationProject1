package com.eliorcohen123456.locationprojectroom.CustomAdapterPackage;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

import com.eliorcohen123456.locationprojectroom.MapsDataPackage.FragmentMapFavorites;
import com.eliorcohen123456.locationprojectroom.R;
import com.eliorcohen123456.locationprojectroom.RoomFavoritesPackage.PlacesFavorites;

public class PlacesListAdapterFavorites extends RecyclerView.Adapter<PlacesListAdapterFavorites.PlaceViewHolder> {

    class PlaceViewHolder extends RecyclerView.ViewHolder {

        private TextView name3, address3, kmMe3;
        private ImageView image3;
        private RelativeLayout relativeLayout3;

        private PlaceViewHolder(View itemView) {
            super(itemView);
            name3 = itemView.findViewById(R.id.name1);
            address3 = itemView.findViewById(R.id.address1);
            kmMe3 = itemView.findViewById(R.id.kmMe1);
            image3 = itemView.findViewById(R.id.image1);
            relativeLayout3 = itemView.findViewById(R.id.relative1);
        }
    }

    private final LayoutInflater mInflater;
    private List<PlacesFavorites> mPlacesFavoritesList;
    private Location location;
    private LocationManager locationManager;
    private Criteria criteria;
    private String provider;

    public PlacesListAdapterFavorites(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public PlaceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_item_total, parent, false);
        return new PlaceViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final PlaceViewHolder holder, final int position) {
        if (mPlacesFavoritesList != null) {
            final PlacesFavorites current = mPlacesFavoritesList.get(position);
            locationManager = (LocationManager) mInflater.getContext().getSystemService(Context.LOCATION_SERVICE);
            criteria = new Criteria();
            provider = locationManager.getBestProvider(criteria, true);
            if (ActivityCompat.checkSelfPermission(mInflater.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.checkSelfPermission(mInflater.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION);
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
                    holder.name3.setText(current.getName());
                    holder.address3.setText(current.getAddress());
                    double distanceMe;
                    Location locationA = new Location("Point A");
                    locationA.setLatitude(current.getLat());
                    locationA.setLongitude(current.getLng());
                    Location locationB = new Location("Point B");
                    locationB.setLatitude(location.getLatitude());
                    locationB.setLongitude(location.getLongitude());
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mInflater.getContext());
                    String result = prefs.getString("myKm", "1000.0");
                    assert result != null;
                    double val = Double.parseDouble(result);
                    distanceMe = locationA.distanceTo(locationB) / val;   // in km
                    String distanceKm1;
                    String disMile;
                    if (val == 1000.0) {
                        if (distanceMe < 1) {
                            int dis = (int) (distanceMe * 1000);
                            distanceKm1 = "\n" + "Meters: " + String.valueOf(dis);
                            holder.kmMe3.setText(distanceKm1);
                        } else if (distanceMe >= 1) {
                            String disM = String.format("%.2f", distanceMe);
                            distanceKm1 = "\n" + "Km: " + String.valueOf(disM);
                            // Put the text in kmMe3
                            holder.kmMe3.setText(distanceKm1);
                        }
                    } else if (val == 1609.344) {
                        String distanceMile1 = String.format("%.2f", distanceMe);
                        disMile = "\n" + "Miles: " + String.valueOf(distanceMile1);
                        // Put the text in kmMe3
                        holder.kmMe3.setText(disMile);
                    }
                    if (!current.getPhoto().equals("")) {
                        Picasso.get().load("https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="
                                + current.getPhoto() +
                                "&key=" + mInflater.getContext().getString(R.string.api_key_search)).into(holder.image3);
                    } else {
                        holder.image3.setImageResource(R.drawable.no_image_available);
                    }
                }
            }
            holder.relativeLayout3.setOnClickListener(v -> {
                FragmentMapFavorites fragmentMapFavorites = new FragmentMapFavorites();
                Bundle bundle = new Bundle();
                bundle.putSerializable(mInflater.getContext().getString(R.string.map_favorites_key), current);
                fragmentMapFavorites.setArguments(bundle);
                FragmentManager fragmentManager = ((AppCompatActivity) mInflater.getContext()).getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.fragmentFavoritesContainer, fragmentMapFavorites).addToBackStack(null).commit();
            });

            setFadeAnimation(holder.itemView);
        } else {
            // Covers the case of data not being ready yet.
            holder.name3.setText("No Places");
        }
    }

    public void setPlaces(List<PlacesFavorites> placesFavorites) {
        mPlacesFavoritesList = placesFavorites;
        try {
            Collections.sort(mPlacesFavoritesList, (obj1, obj2) -> {
                // ## Ascending order
//                return obj1.getDistance().compareToIgnoreCase(obj2.getDistance()); // To compare string values
                return Double.compare(Math.sqrt(Math.pow(obj1.getLat() - location.getLatitude(), 2) + Math.pow(obj1.getLng() - location.getLongitude(), 2)),
                        Math.sqrt(Math.pow(obj2.getLat() - location.getLatitude(), 2) + Math.pow(obj2.getLng() - location.getLongitude(), 2))); // To compare integer values

                // ## Descending order
                // return obj2.getCompanyName().compareToIgnoreCase(obj1.getCompanyName()); // To compare string values
                // return Integer.valueOf(obj2.getId()).compareTo(obj1.getId()); // To compare integer values
            });
        } catch (Exception e) {

        }
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // mPlacesSearchList has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (mPlacesFavoritesList != null)
            return mPlacesFavoritesList.size();
        else return 0;
    }

    public PlacesFavorites getPlaceAtPosition(int position) {
        return mPlacesFavoritesList.get(position);
    }

    private void setFadeAnimation(View view) {
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(1500);
        view.startAnimation(anim);
    }

}
