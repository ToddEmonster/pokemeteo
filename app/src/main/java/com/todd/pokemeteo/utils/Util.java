package com.todd.pokemeteo.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.todd.pokemeteo.R;

import java.util.Date;

public class Util {

    public final static String CITIES = "cities";
    public static final String CITY = "city";
    public static final String KEY_MESSAGE = "key_message";


    /*
     * Méthode qui initialise l'icone blanc présent dans la MainActivity
     * */
    public static int setWeatherIcon(int actualId, long sunrise, long sunset) {

        int id = actualId / 100;
        int icon = R.drawable.morpheo_base;

        if (actualId == 800) {
            long currentTime = new Date().getTime();

            if (currentTime >= sunrise && currentTime < sunset) {
                icon = R.drawable.morpheo_sun;
            } else {
                icon = R.drawable.noctali_moon;
            }
        } else {
            switch (id) {
                case 2:
                    icon = R.drawable.electhor_thunder;
                    break;
                case 3:
                    icon = R.drawable.nenupiot_drizzle;
                    break;
                case 7:
                    icon = R.drawable.doudouvet_foggy;
                    break;
                case 8:
                    icon = R.drawable.morpheo_cloud;
                    break;
                case 6:
                    icon = R.drawable.sorbebe_snow;
                    break;
                case 5:
                    icon = R.drawable.morpheo_rain;
                    break;
            }
        }

        return icon;
    }

    /*
     * Méthode qui initialise l'icone gris présent dans le forecast et dans la liste des favoris.
     * */
    public static int setWeatherIcon(int actualId) {

        int id = actualId / 100;
        int icon = R.drawable.morpheo_base;

        if (actualId != 800) {
            switch (id) {
                case 2:
                    icon = R.drawable.electhor_thunder;
                    break;
                case 3:
                    icon = R.drawable.nenupiot_drizzle;
                    break;
                case 7:
                    icon = R.drawable.doudouvet_foggy;
                    break;
                case 8:
                    icon = R.drawable.morpheo_cloud;
                    break;
                case 6:
                    icon = R.drawable.sorbebe_snow;
                    break;
                case 5:
                    icon = R.drawable.morpheo_rain;
                    break;
            }
        }

        return icon;
    }

    public static boolean isActiveNetwork(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }

    public static String capitalize(String s) {
        if (s == null) return null;
        if (s.length() == 1) {
            return s.toUpperCase();
        }
        if (s.length() > 1) {
            return s.substring(0, 1).toUpperCase() + s.substring(1);
        }
        return "";
    }
}
