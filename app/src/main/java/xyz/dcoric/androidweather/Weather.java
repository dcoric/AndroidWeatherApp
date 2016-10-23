package xyz.dcoric.androidweather;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static xyz.dcoric.androidweather.Weather.UnitSystem.IMPERIAL;

/**
 * Created by denis on 23.10.2016..
 */

public class Weather {
    private double high;
    private double low;
    private String weather;
    private long timestamp;
    private Bitmap weatherImage;
    private String embededImage;

    public double getHigh() {
        return high;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    public double getLow() {
        return low;
    }

    public void setLow(double low) {
        this.low = low;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public Bitmap getWeatherImage() {
        return weatherImage;
    }

    public void setWeatherImage(Bitmap weatherImage) {
        this.weatherImage = weatherImage;
    }

    public String getEmbededImage() {
        return embededImage;
    }

    public void setEmbededImage(String embededImage) {
        this.embededImage = embededImage;
    }

    public static UnitSystem getUnitSystem() {
        Locale locale = Locale.getDefault();
        List<String> imperialLocales = new ArrayList<>(Arrays.asList(new String[]{"US", "LR", "MM"}));
        if (imperialLocales.contains(locale.getCountry())) {
            return IMPERIAL;
        }
        return UnitSystem.METRIC;
    }

    public static String getUnit() {
        switch (getUnitSystem()) {
            case IMPERIAL:
                return "°F";
            case METRIC:
                return "°C";
            default:
                return "°";
        }
    }

    enum UnitSystem {
        METRIC,
        IMPERIAL
    }
}
