package com.eliorcohen123456.locationprojectroom.MapsDataPackage;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.eliorcohen123456.locationprojectroom.R;

// Activity of FragmentFavorites
public class ActivityFavorites extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        frg();
    }

    private void frg() {
        FragmentFavorites fragmentFavorites = new FragmentFavorites();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentFavoritesContainer, fragmentFavorites);
        fragmentTransaction.commit();
    }

}
