package com.todd.pokemeteo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Main Activity";
    private TextView mTextViewCityName;
    private RelativeLayout mRelativeLayoutConnection;
    private TextView mTextViewNoConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRelativeLayoutConnection = findViewById(R.id.relative_layout_connection);
        mTextViewNoConnection = findViewById(R.id.text_view_no_connection);
        mTextViewCityName = findViewById(R.id.text_view_city_name);

        // Check internet connection
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            mRelativeLayoutConnection.setVisibility(View.VISIBLE);
            mTextViewNoConnection.setVisibility(View.INVISIBLE);
            mTextViewCityName.setText(getString(R.string.city_name));
            Toast.makeText(this, mTextViewCityName.getText(), Toast.LENGTH_SHORT).show();
        } else {
            mRelativeLayoutConnection.setVisibility(View.INVISIBLE);
            mTextViewNoConnection.setVisibility(View.VISIBLE);
            mTextViewNoConnection.setText(getString(R.string.pas_connexion));
            Toast.makeText(this, mTextViewNoConnection.getText(), Toast.LENGTH_SHORT).show();
        }

    }
}