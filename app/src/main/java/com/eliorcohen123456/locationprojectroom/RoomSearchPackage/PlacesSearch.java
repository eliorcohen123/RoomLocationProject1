package com.eliorcohen123456.locationprojectroom.RoomSearchPackage;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

import java.io.Serializable;

@Entity(tableName = "places_table_search")
public class PlacesSearch implements Serializable {

    public PlacesSearch(@NonNull String mName, @NonNull String mAddress, double mLat, double mLng, String mPhoto, boolean mIs_open) {
        this.mName = mName;
        this.mAddress = mAddress;
        this.mLat = mLat;
        this.mLng = mLng;
        this.mPhoto = mPhoto;
        this.mIs_open = mIs_open;
    }

    public PlacesSearch() {

    }

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ID")
    private long ID;

    @ColumnInfo(name = "name")
    private String mName;

    @ColumnInfo(name = "address")
    private String mAddress;

    @ColumnInfo(name = "photo")
    private String mPhoto;

    @ColumnInfo(name = "distance")
    private String mDistance;

    @ColumnInfo(name = "lat")
    private double mLat;

    @ColumnInfo(name = "lng")
    private double mLng;

    @ColumnInfo(name = "rating")
    private double mRating;

    @ColumnInfo(name = "user_ratings_total")
    private int mUser_ratings_total;

    @ColumnInfo(name = "opening_hours")
    private boolean mIs_open;


    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    @NonNull
    public String getName() {
        return mName;
    }

    public void setName(@NonNull String mName) {
        this.mName = mName;
    }

    @NonNull
    public String getAddress() {
        return mAddress;
    }

    public void setAddress(@NonNull String mAddress) {
        this.mAddress = mAddress;
    }

    public String getPhoto() {
        return mPhoto;
    }

    public void setPhoto(String mPhoto) {
        this.mPhoto = mPhoto;
    }

    @NonNull
    public String getDistance() {
        return mDistance;
    }

    public void setDistance(@NonNull String mDistance) {
        this.mDistance = mDistance;
    }

    public double getLat() {
        return mLat;
    }

    public void setLat(double mLat) {
        this.mLat = mLat;
    }

    public double getLng() {
        return mLng;
    }

    public void setLng(double mLng) {
        this.mLng = mLng;
    }

    public double getRating() {
        return mRating;
    }

    public void setRating(double mRating) {
        this.mRating = mRating;
    }

    public int getUser_ratings_total() {
        return mUser_ratings_total;
    }

    public void setUser_ratings_total(int mUser_ratings_total) {
        this.mUser_ratings_total = mUser_ratings_total;
    }

    public boolean getmIs_open() {
        return mIs_open;
    }

    public void setmIs_open(boolean mIs_open) {
        this.mIs_open = mIs_open;
    }

}
