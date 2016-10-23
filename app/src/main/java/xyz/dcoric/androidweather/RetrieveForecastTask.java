package xyz.dcoric.androidweather;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by denis on 23.10.2016..
 */

public class RetrieveForecastTask extends AsyncTask<Double, Void, ArrayList<Weather>> {

    private ForecastAdapter adapter;
    private int responseCode;
    private Context context;
    private Toolbar toolbar;
    private String cityName;

    public RetrieveForecastTask(ForecastAdapter adapter, Context context, Toolbar toolbar) {
        this.adapter = adapter;
        this.context = context;
        this.toolbar = toolbar;
    }

    @Override
    protected ArrayList<Weather> doInBackground(Double... params) {
        ArrayList<Weather> resultList = new ArrayList<Weather>();

        try {
            URL url = new URL("http://api.openweathermap.org/data/2.5/forecast/daily?appid=1fe3fc497c1f8a1c53d67e08754c4e28&lat=" +
                    params[0] +
                    "&lon=" +
                    params[1] +
                    "&units=" +
                    Weather.getUnitSystem().toString().toLowerCase() +
                    "&cnt=7" +
                    "&lang=" +
                    Locale.getDefault().toString());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();

            responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream is = connection.getInputStream();
                try {
                    String json = convertStreamToString(is);
                    resultList = parseJSON(json);
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    is.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return resultList;
    }

    @Override
    protected void onPostExecute(ArrayList<Weather> weathers) {
        super.onPostExecute(weathers);
        adapter.setForecast(weathers);
        adapter.notifyDataSetChanged();
        Toast.makeText(context, "Reloaded at: " +
                DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.SHORT, Locale.getDefault()).format(new Date()),
                Toast.LENGTH_SHORT).show();
        toolbar.setTitle(cityName);
    }


    // Helper method to parse the JSON that OpenWeatherMap returns
    private ArrayList<Weather> parseJSON(String json) throws JSONException {
        ArrayList<Weather> forecast = new ArrayList<Weather>();
        JSONObject jsonObject = new JSONObject(json).getJSONObject("city");
        cityName = jsonObject.getString("name");
        JSONArray jsonArray = new JSONObject(json).getJSONArray("list");
        for (int i = 0; i < jsonArray.length(); i++) {
            Weather weather = new Weather();
            JSONObject jsonDay = jsonArray.getJSONObject(i);
            weather.setTimestamp(jsonDay.getInt("dt"));
            weather.setHigh(jsonDay.getJSONObject("temp").getDouble("max"));
            weather.setLow(jsonDay.getJSONObject("temp").getDouble("min"));


            JSONObject jsonWeather = jsonDay.getJSONArray("weather").getJSONObject(0);
            weather.setWeather(jsonWeather.getString("main"));
            String iconName = jsonWeather.getString("icon");
            weather.setEmbededImage(iconName);

            byte[] bytes = imageByter(iconName);
            weather.setWeatherImage(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));

            forecast.add(weather);
        }
        return forecast;
    }

    // Helper method to convert the output from OpenWeatherMap to a String
    private String convertStreamToString(InputStream is) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder builder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }
        return builder.toString();
    }

    private static byte[] imageByter(String strurl) {
        try {
            URL url = new URL("http://openweathermap.org/img/w/" + strurl + ".png");
            InputStream is = (InputStream) url.getContent();
            byte[] buffer = new byte[8192];
            int bytesRead;
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            while ((bytesRead = is.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }
            return output.toByteArray();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }
}
