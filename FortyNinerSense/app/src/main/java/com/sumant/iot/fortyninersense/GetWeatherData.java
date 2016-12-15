package com.sumant.iot.fortyninersense;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

/**
 * Created by sumant on 10/25/16.
 */
public class GetWeatherData
{
    private static final String OPEN_WEATHER_MAP_API =
            "http://api.openweathermap.org/data/2.5/weather?q=%s&units=imperial";

    public static String getTempValue(Context context){
        try {
            URL url = new URL(String.format(OPEN_WEATHER_MAP_API, "Charlotte"));
            HttpURLConnection connection =
                    (HttpURLConnection)url.openConnection();

            connection.addRequestProperty("x-api-key",
                    context.getString(R.string.open_weather_maps_app_id));

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));

            StringBuffer json = new StringBuffer(1024);
            String tmp="";
            while((tmp=reader.readLine())!=null)
                json.append(tmp).append("\n");
            reader.close();

            JSONObject data = new JSONObject(json.toString());
            JSONObject main = data.getJSONObject("main");
            Integer temp = (int) Math.ceil(main.getDouble("temp"));
            String ret =String.format("%d",temp)+" \u2109";


            // This value will be 404 if the request was not
            // successful
            if(data.getInt("cod") != 200){
                return null;
            }
            return ret;

        }catch(Exception e){
            return null;
        }
    }
}

