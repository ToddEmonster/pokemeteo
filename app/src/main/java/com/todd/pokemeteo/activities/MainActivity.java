package com.todd.pokemeteo.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.todd.pokemeteo.R;
import api.API;

import com.todd.pokemeteo.databinding.ActivityMainBinding;
import com.todd.pokemeteo.models.City;

import org.json.JSONException;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Main Activity";

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()");
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Check internet connection
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            binding.relativeLayoutConnection.setVisibility(View.VISIBLE);
            binding.textViewNoConnection.setVisibility(View.INVISIBLE);

            binding.buttonFavoris.setOnClickListener(this::onClickFavoris);

            // Toast.makeText(this, mTextViewCityLabel.getText(), Toast.LENGTH_SHORT).show();
            API.callApiGetByCoordinates(API.defaultLat, API.defaultLon, this, this::renderCurrentWeather);


        } else {
            binding.relativeLayoutConnection.setVisibility(View.INVISIBLE);
            binding.textViewNoConnection.setVisibility(View.VISIBLE);
            binding.textViewNoConnection.setText(getString(R.string.pas_connexion));
            Toast.makeText(this, binding.textViewNoConnection.getText(), Toast.LENGTH_SHORT).show();
        }

    }

    public void renderCurrentWeather(String strJson) {
        try {
            City currentCity = new City(strJson);
            binding.textViewCityName.setText(currentCity.mName);
            binding.textViewCityDesc.setText(currentCity.mDescription);
            binding.textViewCityTemp.setText(currentCity.mTemperature);
            binding.imageViewCityIcon.setImageResource(currentCity.mWeatherResIconWhite);

            binding.linearLayoutCurrentCity.setVisibility(View.VISIBLE);
            binding.progressBar.setVisibility(View.GONE);

        } catch (JSONException e) {
            Toast.makeText(this, "Une erreur est survenue", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume()");
    }
    
    public void onClickFavoris(View v) {
        //Toast.makeText(this, "Clic sur favoris", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this,FavoriteActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy()");
    }
}