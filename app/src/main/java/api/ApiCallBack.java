package api;

import android.content.Context;
import android.widget.Toast;

import org.json.JSONException;

public interface ApiCallBack {

    void execute(String strJson);

    default void error(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
