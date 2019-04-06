package com.eliorcohen123456.locationprojectroom.CustomAdapterPackage;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import com.eliorcohen123456.locationprojectroom.R;
import com.eliorcohen123456.locationprojectroom.RoomSearchPackage.PlacesSearch;

public class CustomInfoWindowGoogleMapSearch implements GoogleMap.InfoWindowAdapter {

    private Context context;

    public CustomInfoWindowGoogleMapSearch(Context ctx) {
        context = ctx;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = ((Activity) context).getLayoutInflater()
                .inflate(R.layout.map_custom_infowindow, null);

        TextView name = view.findViewById(R.id.nameInfo);

//        ImageView img = view.findViewById(R.id.pic);

        TextView address = view.findViewById(R.id.addressInfo);
        TextView rating = view.findViewById(R.id.ratingInfo);
        TextView ratingQua = view.findViewById(R.id.ratingQuantityInfo);
        TextView distance = view.findViewById(R.id.distanceInfo);

        name.setText(marker.getTitle());

        PlacesSearch infoWindowData = (PlacesSearch) marker.getTag();

//        int imageId = context.getResources().getIdentifier(infoWindowData.getPhoto_reference(),
//                "drawable", context.getPackageName());
//        img.setImageResource(imageId);

        try {
            address.setText(infoWindowData.getAddress());
            rating.setText("Rating: " + String.valueOf(infoWindowData.getRating()));
            ratingQua.setText("User ratings total: " + String.valueOf(infoWindowData.getUser_ratings_total()));
            distance.setText(infoWindowData.getDistance());
        } catch (Exception e) {

        }

        return view;
    }

}
