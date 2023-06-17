package com.example.weatherapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FavCityAdapter extends RecyclerView.Adapter<FavCityAdapter.ViewHolder> {

    private Context context;
    private ArrayList<FavCityModel> arr;

    public FavCityAdapter(Context context, ArrayList<FavCityModel> arr) {
        this.context = context;
        this.arr = arr;
    }

    @NonNull
    @Override

    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.favcity,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FavCityModel obj = arr.get(position);

        holder.imgCardBG.setImageResource(R.drawable.favoritecard);
        holder.textCity.setText(obj.getCity());
        holder.textTemperature.setText(obj.getTemperature());
        Picasso.get().load("https://openweathermap.org/img/w/"+obj.getImgCondition()+".png").into(holder.imgCondition);
        holder.textCondition.setText(obj.getCondition());
        holder.textWindSpeed.setText(obj.getWindSpeed());
    }

    @Override
    public int getItemCount() {
        return arr.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imgCardBG, imgCondition;
        TextView textCity, textTemperature, textCondition, textWindSpeed;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgCardBG = itemView.findViewById(R.id.imgCardBG);
            imgCondition = itemView.findViewById(R.id.imgCondition);
            textCity = itemView.findViewById(R.id.textCity);
            textTemperature = itemView.findViewById(R.id.textTemperature);
            textCondition = itemView.findViewById(R.id.textCondition);
            textWindSpeed = itemView.findViewById(R.id.textWindSpeed);
        }
    }
}
