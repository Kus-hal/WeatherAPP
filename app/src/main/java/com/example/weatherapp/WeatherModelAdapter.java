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

public class WeatherModelAdapter extends RecyclerView.Adapter<WeatherModelAdapter.ViewHolder> {

    private Context context;
    private ArrayList<WeatherModel> arr;

    public WeatherModelAdapter(Context context, ArrayList<WeatherModel> arr) {
        this.context = context;
        this.arr = arr;
    }

    @NonNull
    @Override
    public WeatherModelAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layout = LayoutInflater.from(parent.getContext());
        View view = layout.inflate(R.layout.weather_model_item,parent,false);
        return new ViewHolder (view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WeatherModel obj = arr.get(position);

        if(obj.getPod().equals("d")){
            holder.imgCardBG.setImageResource(R.drawable.day);
        }else {
            holder.imgCardBG.setImageResource(R.drawable.night);
        }
        holder.textTime.setText(obj.getTime());
        holder.textTemp.setText(obj.getTemp()+"°c");
        Picasso.get().load("https://openweathermap.org/img/w/"+obj.getIcon()+".png").into(holder.imgCondition);
        holder.textWSpeed.setText(""+obj.getwSpeed()+" Km/h");
    }

    @Override
    public int getItemCount() {
        if(arr!=null){
            return arr.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView textTemp, textTime, textWSpeed;
        ImageView imgCondition, imgCardBG;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textTime = itemView.findViewById(R.id.textCity);
            textTemp = itemView.findViewById(R.id.textTemperature);
            imgCondition = itemView.findViewById(R.id.imgCondition);
            imgCardBG = itemView.findViewById(R.id.imgCardBG);
            textWSpeed = itemView.findViewById(R.id.textWindSpeed);
        }
    }
}
