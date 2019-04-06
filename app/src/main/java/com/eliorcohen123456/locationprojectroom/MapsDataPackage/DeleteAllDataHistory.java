package com.eliorcohen123456.locationprojectroom.MapsDataPackage;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.eliorcohen123456.locationprojectroom.MainAndOtherPackage.MainActivity;
import com.eliorcohen123456.locationprojectroom.MainAndOtherPackage.NearByApplication;
import com.eliorcohen123456.locationprojectroom.R;
import com.eliorcohen123456.locationprojectroom.RoomSearchPackage.PlaceViewModelSearch;

public class DeleteAllDataHistory extends AppCompatActivity {

    private PlaceViewModelSearch placeViewModelSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete_all_data_history);

        // Button are delete all the data of the app
        Button button1 = findViewById(R.id.button3);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                placeViewModelSearch = new PlaceViewModelSearch(NearByApplication.getApplication());
                placeViewModelSearch.deleteAll();

                Toast toast = Toast.makeText(DeleteAllDataHistory.this, "All the data of history are deleted!", Toast.LENGTH_LONG);
                View view = toast.getView();
                view.getBackground().setColorFilter(getResources().getColor(R.color.colorLightBlue), PorterDuff.Mode.SRC_IN);
                TextView text = view.findViewById(android.R.id.message);
                text.setTextColor(getResources().getColor(R.color.colorBrown));
                toast.show();

                Intent intentDeleteAllDataToMain = new Intent(DeleteAllDataHistory.this, MainActivity.class);
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
