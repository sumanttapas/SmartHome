package com.sumant.iot.fortyninersense;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by sumant on 10/26/16.
 */
public class GetSSMotionDoorData {

    final static String URL = "http://%s/%s?userID=%s";
    public static String getSecuritySystemData(String phpfile, String ipAddress, String userId)
    {
        try {
            return GetSSMotionDoorData.getJSON(phpfile, ipAddress, userId).getString("security_status");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static JSONObject getJSON(String phpfile, String ipAddress, String userId){
        try {
            java.net.URL url = new URL(String.format(URL, ipAddress, phpfile, userId));

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

            JSONObject data = new JSONObject(json.toString());
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public static String[] getGarageDoorStatus(String phpfile, String ipAddress, String userId)
    {
        String[] ret = new String[2];
        try {
            JSONArray data = GetSSMotionDoorData.getJSONArray(phpfile, ipAddress, userId);
            for (int i = 0; i < data.length(); i++) {
                JSONObject k = data.getJSONObject(i);
                if(k.getString("door_type").equals("1 car"))
                    ret[0] = k.getString("door_status");
                else if(k.getString("door_type").equals("2 car"))
                    ret[1] = k.getString("door_status");
            }
            return ret;


        }catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }



    }

    public static String [] getMotionSensorsStatus(String phpfile, String ipAddress, String userId)
    {
        String[] ret = new String[2];
        try {
            JSONArray data = getJSONArray(phpfile, ipAddress, userId);
            for (int i = 0; i < data.length(); i++) {
                JSONObject k = data.getJSONObject(i);
                if(k.getString("floor").equals("main"))
                    ret[0] = k.getString("status");
                else if(k.getString("floor").equals("up"))
                    ret[1] = k.getString("status");

            }
            return ret;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static String getEnergyData(String phpfile, String ipAddress)
    {
        String ret = null;//new String[2];
        try {
            java.net.URL url = new URL(String.format("http://%s/%s", ipAddress, phpfile));

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
            for (int i = 0; i < data.length(); i++) {
                JSONObject k = data.getJSONObject(i);
                ret = k.getString("energy");

            }
            return ret;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


    public static int setEnergyQuery(String phpfile, String ipAddress, String userID, String time1,
                                          String time2, String flag, String applianceID)
    {
        String[] ret = new String[2];
        try {
            //JSONArray data = getJSONArray(phpfile, ipAddress, userId,time1, time2, flag, applianceID);
            java.net.URL url = new URL(String.format("http://%s/%s?userID=%s&time1=%s&time2=%s&flag=%s" +
                    "&applianceID=%s",
                     ipAddress, phpfile, userID, time1, time2, flag, applianceID));

            HttpURLConnection connection =
                    (HttpURLConnection)url.openConnection();
            int responseCode = connection.getResponseCode();
            System.out.println(responseCode);
            return responseCode;
        }catch (Exception e){
            e.printStackTrace();
            return -1;
        }
    }

    public static String queryEnergyFlag(String phpfile, String ipAddress, String userID, String applianceID)
    {
        String ret = null;//new String[2];
        try {
            JSONArray data = getJSONArray(phpfile, ipAddress, userID);
            for (int i = 0; i < data.length(); i++) {
                JSONObject k = data.getJSONObject(i);
                ret = k.getString("flag");
            }
            return ret;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }



    public static JSONArray getJSONArray(String phpfile, String ipAddress, String userID) {
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
