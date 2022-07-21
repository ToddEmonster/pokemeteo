package com.todd.pokemeteo.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.todd.pokemeteo.R;
import com.todd.pokemeteo.adapters.FavoriteAdapter;
import com.todd.pokemeteo.databinding.ActivityFavoriteBinding;
import com.todd.pokemeteo.models.City;

import java.util.ArrayList;

public class FavoriteActivity extends AppCompatActivity {
    private static final String TAG = "Favorite Activity";

    private ActivityFavoriteBinding binding;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private ArrayList<City> mCities;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityFavoriteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = binding.toolbarLayout;
//        toolBarLayout.setTitle(getTitle());

        mContext = this;

        // Return button
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Floating Action Button
        FloatingActionButton fab = binding.fab;
        fab.setOnClickListener(this::onClickSearch);

        // Items list
        mCities = new ArrayList<>();

        City city1 = new City("Antananarivo", "Nuageux", "17°C", R.drawable.morpheo_cloud);
        City city2 = new City("Barcelone", "Ensoleillé", "28°C", R.drawable.morpheo_sun);
        City city3 = new City("Chongqing", "Légères pluies", "24°C", R.drawable.nenupiot_drizzle);
        City city4 = new City("New Delhi", "Pluies modérées", "29°C", R.drawable.morpheo_rain);

        mCities.add(city1);
        mCities.add(city2);
        mCities.add(city3);
        mCities.add(city4);

        // Recycler View
        mRecyclerView = findViewById(R.id.favorite_recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new FavoriteAdapter(this, mCities);
        mRecyclerView.setAdapter(mAdapter);
    }
    public void onClickSearch(View v) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(
                new ContextThemeWrapper(mContext, R.style.AlertDialogDarkStyle)
        );

        /* // Méthode 1 : à la mano
        builder
                .setTitle("Titre")
                .setMessage("Message")
                .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, close
                        // current activity
                        FavoriteActivity.this.finish();
                    }
                })
                .setNegativeButton("Non", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });
        */

        // Méthode 2 : custom dialog
        View dialogView = LayoutInflater.from(mContext).inflate(R.layout.dialog_add_favorite, null) ;
        final EditText editTextCity = (EditText) dialogView.findViewById(R.id.edit_text_dialog_city);
        builder.setView(dialogView);

        builder.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (editTextCity.getText().toString().length() > 0) {
                    updateWeatherDataFromCityName(editTextCity.getText().toString());
                }
            }
        });

        builder.setNegativeButton(R.string.dialog_annuler, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });

        builder.create().show();
    }

    public void updateWeatherDataFromCityName(final String newCityName) {
        // TODO : appel API pour récupérer la ville avec ses données météo à partir de newCityName
        City newCity = new City(newCityName, "Neige", "-2°C", R.drawable.sorbebe_snow);
        mCities.add(newCity);
        mAdapter.notifyDataSetChanged();
    }

    /**
     * Methode appelée automatiquement lors du clic sur le bouton Retour dans la toolbar
     * @param item  l'item cliqué
     * @return un booleen
     */
    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(myIntent);
        return true;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy()");
    }
}