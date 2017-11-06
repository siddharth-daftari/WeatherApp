package com.apoorv.android.weatherapp.helper;

import android.app.Activity;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.apoorv.android.weatherapp.CityListActivity;
import com.apoorv.android.weatherapp.R;
import com.apoorv.android.weatherapp.dummy.DefaultList;
import com.google.android.gms.location.places.Place;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

/**
 * Created by siddharthdaftari on 11/1/17.
 */

public class GetTimeZone {

    public void getTimeDetails(String latitude, String longitude, final String action, final Activity activity, final HashMap<String, Object> extraParams) throws JSONException{

        HashMap returnHashMap = new HashMap<String, String>();
        long timestamp = System.currentTimeMillis() / 1000;

        String urlString = "https://maps.googleapis.com/maps/api/timezone/json?location=" + latitude + ","
                + longitude + "&timestamp=" + timestamp + "&key=" + Secrets.SECRET_FOR_TIMEZONE_API;

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, urlString,null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject responseJson) {
                        try {

                            HashMap returnHashMap = new HashMap<String, String>();

                            System.out.println(Constants.TIMEZONE_API_PROP_DST_OFFSET + ": " + responseJson.getLong(Constants.TIMEZONE_API_PROP_DST_OFFSET));
                            System.out.println(Constants.TIMEZONE_API_PROP_RAW_OFFSET + ": " + responseJson.getLong(Constants.TIMEZONE_API_PROP_RAW_OFFSET));
                            System.out.println(Constants.TIMEZONE_API_PROP_STATUS + ": " + responseJson.getString(Constants.TIMEZONE_API_PROP_STATUS));
                            System.out.println(Constants.TIMEZONE_API_PROP_TIMEZONE_NAME + ": " + responseJson.getString(Constants.TIMEZONE_API_PROP_TIMEZONE_NAME));
                            System.out.println(Constants.TIMEZONE_API_PROP_TIMEZONE_ID + ": " + responseJson.getString(Constants.TIMEZONE_API_PROP_TIMEZONE_ID));

                            //long newTimeStamp = (timestamp + jsonObject.getLong(TIMEZONE_API_PROP_DST_OFFSET) + jsonObject.getLong(TIMEZONE_API_PROP_RAW_OFFSET)) * 1000;

                            DateFormat df = new SimpleDateFormat();
                            df.setTimeZone(TimeZone.getTimeZone(responseJson.getString(Constants.TIMEZONE_API_PROP_TIMEZONE_ID)));
                            Date date = new Date();
                            System.out.println("Time at is: " + new Date(df.format(date)));
                            df.setTimeZone(Calendar.getInstance().getTimeZone());

                            returnHashMap.put(Constants.TIMEZONE_API_PROP_TIMEZONE_ID,responseJson.getString(Constants.TIMEZONE_API_PROP_TIMEZONE_ID));

                            update(action, activity, returnHashMap, extraParams);


                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();

                    }
                });
        RequestClass.getRequestQueue().add(jsObjRequest);
    }

    public void update(String action, Activity activity, HashMap<String, String> returnHashMap, HashMap<String, Object> extraParams){

        switch (action) {
            case Constants.ACTION_UPDATE_CITY_LIST_UI_FOR_TIMEZONE:
                //check if the selected Place exists, by constructing a unique Delimited String
                Place selectedPlace = ((Place)extraParams.get(Constants.SELECTED_PLACE));
                CityListActivity.SimpleItemRecyclerViewAdapter cityListAdapter = ((CityListActivity.SimpleItemRecyclerViewAdapter)extraParams.get(Constants.CITY_LIST_ADAPATER));

                String newCityId = null;
                if(DefaultList.LISTPLACES.size()>0)
                    newCityId = String.valueOf(Integer.parseInt(DefaultList.LISTPLACES.get(DefaultList.LISTPLACES.size()-1).id) + 1 );
                else
                    newCityId = "1";
                DefaultList.CityItem selectedCity = new DefaultList.CityItem(newCityId,selectedPlace.getName().toString(),selectedPlace.getAddress().toString(),String.valueOf(selectedPlace.getLatLng().latitude),String.valueOf(selectedPlace.getLatLng().longitude),Boolean.toString(false),returnHashMap.get(Constants.TIMEZONE_API_PROP_TIMEZONE_ID).toString(),selectedPlace.getId(),"33",null);

                Boolean alreadyExists = false;
                for (DefaultList.CityItem existingCity: DefaultList.LISTPLACES) {
                    if(existingCity.getUniqueDelimitedString().equals(selectedCity.getUniqueDelimitedString()))
                        alreadyExists = true;

                }

                if(!alreadyExists) {
                    DefaultList.citiesset.add(selectedCity.getDelimitedString());
                    DefaultList.writetoSharedInitial(activity.getApplicationContext());
                    DefaultList.addItem(selectedCity);
                    cityListAdapter.notifyDataSetChanged();
                }else{
                    Toast.makeText(activity,"City Already Exists",Toast.LENGTH_SHORT).show();
                }
                break;
        }


    }

    public static HashMap<String, String> getTimeDetailsWithoutApiCall(String timeZone ) throws JSONException {
        HashMap returnHashMap = new HashMap<String, String>();

        if(timeZone!=null && !"".equalsIgnoreCase(timeZone)){

            DateFormat df = new SimpleDateFormat();
            df.setTimeZone(TimeZone.getTimeZone(timeZone));
            Date date = new Date();
            returnHashMap.put(Constants.TIMEZONE_API_CALC_FIELD_DATE,new Date(df.format(date)).toString());
            df.setTimeZone(Calendar.getInstance().getTimeZone());

        }
        return returnHashMap;
    }
}
