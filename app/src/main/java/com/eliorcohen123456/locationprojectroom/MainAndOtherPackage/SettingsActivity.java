package com.eliorcohen123456.locationprojectroom.MainAndOtherPackage;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.eliorcohen123456.locationprojectroom.R;

// Activity of Setting of distance
public class SettingsActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings_xml);

    }

}
