package com.todd.pokemeteo.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.todd.pokemeteo.R;
import com.todd.pokemeteo.models.City;

import java.util.ArrayList;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {

    private static final String TAG = "Favorite Adapter";

    private Context mContext;
    private ArrayList<City> mCities;

    public FavoriteAdapter(Context mContext, ArrayList<City> mCities) {
        this.mContext = mContext;
        this.mCities = mCities;
    }

    // Classe holder qui contient la vue d'un item
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {

        private TextView mTextViewCityName;
        private TextView mTextViewCityDescription;
        private TextView mTextViewCityTemp;
        private ImageView mTextViewCityImage;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnLongClickListener(this);
            itemView.setTag(this);

            mTextViewCityName = itemView.findViewById(R.id.text_view_item_city_name);
            mTextViewCityDescription = itemView.findViewById(R.id.text_view_item_city_desc);
            mTextViewCityTemp = itemView.findViewById(R.id.text_view_item_city_temp);
            mTextViewCityImage = itemView.findViewById(R.id.text_view_item_city_img);
        }

        @Override
        public boolean onLongClick(View view) {

            final AlertDialog.Builder builder = new AlertDialog.Builder(
                    new ContextThemeWrapper(mContext, R.style.AlertDialogDarkStyle)
            );

            builder
                    .setMessage(String.format("Voulez-vous supprimer la ville \"%s\" ?", mTextViewCityName.getText()))
                    .setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            mCities.remove(getBindingAdapterPosition());
                            notifyDataSetChanged();
                            notifyItemRemoved(getBindingAdapterPosition());
                            notifyItemRangeChanged(getBindingAdapterPosition(), mCities.size());
                        }
                    })
                    .setNegativeButton(R.string.dialog_annuler, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) { }
                    });

            builder.create().show();
            return false;
        }

    }

    @NonNull
    @Override
    public FavoriteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_favorite_city, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteAdapter.ViewHolder holder, int position) {
        City city = mCities.get(position);

        // No need for `holder.position = position;` because done auto inside library : Holder class has getBindingAdapterPosition()
        holder.mTextViewCityName.setText(city.mName);
        holder.mTextViewCityDescription.setText(city.mDescription);
        holder.mTextViewCityTemp.setText(city.mTemperature);
        holder.mTextViewCityImage.setImageResource(city.mWeatherResIconGrey);

    }

    @Override
    public int getItemCount() {
        return mCities.size();
    }
}
