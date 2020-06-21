package com.eliorcohen12345.locationproject.CustomAdapterPackage;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.eliorcohen12345.locationproject.MapsDataPackage.FragmentMapSearch;
import com.eliorcohen12345.locationproject.R;
import com.eliorcohen12345.locationproject.RoomFavoritesPackage.PlaceViewModelFavorites;
import com.eliorcohen12345.locationproject.RoomSearchPackage.PlacesSearch;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.Collections;
import java.util.List;

public class PlacesListAdapterSearch extends RecyclerView.Adapter<PlacesListAdapterSearch.PlaceViewHolder> {

    class PlaceViewHolder extends RecyclerView.ViewHolder {

        private TextView name1, address1, kmMe1, isOpen1;
        private LinearLayout linear1;
        private PlaceViewModelFavorites placeViewModelFavorites;

        private PlaceViewHolder(View itemView) {
            super(itemView);
            name1 = itemView.findViewById(R.id.name1);
            address1 = itemView.findViewById(R.id.address1);
            kmMe1 = itemView.findViewById(R.id.kmMe1);
            isOpen1 = itemView.findViewById(R.id.isOpen1);
            linear1 = itemView.findViewById(R.id.linear1);
        }
    }

    private final LayoutInflater mInflater;
    private List<PlacesSearch> mPlacesSearchList;
    private double diagonalInches;
    private Location location;
    private LocationManager locationManager;
    private Criteria criteria;
    private String provider;

    public PlacesListAdapterSearch(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public PlaceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_item_total, parent, false);
        return new PlaceViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final PlaceViewHolder holder, final int position) {
        if (mPlacesSearchList != null) {
            final PlacesSearch current = mPlacesSearchList.get(position);
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
                    holder.name1.setText(current.getName());
                    holder.address1.setText(current.getAddress());
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
                            holder.kmMe1.setText(distanceKm1);
                        } else if (distanceMe >= 1) {
                            String disM = String.format("%.2f", distanceMe);
                            distanceKm1 = "\n" + "Km: " + String.valueOf(disM);
                            // Put the text in kmMe1
                            holder.kmMe1.setText(distanceKm1);
                        }
                    } else if (val == 1609.344) {
                        String distanceMile1 = String.format("%.2f", distanceMe);
                        disMile = "\n" + "Miles: " + String.valueOf(distanceMile1);
                        // Put the text in kmMe1
                        holder.kmMe1.setText(disMile);
                    }
                    try {
                        if (String.valueOf(current.getIs_open()).equals("true")) {
                            holder.isOpen1.setText("Open");
                        } else if (String.valueOf(current.getIs_open()).equals("false")) {
                            holder.isOpen1.setText("Close");
                        } else {
                            holder.isOpen1.setText("No info");
                        }
                    } catch (Exception e) {

                    }
                    try {
                        Picasso.get().load("https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="
                                + current.getPhoto() +
                                "&key=" + mInflater.getContext().getString(R.string.api_key_search)).into(new Target() {
                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                holder.linear1.setBackground(new BitmapDrawable(bitmap));
                            }

                            @Override
                            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                                holder.linear1.setBackgroundResource(R.drawable.no_image_available);
                            }

                            @Override
                            public void onPrepareLoad(Drawable placeHolderDrawable) {
                                holder.linear1.setBackgroundResource(R.drawable.no_image_available);
                            }
                        });
                    } catch (Exception e) {
                        holder.linear1.setBackgroundResource(R.drawable.no_image_available);
                    }
                }
            }
            holder.linear1.setOnClickListener(v -> {
                // Tablet/Phone mode
                DisplayMetrics metrics = new DisplayMetrics();
                ((AppCompatActivity) mInflater.getContext()).getWindowManager().getDefaultDisplay().getMetrics(metrics);

                float yInches = metrics.heightPixels / metrics.ydpi;
                float xInches = metrics.widthPixels / metrics.xdpi;
                diagonalInches = Math.sqrt(xInches * xInches + yInches * yInches);

                FragmentMapSearch fragmentMapSearch = new FragmentMapSearch();
                Bundle bundle = new Bundle();
                bundle.putSerializable(mInflater.getContext().getString(R.string.map_search_key), current);
                fragmentMapSearch.setArguments(bundle);
                FragmentManager fragmentManager = ((AppCompatActivity) mInflater.getContext()).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                if (diagonalInches >= 6.5) {
                    fragmentTransaction.replace(R.id.fragmentLt, fragmentMapSearch);
                } else {
                    fragmentTransaction.replace(R.id.fragmentContainer, fragmentMapSearch);
                }
                fragmentTransaction.addToBackStack(null).commit();
            });
        } else {
            // Covers the case of data not being ready yet.
            holder.name1.setText("No Places");
        }
    }

    public void setPlaces(List<PlacesSearch> placesSearches) {
        mPlacesSearchList = placesSearches;
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
                Collections.sort(mPlacesSearchList, (obj1, obj2) -> {
                    // ## Ascending order
//                return obj1.getDistance().compareToIgnoreCase(obj2.getDistance()); // To compare string values
                    return Double.compare(Math.sqrt(Math.pow(obj1.getLat() - location.getLatitude(), 2) + Math.pow(obj1.getLng() - location.getLongitude(), 2)),
                            Math.sqrt(Math.pow(obj2.getLat() - location.getLatitude(), 2) + Math.pow(obj2.getLng() - location.getLongitude(), 2))); // To compare integer values

                    // ## Descending order
                    // return obj2.getCompanyName().compareToIgnoreCase(obj1.getCompanyName()); // To compare string values
                    // return Integer.valueOf(obj2.getId()).compareTo(obj1.getId()); // To compare integer values
                });
            }
        }
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // mPlacesSearchList has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (mPlacesSearchList != null)
            return mPlacesSearchList.size();
        else return 0;
    }

    public PlacesSearch getPlaceAtPosition(int position) {
        return mPlacesSearchList.get(position);
    }

}
