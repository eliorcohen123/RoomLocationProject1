package com.eliorcohen12345.locationproject.RoomSearchPackage;

import java.util.ArrayList;

import com.eliorcohen12345.locationproject.DataAppPackage.PlaceModel;

public interface IPlacesDataReceived {

    void onPlacesDataReceived(ArrayList<PlaceModel> results_);
}
