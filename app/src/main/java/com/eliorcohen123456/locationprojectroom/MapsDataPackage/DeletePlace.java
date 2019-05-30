package com.eliorcohen123456.locationprojectroom.MapsDataPackage;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.eliorcohen123456.locationprojectroom.R;

public class DeletePlace extends AppCompatActivity {

    private Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete_place);

        initUI();
        btnBack();
    }

    private void initUI() {
        btnBack = findViewById(R.id.button13);
    }

    private void btnBack() {
        // A button are passes from DeletePlace to Favorites
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

}
