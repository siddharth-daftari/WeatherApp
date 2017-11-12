package com.apoorv.android.weatherapp.model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by siddharthdaftari on 11/2/17.
 */

public class CityDetailModel {
    private String cityName;
    private String weatherIcon;
    private String weatherStatus;
    private Double temp;
    private Double tempHigh;
    private Double tempLow;
    private int index;
    private String dateValue;
    private Date dateInProperFormat;

    public String getDateValue() {
        return dateValue;
    }
    public void setDateValue(String dateValue, String timezone) throws ParseException {
        this.dateValue = dateValue;
        this.dateInProperFormat = getTimeInLocal(dateValue, timezone);
    }

    public Date getTimeInLocal(String date, String timezone) throws ParseException {
        //SimpleDateFormat df = new SimpleDateFormat("h:mm a");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date dateTemp = df.parse(date);

        DateFormat df1 = new SimpleDateFormat();
        df1.setTimeZone(TimeZone.getTimeZone(timezone));
        dateTemp = new Date(df1.format(dateTemp));

        df1.setTimeZone(Calendar.getInstance().getTimeZone());

        return dateTemp;
    }

    public Date getDateInProperFormat() {
        return this.dateInProperFormat;
    }
    public String getWeatherIcon() {
        return weatherIcon;
    }
    public void setWeatherIcon(String weatherIcon) {
        this.weatherIcon = weatherIcon;
    }
    public String getCityName() {
        return cityName;
    }
    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
    public String getWeatherStatus() {
        return weatherStatus;
    }
    public void setWeatherStatus(String weatherStatus) {
        this.weatherStatus = weatherStatus;
    }
    public int getTemp() {
        return (int)Math.round(temp);
    }
    public Double getTempInDouble() {
        return temp;
    }
    public void setTemp(Double temp) {
        this.temp = temp;
    }
    public int getTempHigh() {
        return (int)Math.round(tempHigh);
    }
    public Double getTempHighInDouble() {
        return tempHigh;
    }
    public void setTempHigh(Double tempHigh) {
        this.tempHigh = tempHigh;
    }
    public int getTempLow() {
        return (int)Math.round(tempLow);
    }
    public Double getTempLowInDouble() {
        return tempLow;
    }
    public void setTempLow(Double tempLow) {
        this.tempLow = tempLow;
    }
    public int getIndex() {
        return index;
    }
    public void setIndex(int index) {
        this.index = index;
    }

    public void printValues(){
        System.out.println("cityName: " + cityName
                + "  weatherStatus: " + weatherStatus
                + "  temp: " + temp
                + "  tempHigh: " + tempHigh
                + "  tempLow: " + tempLow
                + "  index: " + index
                + "  dateValue: " + dateValue
                + "  dateInProperFormat: " + dateInProperFormat);
    }

}

