package api;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class API {
    public static final String OPEN_WEATHER_API_KEY = "01897e497239c8aff78d9b8538fb24ea";

    public static final String OPEN_WEATHER_MAP_API_COORDINATES = "http://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&units=metric&lang=fr&appid=" + OPEN_WEATHER_API_KEY;
    public static final String OPEN_WEATHER_MAP_API_CITY_NAME = "http://api.openweathermap.org/data/2.5/weather?q=%s&units=metric&lang=fr&appid=" + OPEN_WEATHER_API_KEY;

    public static final String GEODB_API_CITY_SEARCH = "http://geodb-free-service.wirefreethought.com/v1/geo/cities?types=CITY&namePrefix=%s";

    private static OkHttpClient okHttpClient = new OkHttpClient();
    private static Handler handler = new Handler();

    // Coordonnées de Antananarivo, Madagascar
    public static final double defaultLat = -18.9137;
    public static final double defaultLon = 47.5361;

    public static void callApiGetCitiesBySearch(String inputCityName, Context context, ApiCallBack callback) {
        String[] params = { String.valueOf(inputCityName) };
        String callUrl = String.format(GEODB_API_CITY_SEARCH, params);

        callApiGet(callback, context, callUrl);
    }

    public static void callApiGetByCityName(String cityName, Context context, ApiCallBack callback) {
        String[] params = { String.valueOf(cityName) };
        String callUrl = String.format(OPEN_WEATHER_MAP_API_CITY_NAME, params);

        callApiGet(callback, context, callUrl);
    }

    public static void callApiGetByCoordinates(double latitude, double longitude, Context context, ApiCallBack callback) {
        String[] params = { String.valueOf(latitude), String.valueOf(longitude) };

        String callUrl = String.format(OPEN_WEATHER_MAP_API_COORDINATES, params);
        callApiGet(callback, context, callUrl);
    }

    public static void callApiGet(ApiCallBack callback, Context context, String callUrl) {
        Request request = new Request.Builder().url(callUrl).build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.post(() -> callback.error(context,"erreur à l'appel d'API"));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("TAG", "sendApiCall.onResponse()");
                if (response.isSuccessful()) {
                    final String strJson = response.body().string();
                    Log.d("API CALL RESULTS", strJson);
                    handler.post(() -> callback.execute(strJson));
                } else {
                    handler.post(() -> callback.error(context,"erreur à l'appel d'API"));
                }
            }
        });
    }
}
