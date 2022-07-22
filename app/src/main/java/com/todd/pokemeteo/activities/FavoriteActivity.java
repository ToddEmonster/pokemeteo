package com.todd.pokemeteo.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.todd.pokemeteo.R;
import com.todd.pokemeteo.adapters.FavoriteAdapter;
import com.todd.pokemeteo.databinding.ActivityFavoriteBinding;
import com.todd.pokemeteo.databinding.DialogAddFavoriteBinding;
import com.todd.pokemeteo.models.City;
import com.todd.pokemeteo.models.CityAutocomplete;
import com.todd.pokemeteo.utils.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import api.API;

public class FavoriteActivity extends AppCompatActivity {
    private static final String TAG = "Favorite Activity";

    private ActivityFavoriteBinding binding;
    private DialogAddFavoriteBinding dialogBinding;

    private RecyclerView mRecyclerView;
    private FavoriteAdapter mAdapter;

    private City mCityRemoved;
    private int mPositionCityRemoved;

    private ArrayList<City> mCities;
    private Context mContext;

    private ArrayAdapter<String> mAutocompleteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFavoriteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mContext = this;

        setSupportActionBar(binding.toolbar);
        binding.toolbarLayout.setTitle(getTitle());

        binding.fab.setOnClickListener(view -> showAlertDialog());

        mCities = new ArrayList<>();

        // Recycler View
        mRecyclerView = binding.include.recyclerView;
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new FavoriteAdapter(mContext, mCities);
        mRecyclerView.setAdapter(mAdapter);

        ItemTouchHelper itemTouchHelper = createItemTouchHelper();
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    /** Ouvre la modale d'ajout de villes */
    public void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(mContext, R.style.AlertDialogDarkStyle));
        dialogBinding = DialogAddFavoriteBinding.inflate(LayoutInflater.from(mContext));
        builder.setView(dialogBinding.getRoot());

        // Création d'un adapter pour la liste
        mAutocompleteAdapter = new ArrayAdapter<String>(
                mContext,
                android.R.layout.simple_dropdown_item_1line,
                new ArrayList<>());
        dialogBinding.autocompleteTextViewCities.setAdapter(mAutocompleteAdapter);

        // Ecouter l'input utilisateur
        dialogBinding.autocompleteTextViewCities.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                // Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Auto-generated method stub
            }

            // Ici lorsque le texte change
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Action si le texte a au moins 3 caractères
                String formattedInput = s.toString().trim();
                Log.d("TEXT", String.format("Charsequence: %s", formattedInput));
                    Log.d("DEMO", "plus de 3 char !");
                    callApiSearchWithCityNameInput(formattedInput);
            }
        });

        builder.setPositiveButton(R.string.dialog_ok, (dialog, which) -> {
                    String cityName = dialogBinding.autocompleteTextViewCities.getText().toString().toLowerCase();
                    callAPIWithCityName(cityName);
                });
        builder.setNegativeButton(R.string.dialog_annuler, null);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    // Appelle à l'API externe pour rechercher une ville
    private void callApiSearchWithCityNameInput(String cityNameInput) {
        API.callApiGetCitiesBySearch(cityNameInput, this, this::renderAutocompleteCities);
    }

    // Callback de la recherche de villes
    public void renderAutocompleteCities(String strJson) {
        try {
            // Récupérer la donnée JSON
            JSONObject json = new JSONObject(strJson);
            JSONArray data = json.getJSONArray("data");

            mAutocompleteAdapter.clear();

            int i = 0;
            // Prendre les n premières valeurs du tableau de JSONArray
            while (i < Util.AUTOCOMPLETE_DEFAULT_LIMIT) {
                JSONObject cityJson = data.getJSONObject(i);
                // Caster en POJO
                CityAutocomplete cityAutocomplete = new CityAutocomplete(cityJson.toString());
                Log.d("DEMO", String.format("New city created: %s", cityAutocomplete));

                // Ajouter le String à afficher
                mAutocompleteAdapter.add(cityAutocomplete.mName);
                i++;
            }

            // Ecoute du clic : gestion auto par le composant
        } catch (JSONException e) { }
    }

    private void callAPIWithCityName(String cityName) {
        API.callApiGetByCityName(cityName, this, this::renderFavoriteCity);
    }

    public void renderFavoriteCity(String strJson) {
        try {
            City favoriteCity = new City(strJson);
            mCities.add(favoriteCity);
            mAdapter.notifyDataSetChanged();
        } catch (JSONException e) { }
    }

    private ItemTouchHelper createItemTouchHelper() {
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                mPositionCityRemoved = viewHolder.getAdapterPosition();
                mCityRemoved = mCities.remove(mPositionCityRemoved);

                mAdapter.notifyDataSetChanged();

                Snackbar.make(binding.myCoordinatorLayout, mCityRemoved.mName + " est supprimé", Snackbar.LENGTH_LONG)
                        .setAction("Annuler", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mCities.add(mPositionCityRemoved, mCityRemoved);
                                mAdapter.notifyDataSetChanged();
                            }
                        })
                        .show();
            }
        });
        return itemTouchHelper;
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
}