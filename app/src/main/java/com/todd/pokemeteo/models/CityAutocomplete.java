package com.todd.pokemeteo.models;

import com.todd.pokemeteo.utils.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class CityAutocomplete implements Serializable {
    /*
        {
          "id": 2987553,
          "wikiDataId": "Q24371",
          "type": "CITY",
          "name": "L'Aldosa de Canillo",
          "country": "Andorra",
          "countryCode": "AD",
          "region": "Canillo",
          "regionCode": 2,
          "latitude": 42.57777778,
          "longitude": 1.61944444
        },
     */
    public int mIdCity;
    public String mName;

    public String mCountry;
    public double mLatitude;
    public double mLongitude;

    public String mStringJson;

    public CityAutocomplete(String stringJson) throws JSONException {

        mStringJson = stringJson;

        JSONObject json = new JSONObject(stringJson);

        JSONObject details = json.getJSONArray("weather").getJSONObject(0);
        JSONObject main = json.getJSONObject("main");
        JSONObject coord = json.getJSONObject("coord");

        mIdCity = json.getInt("id");
        mName = json.getString("name");
        mCountry = json.getJSONObject("sys").getString("country");
        mLatitude = coord.getDouble("lat");
        mLongitude = coord.getDouble("lon");
    }
}
