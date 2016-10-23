package xyz.dcoric.androidweather;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by denis on 23.10.2016..
 */

public class ForecastAdapter extends ArrayAdapter<Weather> {
    private ArrayList<Weather> forecast;

    public ForecastAdapter(Context context, int resource) {
        super(context, resource);
        forecast = new ArrayList<Weather>();
    }

    public void setForecast(ArrayList<Weather> forecast) {
        this.forecast = forecast;
    }

    @Override
    public int getCount() {
        return forecast.size();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Weather weather = forecast.get(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        SimpleDateFormat simpleDateformat = new SimpleDateFormat("EEEE");

        Date date = new Date(weather.getTimestamp() * 1000);
        String dayText = simpleDateformat.format(date);
        viewHolder.dayTextView.setText(dayText);
        viewHolder.weatherTextView.setText(weather.getWeather());
        viewHolder.highTextView.setText(String.valueOf(Math.round(weather.getHigh())) + Weather.getUnit());
        viewHolder.lowTextView.setText(String.valueOf(Math.round(weather.getLow())) + Weather.getUnit());

        String uri = "@drawable/open_weather_icon_" + weather.getEmbededImage();  // where myresource (without the extension) is the file

        int imageResource = getContext().getResources().getIdentifier(uri, null, getContext().getPackageName());

        Drawable res = ResourcesCompat.getDrawable(getContext().getResources(), imageResource, null);
        if (res != null) {
            viewHolder.imageView.setImageDrawable(res);
        } else {
            viewHolder.imageView.setImageBitmap(weather.getWeatherImage());
        }

        return convertView;
    }

    private static class ViewHolder {
        public TextView dayTextView, weatherTextView, highTextView, lowTextView;
        public ImageView imageView;

        public ViewHolder(View listItem) {
            dayTextView = (TextView) listItem.findViewById(R.id.dayTextView);
            weatherTextView = (TextView) listItem.findViewById(R.id.weatherTextView);
            highTextView = (TextView) listItem.findViewById(R.id.highTextView);
            lowTextView = (TextView) listItem.findViewById(R.id.lowTextView);
            imageView = (ImageView) listItem.findViewById(R.id.imageView);
        }
    }
}
