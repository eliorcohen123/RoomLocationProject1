package com.eliorcohen123456.locationprojectroom.MainAndOtherPackage;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.eliorcohen123456.locationprojectroom.R;

public class Credits extends AppCompatActivity implements View.OnClickListener {

    private Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.credits);

        initUI();
        initListeners();
    }

    private void initUI() {
        btnBack = findViewById(R.id.btnBack);
    }

    private void initListeners() {
        btnBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack:
                onBackPressed();
                break;
        }
    }

}
