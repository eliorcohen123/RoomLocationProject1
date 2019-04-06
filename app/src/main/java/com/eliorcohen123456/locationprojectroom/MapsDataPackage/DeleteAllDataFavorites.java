package com.eliorcohen123456.locationprojectroom.MapsDataPackage;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.eliorcohen123456.locationprojectroom.R;
import com.eliorcohen123456.locationprojectroom.RoomFavoritesPackage.PlaceViewModelFavorites;
import com.eliorcohen123456.locationprojectroom.MainAndOtherPackage.MainActivity;
import com.eliorcohen123456.locationprojectroom.MainAndOtherPackage.NearByApplication;

public class DeleteAllDataFavorites extends AppCompatActivity {

    private PlaceViewModelFavorites placeViewModelFavorites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete_all_data_favorites);

        // Button are delete all the data of the Favorites
        Button button1 = findViewById(R.id.button3);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                placeViewModelFavorites = new PlaceViewModelFavorites(NearByApplication.getApplication());
                placeViewModelFavorites.deleteAll();

                Toast toast = Toast.makeText(DeleteAllDataFavorites.this, "All the data of favorites are deleted!", Toast.LENGTH_LONG);
                View view = toast.getView();
                view.getBackground().setColorFilter(getResources().getColor(R.color.colorLightBlue), PorterDuff.Mode.SRC_IN);
                TextView text = view.findViewById(android.R.id.message);
                text.setTextColor(getResources().getColor(R.color.colorBrown));
                toast.show();

                Intent intentDeleteAllDataToMain = new Intent(DeleteAllDataFavorites.this, MainActivity.class);
                startActivity(intentDeleteAllDataToMain);
            }
        });

        // Button are back to the previous activity
        Button button2 = findViewById(R.id.button4);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

}
