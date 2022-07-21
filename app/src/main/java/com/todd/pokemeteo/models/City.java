package com.todd.pokemeteo.models;

import com.todd.pokemeteo.utils.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class City implements Serializable {

    public int mIdCity;
    public String mName;
    public int mWeatherResIconGrey;
    public String mTemperature;
    public String mDescription;

    public String mCountry;
    public int mWeatherResIconWhite;
    public double mLatitude;
    public double mLongitude;

    public String mStringJson;

    public City(String name, String desc, String temp, int resWeatherIcon) {
        mName = name;
        mDescription = desc;
        mTemperature = temp;
        mWeatherResIconGrey = resWeatherIcon;
    }

    public City(String stringJson) throws JSONException {

        mStringJson = stringJson;

        JSONObject json = new JSONObject(stringJson);

        JSONObject details = json.getJSONArray("weather").getJSONObject(0);
        JSONObject main = json.getJSONObject("main");
        JSONObject coord = json.getJSONObject("coord");

        mIdCity = json.getInt("id");
        mName = json.getString("name");
        mCountry = json.getJSONObject("sys").getString("country");
        mTemperature = String.format("%.0f", main.getDouble("temp")) + " ℃";
        mDescription = Util.capitalize(details.getString("description"));
        mWeatherResIconWhite = Util.setWeatherIcon(details.getInt("id"), json.getJSONObject("sys").getLong("sunrise") * 1000, json.getJSONObject("sys").getLong("sunset") * 1000);
        mWeatherResIconGrey = Util.setWeatherIcon(details.getInt("id"));
        mLatitude = coord.getDouble("lat");
        mLongitude = coord.getDouble("lon");
    }

}
