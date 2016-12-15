package com.sumant.iot.fortyninersense;


import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragLights extends Fragment implements SeekBar.OnSeekBarChangeListener,
        View.OnClickListener{

    final int KITCHEN = 1;
    final int LIVING_ROOM = 2;
    final int BASEMENT = 3;
    View view;
    SeekBar seekBarLivingRoom;
    SeekBar seekBarKitchen;
    TextView textViewLVSeek;
    TextView textViewKitSeek;
    ImageButton imageButtonAll;
    ImageButton imageButtonLivingRoom;
    ImageButton imageButtonBasement;
    ImageButton imageButtonKitchen;
    String userID;
    String ipAddress;
    public FragLights()
    {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_frag_lights, container, false);
        userID = this.getArguments().getString("userID");
        ipAddress = this.getArguments().getString("ipAddress");
        seekBarLivingRoom = (SeekBar) view.findViewById(R.id.seekBarLivingRoom);
        seekBarKitchen = (SeekBar) view.findViewById(R.id.seekBarKitchen);
        textViewLVSeek = (TextView) view.findViewById(R.id.textViewLRSeek);
        textViewKitSeek = (TextView) view.findViewById(R.id.textViewKitSeek);
        imageButtonAll = (ImageButton) view.findViewById(R.id.imageButtonAll);
        imageButtonLivingRoom = (ImageButton) view.findViewById(R.id.imageButtonLivingRoom);
        imageButtonBasement = (ImageButton) view.findViewById(R.id.imageButtonBasement);
        imageButtonKitchen = (ImageButton) view.findViewById(R.id.imageButtonKitchen);
        imageButtonAll.setOnClickListener(this);
        imageButtonBasement.setOnClickListener(this);
        imageButtonKitchen.setOnClickListener(this);
        imageButtonLivingRoom.setOnClickListener(this);
        seekBarKitchen.setOnSeekBarChangeListener(this);
        seekBarLivingRoom.setOnSeekBarChangeListener(this);
        imageButtonAll.setTag(R.drawable.ic_light_off);
        imageButtonKitchen.setTag(R.drawable.ic_light_off);
        imageButtonLivingRoom.setTag(R.drawable.ic_light_off);
        imageButtonBasement.setTag(R.drawable.ic_light_off);

        JSONArray data = GetSSMotionDoorData.getJSONArray("getLightStatus.php", ipAddress, userID);
        int flag = 1;
        for (int i = 0; i < data.length(); i++) {
            try {
                JSONObject k = data.getJSONObject(i);
                switch(k.getInt("room"))
                {
                    case 1:
                        if(k.getString("light_status").equals("on")) {
                            imageButtonKitchen.setImageDrawable(getResources().getDrawable(R.drawable.ic_light_on));
                            imageButtonKitchen.setTag(R.drawable.ic_light_on);
                            seekBarKitchen.setProgress(k.getInt("dimmer_status"));
                            textViewKitSeek.setText(Integer.toString(seekBarKitchen.getProgress())+"%");
                        }
                        else {
                            flag = 0;
                            seekBarKitchen.setEnabled(false);
                            seekBarKitchen.setProgress(k.getInt("dimmer_status"));
                            imageButtonKitchen.setTag(R.drawable.ic_light_off);
                            textViewKitSeek.setText("");
                            imageButtonKitchen.setImageDrawable(getResources().getDrawable(R.drawable.ic_light_off));
                        }
                        break;
                    case 2:
                        if(k.getString("light_status").equals("on")) {
                            imageButtonLivingRoom.setImageDrawable(getResources().getDrawable(R.drawable.ic_light_on));
                            imageButtonLivingRoom.setTag(R.drawable.ic_light_on);
                            seekBarLivingRoom.setProgress(k.getInt("dimmer_status"));
                            textViewLVSeek.setText(Integer.toString(seekBarLivingRoom.getProgress())+"%");
                        }
                        else {
                            flag = 0;
                            imageButtonLivingRoom.setImageDrawable(getResources().getDrawable(R.drawable.ic_light_off));
                            imageButtonLivingRoom.setTag(R.drawable.ic_light_off);
                            seekBarLivingRoom.setEnabled(false);
                            seekBarLivingRoom.setProgress(k.getInt("dimmer_status"));
                            textViewLVSeek.setText("");
                        }

                        break;
                    case 3:
                        if(k.getString("light_status").equals("on")) {
                            imageButtonBasement.setTag(R.drawable.ic_light_on);
                            imageButtonBasement.setImageDrawable(getResources().getDrawable(R.drawable.ic_light_on));
                        }
                        else {
                            flag = 0;
                            imageButtonBasement.setTag(R.drawable.ic_light_off);
                            imageButtonBasement.setImageDrawable(getResources().getDrawable(R.drawable.ic_light_off));
                        }// seekBarLivingRoom.setProgress(k.getInt("dimmer_status"));
                        break;
                }

            }catch (Exception e)
            {
                e.printStackTrace();
            }

        }
        if(flag == 1)
        {
            imageButtonAll.setImageDrawable(getResources().getDrawable(R.drawable.ic_light_on));
            imageButtonAll.setTag(R.drawable.ic_light_on);
        }else{
            imageButtonAll.setImageDrawable(getResources().getDrawable(R.drawable.ic_light_off));
            imageButtonAll.setTag(R.drawable.ic_light_off);
        }



        return view;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if(seekBar.equals(seekBarKitchen))
        {
            String text = Integer.toString(seekBar.getProgress());
            textViewKitSeek.setText(text+"%");
        }
        else if(seekBar.equals(seekBarLivingRoom))
        {
            String text = Integer.toString(seekBar.getProgress());
            textViewLVSeek.setText(text+"%");
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if(seekBar.equals(seekBarKitchen))
        {

            setLights(userID, 1, "on", seekBar.getProgress());
        }
        else if(seekBar.equals(seekBarLivingRoom))
        {
            setLights(userID, 2, "on", seekBar.getProgress());
        }
    }

    @Override
    public void onClick(View v) {
        int tag;
        switch (v.getId())
        {
            case R.id.imageButtonAll:
                tag = (Integer) imageButtonAll.getTag();
                if(tag == R.drawable.ic_light_off)
                {
                    imageButtonAll.setImageDrawable(getResources().getDrawable(R.drawable.ic_light_on));
                    imageButtonAll.setTag(R.drawable.ic_light_on);
                    imageButtonLivingRoom.setImageDrawable(getResources().getDrawable(R.drawable.ic_light_on));
                    imageButtonLivingRoom.setTag(R.drawable.ic_light_on);
                    imageButtonKitchen.setImageDrawable(getResources().getDrawable(R.drawable.ic_light_on));
                    imageButtonKitchen.setTag(R.drawable.ic_light_on);
                    imageButtonBasement.setImageDrawable(getResources().getDrawable(R.drawable.ic_light_on));
                    imageButtonBasement.setTag(R.drawable.ic_light_on);
                    seekBarKitchen.setEnabled(true);
                    seekBarLivingRoom.setEnabled(true);
                    textViewKitSeek.setText(Integer.toString(seekBarKitchen.getProgress()));
                    textViewLVSeek.setText(Integer.toString(seekBarLivingRoom.getProgress()));

                    if(isNetworkAvailable())
                    {
                        String res = setLights(userID, LIVING_ROOM ,"on", seekBarLivingRoom.getProgress());
                        String res1 = setLights(userID, KITCHEN ,"on", seekBarKitchen.getProgress());
                        String res2= setLights(userID, BASEMENT ,"on", 0);
                        Log.d("db","ALLon");
                    }
                }
                else
                {
                    imageButtonAll.setImageDrawable(getResources().getDrawable(R.drawable.ic_light_off));
                    imageButtonAll.setTag(R.drawable.ic_light_off);
                    imageButtonLivingRoom.setImageDrawable(getResources().getDrawable(R.drawable.ic_light_off));
                    imageButtonLivingRoom.setTag(R.drawable.ic_light_off);
                    imageButtonKitchen.setImageDrawable(getResources().getDrawable(R.drawable.ic_light_off));
                    imageButtonKitchen.setTag(R.drawable.ic_light_off);
                    imageButtonBasement.setImageDrawable(getResources().getDrawable(R.drawable.ic_light_off));
                    imageButtonBasement.setTag(R.drawable.ic_light_off);
                    seekBarKitchen.setEnabled(false);
                    seekBarLivingRoom.setEnabled(false);
                    textViewKitSeek.setText("");
                    textViewLVSeek.setText("");
                    if(isNetworkAvailable())
                    {
                        String res = setLights(userID, LIVING_ROOM ,"off", seekBarLivingRoom.getProgress());
                        String res1 = setLights(userID, KITCHEN ,"off", seekBarKitchen.getProgress());
                        String res2= setLights(userID, BASEMENT ,"off", 0);
                        Log.d("db","ALLoff");
                    }
                }
                break;
            case R.id.imageButtonLivingRoom:
                tag = (Integer) imageButtonLivingRoom.getTag();
                if(tag == R.drawable.ic_light_off)
                {
                    imageButtonLivingRoom.setImageDrawable(getResources().getDrawable(R.drawable.ic_light_on));
                    imageButtonLivingRoom.setTag(R.drawable.ic_light_on);
                    seekBarLivingRoom.setEnabled(true);
                    textViewLVSeek.setText(Integer.toString(seekBarLivingRoom.getProgress()));
                    if(isNetworkAvailable())
                    {
                        String res = setLights(userID, LIVING_ROOM ,"on", seekBarLivingRoom.getProgress());
                        Log.d("db",res);
                    }

                }
                else
                {
                    imageButtonLivingRoom.setImageDrawable(getResources().getDrawable(R.drawable.ic_light_off));
                    imageButtonLivingRoom.setTag(R.drawable.ic_light_off);
                    seekBarLivingRoom.setEnabled(false);
                    //textViewKitSeek.setText("");
                    textViewLVSeek.setText("");
                    if(isNetworkAvailable())
                    {
                        String res = setLights(userID, LIVING_ROOM ,"off", seekBarLivingRoom.getProgress());
                        Log.d("db",res);
                        Log.d("SB",Integer.toString(seekBarLivingRoom.getProgress()));
                    }
                }
                break;
            case R.id.imageButtonKitchen:
                tag = (Integer) imageButtonKitchen.getTag();
                if(tag == R.drawable.ic_light_off)
                {
                    imageButtonKitchen.setImageDrawable(getResources().getDrawable(R.drawable.ic_light_on));
                    imageButtonKitchen.setTag(R.drawable.ic_light_on);
                    seekBarKitchen.setEnabled(true);
                    textViewKitSeek.setText(Integer.toString(seekBarKitchen.getProgress()));
                    if(isNetworkAvailable())
                    {

                        String res1 = setLights(userID, KITCHEN ,"on", seekBarKitchen.getProgress());
                        Log.d("db",res1);
                        Log.d("SB",Integer.toString(seekBarKitchen.getProgress()));

                    }
                }
                else
                {
                    imageButtonKitchen.setImageDrawable(getResources().getDrawable(R.drawable.ic_light_off));
                    imageButtonKitchen.setTag(R.drawable.ic_light_off);
                    seekBarKitchen.setEnabled(false);
                    textViewKitSeek.setText("");
                   // textViewLVSeek.setText("");
                    if(isNetworkAvailable())
                    {

                        String res1 = setLights(userID, KITCHEN ,"off", seekBarKitchen.getProgress());
                        Log.d("db",res1);
                        Log.d("SB",Integer.toString(seekBarKitchen.getProgress()));

                    }
                }
                break;
            case R.id.imageButtonBasement:
                tag = (Integer) imageButtonBasement.getTag();
                if(tag == R.drawable.ic_light_off)
                {
                    imageButtonBasement.setImageDrawable(getResources().getDrawable(R.drawable.ic_light_on));
                    imageButtonBasement.setTag(R.drawable.ic_light_on);
                    if(isNetworkAvailable())
                    {

                        String res2= setLights(userID, BASEMENT ,"on", 0);
                        Log.d("db",res2);
                    }
                }
                else
                {
                    imageButtonBasement.setImageDrawable(getResources().getDrawable(R.drawable.ic_light_off));
                    imageButtonBasement.setTag(R.drawable.ic_light_off);
                    if(isNetworkAvailable())
                    {

                        String res2= setLights(userID, BASEMENT ,"off", 0);
                        Log.d("db",res2);
                    }
                }
                break;
                //Log.d("tag",Integer.toString(tag));
        }
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().
                getSystemService(getActivity().CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        // if no network is available networkInfo will be null, otherwise check if we are connected
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }

    public String setLights(String userId, int room, String lightStatus, int dimmerValue){

        InputStream inputStream = null;
        String result = "";
        ArrayList<NameValuePair> nameValuePairs1 = new ArrayList<NameValuePair>();
        nameValuePairs1.add(new BasicNameValuePair("userID",userId));
        nameValuePairs1.add(new BasicNameValuePair("room",Integer.toString(room)));
        nameValuePairs1.add(new BasicNameValuePair("light_status",lightStatus));
        nameValuePairs1.add(new BasicNameValuePair("dimmer_status",Integer.toString(dimmerValue)));

        //http postappSpinners
        try{
            HttpClient httpclient = new DefaultHttpClient();

            // have to change the ip here to correct ip
            HttpPost httppost = new HttpPost(
                    String.format("http://%s/%s",ipAddress,"setLightStatus.php"));
            //"http://192.168.1.109/setLightStatus.php");
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs1));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            inputStream = entity.getContent();
        }
        catch(Exception e){
            Log.e("log_tag", "Error in http connection "+e.toString());
            Toast.makeText(getActivity(), "Server Not Responding", Toast.LENGTH_SHORT).show();
            return "";
        }
        //convert response to string
        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"),8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            inputStream.close();
            result=sb.toString();
        }
        catch(Exception e){
            Log.e("log_tag", "Error converting result "+e.toString());
        }
        Log.d("res",result);
        return result;

    }



}
