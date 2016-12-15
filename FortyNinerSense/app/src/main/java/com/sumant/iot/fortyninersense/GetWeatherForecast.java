package com.sumant.iot.fortyninersense;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by sumant on 10/25/16.
 */
public class GetWeatherForecast {

    private static final String OPEN_WEATHER_MAP_API =
            "http://api.openweathermap.org/data/2.5/forecast/daily?lat=35&lon=-81&cnt=5&units=imperial";

    public static int setWeatherIcon(int actualId, Context context){
        int id = actualId / 100;
       // String icon = "";
        if(actualId == 800){
            return R.drawable.ic_weather_sunny;

        } else {
            switch(id) {
                case 2 :
                    //icon = "&#xf01e;";//getActivity().getString(R.string.weather_thunder);
                    return R.drawable.ic_weather_thunder;

                case 3 :
                    //icon = "&#xf01c;";//getActivity().getString(R.string.weather_drizzle);
                    return R.drawable.ic_weather_drizzle;

                case 7 :
                    //icon = "&#xf014;";//getActivity().getString(R.string.weather_foggy);
                    return R.drawable.ic_weather_foggy;

                case 8 :
                    //icon = "&#xf013;";//getActivity().getString(R.string.weather_cloudy);
                    return R.drawable.ic_weather_cloudy;

                case 6 :
                    //icon = "&#xf01b;";//getActivity().getString(R.string.weather_snowy);
                    return R.drawable.ic_weather_snowy;

                case 5 :
                    //icon = "&#xf019;";//getActivity().getString(R.string.weather_rainy);
                    return R.drawable.ic_weather_rainy;

            }
        }
        return R.drawable.ic_not_reachable;
    }


    public static ArrayList<ArrayList<String>> getForecastValue(Context context){
        try {
            URL url = new URL(String.format(OPEN_WEATHER_MAP_API));
            HttpURLConnection connection =
                    (HttpURLConnection)url.openConnection();

            connection.addRequestProperty("x-api-key",
                    context.getString(R.string.open_weather_maps_app_id));

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));

            StringBuffer json = new StringBuffer(4096);
            String tmp="";
            while((tmp=reader.readLine())!=null)
                json.append(tmp).append("\n");
            reader.close();
            ArrayList<ArrayList<String>> mainList = new ArrayList<ArrayList<String>>();
            JSONObject data = new JSONObject(json.toString());
            Log.d("JSON",data.toString());
            /*JSONObject main = data.getJSONObject("main");
            Integer temp = (int) Math.ceil(main.getDouble("temp"));
            String ret =String.format("%d",temp)+" \u2109";*/
            JSONArray list = data.getJSONArray("list");
            for (int i = 0; i < list.length(); i++) {
                JSONObject k = list.getJSONObject(i);
                ArrayList<String> val = new ArrayList<String>();
                JSONObject temp = k.getJSONObject("temp");
                val.add(Integer.toString((int)Math.ceil(temp.getDouble("min")))); // min temp
                val.add(Integer.toString((int)Math.ceil(temp.getDouble("max")))); //max temp
                int id = k.getJSONArray("weather").getJSONObject(0).getInt("id");
                val.add(Integer.toString(setWeatherIcon(id, context))); //icon font

                DateFormat df = DateFormat.getDateTimeInstance();
                String date = df.format(new Date(k.getLong("dt")*1000));
                val.add(date.substring(0,11));
                mainList.add(val);

            }


            // This value will be 404 if the request was not
            // successful
            if(data.getInt("cod") != 200){
                return null;
            }
            return mainList;

        }catch(Exception e){
            Log.d("NULL","Exception in Forecast");
            return null;
        }
    }



}
