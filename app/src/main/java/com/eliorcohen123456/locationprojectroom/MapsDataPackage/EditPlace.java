package com.eliorcohen123456.locationprojectroom.MapsDataPackage;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.eliorcohen123456.locationprojectroom.RoomFavoritesPackage.PlaceViewModelFavorites;
import com.eliorcohen123456.locationprojectroom.RoomFavoritesPackage.PlacesFavorites;
import com.squareup.picasso.Picasso;

import com.eliorcohen123456.locationprojectroom.R;

public class EditPlace extends AppCompatActivity {

    private PlaceViewModelFavorites placeViewModelFavorites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_place);

        final long id = getIntent().getExtras().getLong(getString(R.string.map_id)); // GetSerializable for the ID
        final PlacesFavorites item = (PlacesFavorites) getIntent().getExtras().getSerializable(getString(R.string.map_edit)); // GetSerializable for the texts

        final EditText name = findViewById(R.id.editText8);  // ID of the name
        final EditText address = findViewById(R.id.editText9);  // ID of the address
        final EditText lat = findViewById(R.id.editText10);  // ID of the lat
        final EditText lng = findViewById(R.id.editText11);  // ID of the lng
        final EditText photo = findViewById(R.id.editText12);  // ID of the photo

        assert item != null;  // If the item of name not null
        name.setText(item.getName());  // GetSerializable of name
        address.setText(item.getAddress());  // GetSerializable of address
        lat.setText(String.valueOf(item.getLat()));  // GetSerializable of lat
        lng.setText(String.valueOf(item.getLng()));  // GetSerializable of lng
        photo.setText(item.getPhoto());  // GetSerializable of photo

        // Button that does the following:
        Button button1 = findViewById(R.id.button18);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name1 = name.getText().toString();  // GetText of the name
                String address1 = address.getText().toString();  // GetText of the address
                String lat1 = lat.getText().toString();  // GetText of the lat
                String lng1 = lng.getText().toString();  // GetText of the lng
                String photo1 = photo.getText().toString();  // GetText of the photo
                double lat2 = Double.parseDouble(lat1);
                double lng2 = Double.parseDouble(lng1);

                PlacesFavorites placesFavorites = new PlacesFavorites(name1, address1, lat2, lng2, photo1);
                placesFavorites.setID(id);
                placeViewModelFavorites = ViewModelProviders.of(EditPlace.this).get(PlaceViewModelFavorites.class);
                placeViewModelFavorites.updatePlace(placesFavorites);

                // Pass from AddMapFromInternet to ActivityFavorites
                Intent intentAddInternetToMain = new Intent(EditPlace.this, ActivityFavorites.class);
                startActivity(intentAddInternetToMain);
            }
        });

        //Initialize the ImageView
        String picture = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="
                + item.getPhoto() +
                "&key=" + getString(R.string.api_key_search);
        final ImageView imageView = findViewById(R.id.imageView3);
        Picasso.get().load(picture).into(imageView);
        imageView.setVisibility(View.INVISIBLE); //Set the ImageView Invisible

        // Button to show the ImageView
        Button button2 = findViewById(R.id.button17);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photo.setVisibility(View.INVISIBLE);  // Canceling the show of URL
                imageView.setVisibility(View.VISIBLE);  // Show the ImageView
            }
        });

        // Button are back to the previous activity
        Button button3 = findViewById(R.id.button19);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

}
