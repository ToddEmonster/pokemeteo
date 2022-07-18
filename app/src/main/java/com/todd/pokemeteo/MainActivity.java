package com.todd.pokemeteo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Main Activity";
    private RelativeLayout mRelativeLayoutConnection;
    private TextView mTextViewNoConnection;

    private TextView mTextViewCityName;
    private EditText mEditTextMessage;
    private Button mButtonFavoris;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRelativeLayoutConnection = findViewById(R.id.relative_layout_connection);
        mTextViewNoConnection = findViewById(R.id.text_view_no_connection);
        mTextViewCityName = findViewById(R.id.text_view_city_name);
        mEditTextMessage = findViewById(R.id.edit_text_message);
        mButtonFavoris = findViewById(R.id.button_favoris);

        // Check internet connection
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            mRelativeLayoutConnection.setVisibility(View.VISIBLE);
            mTextViewNoConnection.setVisibility(View.INVISIBLE);

            mButtonFavoris.setOnClickListener(this::onClickFavoris);

            mTextViewCityName.setText(getString(R.string.city_name));
            Toast.makeText(this, mTextViewCityName.getText(), Toast.LENGTH_SHORT).show();
        } else {
            mRelativeLayoutConnection.setVisibility(View.INVISIBLE);
            mTextViewNoConnection.setVisibility(View.VISIBLE);
            mTextViewNoConnection.setText(getString(R.string.pas_connexion));
            Toast.makeText(this, mTextViewNoConnection.getText(), Toast.LENGTH_SHORT).show();
        }

    }

    public void onClickFavoris(View v) {
        Toast.makeText(this, "Clic sur favoris", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this,FavoriteActivity.class);
        intent.putExtra("message", mEditTextMessage.getText().toString());
        startActivity(intent);
    }
}