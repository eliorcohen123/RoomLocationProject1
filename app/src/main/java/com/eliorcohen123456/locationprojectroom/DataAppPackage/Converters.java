//package com.eliorcohen123456.locationprojectroom.DataAppPackage;
//
//import androidx.room.TypeConverter;
//
//import com.eliorcohen123456.locationprojectroom.DataAppPackage.Photos;
//import com.eliorcohen123456.locationprojectroom.RoomSearchPackage.PlacesSearch;
//import com.google.gson.Gson;
//import com.google.gson.reflect.TypeToken;
//
//import java.lang.reflect.Type;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//
//public class Converters {
//
//    @TypeConverter
//    public static List<Photos> fromString(String value) {
//        Type listType = new TypeToken<List<Photos>>() {}.getType();
//        return new Gson().fromJson(value, listType);
//    }
//
//    @TypeConverter
//    public static String fromList(List<Photos> list) {
//        Gson gson = new Gson();
//        String json = gson.toJson(list);
//        return json;
//    }
//
//    @TypeConverter
//    public static Geometry fromString2(String value) {
//        Type listType = new TypeToken<List<Geometry>>() {}.getType();
//        return new Gson().fromJson(value, listType);
//    }
//
//    @TypeConverter
//    public static String fromList2(Geometry list) {
//        Gson gson = new Gson();
//        String json = gson.toJson(list);
//        return json;
//    }
//
//    @TypeConverter
//    public static OpeningHours fromString3(String value) {
//        Type listType = new TypeToken<List<OpeningHours>>() {}.getType();
//        return new Gson().fromJson(value, listType);
//    }
//
//    @TypeConverter
//    public static String fromList3(OpeningHours list) {
//        Gson gson = new Gson();
//        String json = gson.toJson(list);
//        return json;
//    }
//
//}
