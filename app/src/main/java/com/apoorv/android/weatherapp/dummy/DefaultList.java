package com.apoorv.android.weatherapp.dummy;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.apoorv.android.weatherapp.CityListActivity;
import com.apoorv.android.weatherapp.helper.Constants;
import com.apoorv.android.weatherapp.helper.GetTimeZone;
import com.apoorv.android.weatherapp.helper.SettingsPreference;
import com.google.android.gms.location.places.Place;

import org.json.JSONException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

import android.os.Handler;
import android.os.Message;
import android.os.Looper;
import android.widget.Toast;

/**
 * Created by apoorv.mehta on 10/28/17.
 */

public class DefaultList {

    public static void writetoSharedInitial(Context c) {
        SharedPreferences sharedPref = c.getSharedPreferences("weathercities",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        Log.i("Apoorv","Writing to database size"+citiesset.size());
       editor.putStringSet("citieslist",citiesset);
        editor.apply();
    }

    public static void readFromSharedInitial (Context c) {
        SharedPreferences sharedPref = c.getSharedPreferences("weathercities",Context.MODE_PRIVATE);
        Set<String> returnedset = sharedPref.getStringSet("citieslist", Collections.EMPTY_SET);
        LISTPLACES.clear();
        for (String s: returnedset) {
            //for each city string in return set, construct a cityItem PoJo and add to the static list.
            String [] citydetails = s.split("@");
            Log.i("Apoorv", "Got a city from database, adding it to the list");
            System.out.println(citydetails[7]);
            CityItem city1 = new CityItem(citydetails[0],citydetails[1],citydetails[2],citydetails[3],citydetails[4],citydetails[5],citydetails[6],citydetails[7]);
            addItem(city1);
            citiesset.add(city1.getDelimitedString());
        }

        //check if we have cities. If not, add the default sity to sharedContext and to the List.
        if(LISTPLACES.size() == 0) {
            Log.i("Apoorv", "No city database, adding default to the list");
            citiesset.add(defaultCities[0]);
            DefaultList.writetoSharedInitial(c);
            String[] citydetails = defaultCities[0].split("@");
            CityItem city1 = new CityItem(citydetails[0],citydetails[1],citydetails[2],citydetails[3],citydetails[4],citydetails[5],citydetails[6],citydetails[7]);
            addItem(city1);
        }

    }

    public boolean addCity (Place selectedPlace, Context c) throws JSONException {
        if(selectedPlace!=null) {
            HashMap timeDetails = null;

            for(CityItem city : LISTPLACES)
            {
                Log.i("Apoorv list",city.getDelimitedString());
            }

            timeDetails = GetTimeZone.getTimeDetails(String.valueOf(selectedPlace.getLatLng().latitude),String.valueOf(selectedPlace.getLatLng().longitude));
            //timeDetails = mt.getTimeDetails();


            //check if the selected Place exists, by constructing a unique Delimited String
            String newCityId = String.valueOf(Integer.parseInt(LISTPLACES.get(LISTPLACES.size()-1).id) + 1 );
            CityItem selectedCity = new CityItem(newCityId,selectedPlace.getName().toString(),selectedPlace.getAddress().toString(),String.valueOf(selectedPlace.getLatLng().latitude),String.valueOf(selectedPlace.getLatLng().longitude),Boolean.toString(false),timeDetails.get(Constants.TIMEZONE_API_PROP_TIMEZONE_ID).toString(),selectedPlace.getId());
            citiesset.add(selectedCity.getDelimitedString());
            DefaultList.writetoSharedInitial(c);
            addItem(selectedCity);

            return true;
        }
        return false;
    }

    public static String[] defaultCities = {"1@San Jose@CA, United States@37.338208@-121.886329@true@America/Los_Angeles@sj123","2@Mumbai@Maharashtra, India@19.075984@72.877656@false@Asia/Calcutta"};
    public static Set<String> citiesset = new HashSet<String>();

    public static final List<CityItem> LISTPLACES = new ArrayList<>();




    private static void addItem(DefaultList.CityItem item) {
        LISTPLACES.add(item);
    }

    public static void deleteCity(int position, Context context) {

        CityItem cityToBeRemoved = LISTPLACES.get(position);
        Log.i("Apoorv", "Will delete from Database"+cityToBeRemoved.getDelimitedString());
        Log.i("Apoorv","Set size before delete"+citiesset.size());
        citiesset.remove(cityToBeRemoved.getDelimitedString());
        Log.i("Apoorv","Set size after delete"+citiesset.size());
        writetoSharedInitial(context);
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
    public static class CityItem implements  Comparable<CityItem> {
        public final String id;
        public final String name;
        public final String description;
        public final String latitude;
        public final String longitude;
        public boolean isCurrent;
        public String timeZone;
        public String cityId;

        public CityItem(String id, String name, String description, String latitude, String longitude, String isCurrent, String timeZone, String cityId) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.latitude = latitude;
            this.longitude = longitude;
            this.isCurrent =  Boolean.parseBoolean(isCurrent);
            this.timeZone = timeZone;
            this.cityId = cityId;
        }

        @Override
        public String toString() {


            return name+", "+getListViewString(name,timeZone);
        }

        public String getListViewString(String name, String timeZoneid) {
            SimpleDateFormat df = new SimpleDateFormat();
            df.setTimeZone(TimeZone.getTimeZone(timeZoneid));
            Date date = new Date();
            Log.i("Apoorv","Time at " +name+" is: " + new Date(df.format(date)));
            df.applyPattern("h:mm a");
            return df.format(date);
        }

        public String getDelimitedString() {
            return name+"@"+description+"@"+latitude+"@"+longitude+"@"+isCurrent+"@"+timeZone+"@"+cityId;
        }

        @Override
        public int compareTo(CityItem newcity) {
            return Integer.parseInt(this.id) - Integer.parseInt(newcity.id);
        }


    }

}
