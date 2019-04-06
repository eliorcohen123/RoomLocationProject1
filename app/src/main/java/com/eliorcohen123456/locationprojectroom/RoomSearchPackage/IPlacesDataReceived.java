package com.eliorcohen123456.locationprojectroom.RoomSearchPackage;

import java.util.ArrayList;

import com.eliorcohen123456.locationprojectroom.DataAppPackage.LocationModel;

public interface IPlacesDataReceived {

    void onPlacesDataReceived(ArrayList<LocationModel> results_);
}
