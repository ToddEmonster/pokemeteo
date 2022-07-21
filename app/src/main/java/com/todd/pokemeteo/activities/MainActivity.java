package com.todd.pokemeteo.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.todd.pokemeteo.R;
import com.todd.pokemeteo.utils.ApiUtil;
import com.todd.pokemeteo.utils.Util;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Main Activity";
    private OkHttpClient mOkHttpClient;

    private RelativeLayout mRelativeLayoutConnection;
    private TextView mTextViewNoConnection;

    private TextView mTextViewCityLabel;
    private EditText mEditTextMessage;
    private Button mButtonFavoris;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()");
        setContentView(R.layout.activity_main);

        mOkHttpClient = new OkHttpClient();

        mRelativeLayoutConnection = findViewById(R.id.relative_layout_connection);
        mTextViewNoConnection = findViewById(R.id.text_view_no_connection);
        mTextViewCityLabel = findViewById(R.id.text_view_city_label);
        mEditTextMessage = findViewById(R.id.edit_text_message);
        mButtonFavoris = findViewById(R.id.button_favoris);

        // Check internet connection
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            mRelativeLayoutConnection.setVisibility(View.VISIBLE);
            mTextViewNoConnection.setVisibility(View.INVISIBLE);

            mButtonFavoris.setOnClickListener(this::onClickFavoris);

            mTextViewCityLabel.setText(getString(R.string.city_name));
            //Toast.makeText(this, mTextViewCityLabel.getText(), Toast.LENGTH_SHORT).show();
            sendApiCall();
        } else {
            mRelativeLayoutConnection.setVisibility(View.INVISIBLE);
            mTextViewNoConnection.setVisibility(View.VISIBLE);
            mTextViewNoConnection.setText(getString(R.string.pas_connexion));
            Toast.makeText(this, mTextViewNoConnection.getText(), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume()");
    }

    public void sendApiCall() {
        Log.d(TAG, "sendApiCall()");
        String testUrl = String.format(
                "https://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&appid=%s",
                ApiUtil.testLat, ApiUtil.testLon, ApiUtil.APP_API_KEY
        );
        Request request = new Request.Builder().url(testUrl).build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d(TAG, "sendApiCall.onResponse()");
                if (response.isSuccessful()) {
                    final String stringJson = response.body().string();
                    Log.d(TAG, stringJson);
                }
            }
        });
    }
    
    public void onClickFavoris(View v) {
        Toast.makeText(this, "Clic sur favoris", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this,FavoriteActivity.class);
        intent.putExtra(Util.EXTRA_MESSAGE, mEditTextMessage.getText().toString());
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy()");
    }
}