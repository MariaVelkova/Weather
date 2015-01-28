package com.example.maria.weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Maria on 1/15/2015.
 */
public class Day {
    int dt;
    Double lat;
    Double lon;
    int humidity;
    Double pressure;
    Double speed;
    String weatherIcon;
    String weatherDescription;
    String weatherMain;
    Double tempMin;
    Double tempMax;

    public Day(int dt, Double lat, Double lon, int humidity, Double pressure, Double speed, String weatherIcon, String weatherDescription, String weatherMain, Double tempMin, Double tempMax) {
        this.dt = dt;
        this.lat = lat;
        this.lon = lon;
        this.humidity = humidity;
        this.pressure = pressure;
        this.speed = speed;
        this.weatherIcon = weatherIcon;
        this.weatherDescription = weatherDescription;
        this.weatherMain = weatherMain;
        this.tempMin = tempMin;
        this.tempMax = tempMax;
    }

    public int getDt() {
        return dt;
    }

    public void setDt(int dt) {
        this.dt = dt;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public Double getPressure() {
        return pressure;
    }

    public void setPressure(Double pressure) {
        this.pressure = pressure;
    }

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public String getWeatherIcon() {
        return weatherIcon;
    }

    public void setWeatherIcon(String weatherIcon) {
        this.weatherIcon = weatherIcon;
    }

    public String getWeatherDescription() {
        return weatherDescription;
    }

    public void setWeatherDescription(String weatherDescription) {
        this.weatherDescription = weatherDescription;
    }

    public String getWeatherMain() {
        return weatherMain;
    }

    public void setWeatherMain(String weatherMain) {
        this.weatherMain = weatherMain;
    }

    public Double getTempMin() {
        return tempMin;
    }

    public void setTempMin(Double tempMin) {
        this.tempMin = tempMin;
    }

    public Double getTempMax() {
        return tempMax;
    }

    public void setTempMax(Double tempMax) {
        this.tempMax = tempMax;
    }
}
