package com.eliorcohen123456.locationprojectroom.RoomFavoritesPackage;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.io.Serializable;

@Entity(tableName = "places_table_favorites")
public class PlacesFavorites implements Serializable {

    public PlacesFavorites(@NonNull String name1, @NonNull String address1, double lat2, double lng2, String photo1) {
        this.mName = name1;
        this.mAddress = address1;
        this.mLat = lat2;
        this.mLng = lng2;
        this.mPhoto = photo1;
    }

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "ID")
    private long ID;

    @NonNull
    @ColumnInfo(name = "name")
    private String mName;

    @NonNull
    @ColumnInfo(name = "lat")
    private double mLat;

    @NonNull
    @ColumnInfo(name = "lng")
    private double mLng;

    @NonNull
    @ColumnInfo(name = "address")
    private String mAddress;

    @ColumnInfo(name = "photo")
    private String mPhoto;

    @ColumnInfo(name = "isLastSearch")
    private boolean mIsLastSearch;

    @ColumnInfo(name = "isFavorite")
    private boolean mIsFavorite;

    @ColumnInfo(name = "distance")
    private String mDistance;

    @ColumnInfo(name = "rating")
    private double mRating;

    @ColumnInfo(name = "user_ratings_total")
    private int mUser_ratings_total;


    public PlacesFavorites() {

    }


    @NonNull
    public long getID() {
        return ID;
    }

    public void setID(@NonNull long ID) {
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
    public double getLat() {
        return mLat;
    }

    public void setLat(@NonNull double mLat) {
        this.mLat = mLat;
    }

    @NonNull
    public double getLng() {
        return mLng;
    }

    public void setLng(@NonNull double mLng) {
        this.mLng = mLng;
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

    @NonNull
    public String getDistance() {
        return mDistance;
    }

    public void setDistance(@NonNull String mDistance) {
        this.mDistance = mDistance;
    }

    @NonNull
    public double getRating() {
        return mRating;
    }

    public void setRating(@NonNull double mRating) {
        this.mRating = mRating;
    }

    @NonNull
    public int getUser_ratings_total() {
        return mUser_ratings_total;
    }

    public void setUser_ratings_total(@NonNull int mUser_ratings_total) {
        this.mUser_ratings_total = mUser_ratings_total;
    }

}
