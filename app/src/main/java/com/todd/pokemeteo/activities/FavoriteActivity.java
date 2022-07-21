package com.todd.pokemeteo.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

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

import org.json.JSONException;

import java.util.ArrayList;

import api.API;

public class FavoriteActivity extends AppCompatActivity {
    private static final String TAG = "Favorite Activity";

    private ActivityFavoriteBinding binding;

    private RecyclerView mRecyclerView;
    private FavoriteAdapter mAdapter;

    private City mCityRemoved;
    private int mPositionCityRemoved;

    private ArrayList<City> mCities;
    private Context mContext;

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

        mRecyclerView = binding.include.recyclerView;
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new FavoriteAdapter(mContext, mCities);
        mRecyclerView.setAdapter(mAdapter);

        ItemTouchHelper itemTouchHelper = createItemTouchHelper();
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
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

    public void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                new ContextThemeWrapper(mContext, R.style.AlertDialogDarkStyle)
        );
        DialogAddFavoriteBinding dialogBinding = DialogAddFavoriteBinding.inflate(LayoutInflater.from(mContext));
        builder.setView(dialogBinding.getRoot());

        builder.setPositiveButton(R.string.dialog_ok, (dialog, which) -> {
            String cityName = dialogBinding.editTextDialogCity.getText().toString();
            callAPIWithCityName(cityName);
        });
        builder.setNegativeButton(R.string.dialog_annuler, null);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void callAPIWithCityName(String cityName) {
        API.callApiGetByCityName(cityName, this, this::renderFavoriteCity);
    }

    public void renderFavoriteCity(String strJson) {
        try {
            City favoriteCity = new City(strJson);
            mCities.add(favoriteCity);
            mAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
        }
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