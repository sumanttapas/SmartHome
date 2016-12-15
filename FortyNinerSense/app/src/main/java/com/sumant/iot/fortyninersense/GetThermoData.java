package com.sumant.iot.fortyninersense;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

/**
 * Created by sumant on 10/26/16.
 */
public class GetThermoData
{
    String phpfile;
    String ipAddress;
    final static String URL = "http://%s/%s?userID=%s";
    final static String INSIDE_TEMP = "insideTemp";
    final static String SET_TEMP = "setTemp";
    final static String MODE = "ac_mode";
    final static String FAN = "fan";
    public GetThermoData(){

    }

    public static HashMap<String, String> getThermoData(String phpfile, String ipAddress, int userID) {
        try {
            JSONArray data = GetThermoData.getJSONObject(phpfile, ipAddress, Integer.toString(userID));
            HashMap<String, String> ret = new HashMap<>();
            for (int i = 0; i < data.length(); i++) {
                JSONObject k = data.getJSONObject(i);
                int floorid = k.getInt("floor");
                if(floorid == 1) {
                    ret.put("MainCurr",Integer.toString((int)Math.ceil(k.getDouble(INSIDE_TEMP))));
                    ret.put("MainSet",Integer.toString((int)Math.ceil(k.getDouble(SET_TEMP))));
                    ret.put("MainMode",k.getString(MODE));
                    ret.put("MainFan", k.getString(FAN));
                }else if(floorid ==2) {
                    ret.put("UpCurr",Integer.toString((int)Math.ceil(k.getDouble(INSIDE_TEMP))));
                    ret.put("UpSet",Integer.toString((int)Math.ceil(k.getDouble(SET_TEMP))));
                    ret.put("UpMode",k.getString(MODE));
                    ret.put("UpFan", k.getString(FAN));
                }
            }
            if(ret.isEmpty()) {
                return null;
            }
            else
                return ret;
        } catch (Exception e) {
            e.printStackTrace();
        }


        return null;
    }
    public static JSONArray getJSONObject(String phpfile, String ipAddress, String userID) {
        try {
            java.net.URL url = new URL(String.format(URL, ipAddress, phpfile, userID));

            HttpURLConnection connection =
                    (HttpURLConnection)url.openConnection();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));

            StringBuffer json = new StringBuffer(4096);
            String tmp="";
            while((tmp=reader.readLine())!=null)
                json.append(tmp).append("\n");
            reader.close();
            Log.d("json",json.toString());

            JSONArray data = new JSONArray(json.toString());
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
