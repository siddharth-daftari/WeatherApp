package com.apoorv.android.weatherapp.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by siddharthdaftari on 11/2/17.
 */

public class CityDetailModel {
    private String cityName;
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
    public void setDateValue(String dateValue) throws ParseException {
        this.dateValue = dateValue;
        dateInProperFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateValue);

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
    public Double getTemp() {
        return temp;
    }
    public void setTemp(Double temp) {
        this.temp = temp;
    }
    public Double getTempHigh() {
        return tempHigh;
    }
    public void setTempHigh(Double tempHigh) {
        this.tempHigh = tempHigh;
    }
    public Double getTempLow() {
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
                + "  dateInProperFormat: " + dateInProperFormat);
    }

}

