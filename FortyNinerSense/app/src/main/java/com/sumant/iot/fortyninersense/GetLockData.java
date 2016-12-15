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
public class GetLockData {

    final static String URL = "http://%s/%s?userID=%s";
    //4 garage,5 front, 6 back
    final static String LOCK_STATUS = "lock_status";
    //final String phpfile = "getLock.php";
    //final String ipAddress = "192.168.1.109";

    public static HashMap<String, String> getLockData(String phpfile, String ipAddress, String userId) {
        HashMap<String, String> ret = new HashMap<>();
        try {
            JSONArray data = GetThermoData.getJSONObject(phpfile, ipAddress, userId);
            for (int i = 0; i < data.length(); i++) {
                JSONObject k = data.getJSONObject(i);
                switch(k.getInt("room")){
                    case 4:
                        if(k.getString(LOCK_STATUS).equals("locked"))
                        {
                            ret.put("lock_status_garage", "locked");
                        }else if(k.getString(LOCK_STATUS).equals("unlocked"))
                        {
                            ret.put("lock_status_garage", "unlocked");
                        }
                        break;
                    case 5:
                        if(k.getString(LOCK_STATUS).equals("locked"))
                        {
                            ret.put("lock_status_front", "locked");
                        }else if(k.getString(LOCK_STATUS).equals("unlocked"))
                        {
                            ret.put("lock_status_front", "unlocked");
                        }
                        break;

                    case 6:
                        if(k.getString(LOCK_STATUS).equals("locked"))
                        {
                            ret.put("lock_status_back", "locked");
                        }else if(k.getString(LOCK_STATUS).equals("unlocked"))
                        {
                            ret.put("lock_status_back", "unlocked");
                        }
                        break;
                }
            }
            if(ret.isEmpty())
            {
                return null;
            }else
                return ret;
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("GET","ERROR in JSON Parsing");
            return null;
        }
    }


}
