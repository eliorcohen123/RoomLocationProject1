package com.eliorcohen123456.locationprojectroom.RoomSearchPackage;

import java.util.ArrayList;

import com.eliorcohen123456.locationprojectroom.DataAppPackage.PlaceModel;

public interface IPlacesDataReceived {

    void onPlacesDataReceived(ArrayList<PlaceModel> results_);
}
