package com.todd.pokemeteo.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class CityAutocomplete implements Serializable {
    public int mIdCity;
    public String mName;

    public String mCountry;
    public double mLatitude;
    public double mLongitude;

    public String mStringJson;

    public CityAutocomplete(String stringJson) throws JSONException {
        mStringJson = stringJson;
        JSONObject json = new JSONObject(stringJson);

        mIdCity = json.getInt("id");
        mName = json.getString("name");
        mCountry = json.getString("country");
        mLatitude = json.getDouble("latitude");
        mLongitude = json.getDouble("longitude");
    }

    @Override
    public String toString() {
        return "CityAutocomplete{" +
                "mIdCity=" + mIdCity +
                ", mName='" + mName + '\'' +
                ", mCountry='" + mCountry + '\'' +
                ", mLatitude=" + mLatitude +
                ", mLongitude=" + mLongitude +
                ", mStringJson='" + mStringJson + '\'' +
                '}';
    }
}

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