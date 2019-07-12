package com.eliorcohen123456.locationprojectroom.RoomSearchPackage;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.io.Serializable;

@Entity(tableName = "places_table_search")
public class PlacesSearch implements Serializable {

    public PlacesSearch(@NonNull String mName, double mLat, double mLng, @NonNull String mAddress, String mPhoto, boolean mIsLastSearch, boolean mIsFavorite, String mDistance, double mRating, int mUser_ratings_total) {
        this.mName = mName;
        this.mLat = mLat;
        this.mLng = mLng;
        this.mAddress = mAddress;
        this.mPhoto = mPhoto;
        this.mIsLastSearch = mIsLastSearch;
        this.mIsFavorite = mIsFavorite;
        this.mDistance = mDistance;
        this.mRating = mRating;
        this.mUser_ratings_total = mUser_ratings_total;
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

    @ColumnInfo(name = "isLastSearch")
    private boolean mIsLastSearch;

    @ColumnInfo(name = "isFavorite")
    private boolean mIsFavorite;


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

    public boolean isIsLastSearch() {
        return mIsLastSearch;
    }

    public void setIsLastSearch(boolean mIsLastSearch) {
        this.mIsLastSearch = mIsLastSearch;
    }

    public boolean isIsFavorite() {
        return mIsFavorite;
    }

    public void setIsFavorite(boolean mIsFavorite) {
        this.mIsFavorite = mIsFavorite;
    }

}
