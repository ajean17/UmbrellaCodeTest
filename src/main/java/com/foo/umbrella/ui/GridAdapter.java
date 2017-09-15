package com.foo.umbrella.ui;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.foo.umbrella.R;
import com.foo.umbrella.data.model.ForecastCondition;

import java.util.List;

public class GridAdapter extends RecyclerView.Adapter<GridAdapter.ViewHolder> {
    private List<ForecastCondition> forecasts;

    GridAdapter(List<ForecastCondition> forecasts){
        this.forecasts = forecasts;
    }

    public void updateDataSet(List<ForecastCondition> resultList) {
        this.forecasts = resultList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(forecasts.size() > 0) {
            ForecastCondition forecast = forecasts.get(position);
            String time = forecast.getDisplayTime();
            String condition = forecast.getCondition();
            String tempF = forecast.getTempFahrenheit();
            String tempC = forecast.getTempCelsius();

            holder.tvTime.setText(time);
            holder.tvTemp.setText(tempC + "\u00B0");

            switch (condition) {
                case "Chance of Rain":
                    holder.ivFor.setImageResource(R.drawable.weather_rainy);
                    break;
                case "Rain":
                    holder.ivFor.setImageResource(R.drawable.weather_cloudy);
                    break;
                case "Overcast":
                    holder.ivFor.setImageResource(R.drawable.weather_cloudy);
                    break;
                case "Cloudy":
                    holder.ivFor.setImageResource(R.drawable.weather_cloudy);
                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return 0;
    }


    class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvTime;
        TextView tvTemp;
        ImageView ivFor;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTime = (TextView) itemView.findViewById(R.id.tv_forecast_time);
            tvTemp = (TextView) itemView.findViewById(R.id.tv_forecast_temp);
            ivFor = (ImageView) itemView.findViewById(R.id.iv_forecast);
        }
    }
}
