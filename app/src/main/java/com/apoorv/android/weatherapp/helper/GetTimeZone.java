package com.apoorv.android.weatherapp.helper;

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

    public static HashMap<String, String> getTimeDetails(String latitude, String longitude) throws JSONException {

        HashMap returnHashMap = new HashMap<String, String>();
        long timestamp = System.currentTimeMillis() / 1000;

        JSONObject responseJson = CallApi.callApi("https://maps.googleapis.com/maps/api/timezone/json?location=" + latitude + ","
						+ longitude + "&timestamp=" + timestamp + "&key=" + Secrets.SECRET_FOR_TIMEZONE_API);

        System.out.println(Constants.TIMEZONE_API_PROP_DST_OFFSET + ": " + responseJson.getLong(Constants.TIMEZONE_API_PROP_DST_OFFSET));
        System.out.println(Constants.TIMEZONE_API_PROP_RAW_OFFSET + ": " + responseJson.getLong(Constants.TIMEZONE_API_PROP_RAW_OFFSET));
        System.out.println(Constants.TIMEZONE_API_PROP_STATUS + ": " + responseJson.getString(Constants.TIMEZONE_API_PROP_STATUS));
        System.out.println(Constants.TIMEZONE_API_PROP_TIMEZONE_NAME + ": " + responseJson.getString(Constants.TIMEZONE_API_PROP_TIMEZONE_NAME));
        System.out.println(Constants.TIMEZONE_API_PROP_TIMEZONE_ID + ": " + responseJson.getString(Constants.TIMEZONE_API_PROP_TIMEZONE_ID));

        //long newTimeStamp = (timestamp + jsonObject.getLong(TIMEZONE_API_PROP_DST_OFFSET) + jsonObject.getLong(TIMEZONE_API_PROP_RAW_OFFSET)) * 1000;

        DateFormat df = new SimpleDateFormat();
        df.setTimeZone(TimeZone.getTimeZone(responseJson.getString(Constants.TIMEZONE_API_PROP_TIMEZONE_ID)));
        Date date = new Date();
        System.out.println("Time at " + latitude + "," + longitude + " is: " + new Date(df.format(date)));
        df.setTimeZone(Calendar.getInstance().getTimeZone());

        returnHashMap.put(Constants.TIMEZONE_API_PROP_TIMEZONE_ID,responseJson.getString(Constants.TIMEZONE_API_PROP_TIMEZONE_ID));


        return returnHashMap;
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
