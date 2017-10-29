package com.apoorv.android.weatherapp.dummy;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by apoorv.mehta on 10/28/17.
 */

public class DefaultList {

    public static void receiveContext(Context c) {
        SharedPreferences sharedPref = c.getSharedPreferences("temp",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
       editor.putStringSet("citieslist",citiesset);
        editor.apply();
    }

    public static void readFromSharedInitial (Context c) {
        SharedPreferences sharedPref = c.getSharedPreferences("weathercities",Context.MODE_PRIVATE);
        Set<String> returnedset = sharedPref.getStringSet("citieslist", Collections.EMPTY_SET);
        for (String s: returnedset) {
            //for each city string in return set, construct a cityItem PoJo and add to the static list.
            String [] citydetails = s.split("@");
            CityItem city1 = new CityItem(citydetails[0],citydetails[1],citydetails[2],citydetails[3],citydetails[4],citydetails[5]);
            addItem(city1);
        }

        //check if we have cities. If not, add the default sity to sharedContext and to the List.
        if(LISTPLACES.size() == 0) {


        }


    }

    public static String[] defaultCities = {"1@San Jose@CA, United States@37.338208@-121.886329@true","2@Mumbai@Maharashtra, India@19.075984@72.877656@false"};
    public static Set<String> citiesset = new HashSet<String>();

    public static final List<CityItem> LISTPLACES = new ArrayList<>();




    private static void addItem(DefaultList.CityItem item) {
        LISTPLACES.add(item);
    }

//    static {
//        citiesset.add(defaultCities[0]);
//        citiesset.add(defaultCities[1]);
//        String[] citydetails = defaultCities[0].split("@");
//        CityItem city1 = new CityItem(citydetails[0],citydetails[1],citydetails[2],citydetails[3],citydetails[4],citydetails[5]);
//        String[] citydetails2 = defaultCities[1].split("@");
//        CityItem city2 = new CityItem(citydetails2[0],citydetails2[1],citydetails2[2],citydetails2[3],citydetails2[4],citydetails2[5]);
//        addItem(city1);
//        addItem(city2);
//    }


    /**
     * A City item Pojo representing a piece of content.
     */
    public static class CityItem {
        public final String id;
        public final String name;
        public final String description;
        public final String latitude;
        public final String longitude;
        public boolean isCurrent;

        public CityItem(String id, String name, String description, String latitude, String longitude, String isCurrent) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.latitude = latitude;
            this.longitude = longitude;
            this.isCurrent =  Boolean.parseBoolean(isCurrent);
        }

        @Override
        public String toString() {
            return name+", "+description;
        }

        public String getDelimitedString() {
            return id+"@"+name+"@"+description+"@"+latitude+"@"+longitude+"@"+isCurrent;
        }

    }

}
