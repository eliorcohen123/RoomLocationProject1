package com.eliorcohen123456.locationprojectroom.MapsDataPackage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.eliorcohen123456.locationprojectroom.R;

public class DeletePlace extends AppCompatActivity implements View.OnClickListener {

    private Button btnOK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete_place);

        initUI();
        initListeners();
    }

    private void initUI() {
        btnOK = findViewById(R.id.btnOK);
    }

    private void initListeners() {
        btnOK.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnOK:
                Intent intent = new Intent(DeletePlace.this, ActivityFavorites.class);
                startActivity(intent);
                break;
        }
    }

}
