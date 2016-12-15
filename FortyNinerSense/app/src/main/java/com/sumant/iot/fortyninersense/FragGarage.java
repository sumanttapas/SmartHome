package com.sumant.iot.fortyninersense;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragGarage extends Fragment implements View.OnClickListener{

    TextView textView1DoorStatus;
    TextView textView2DoorStatus;

    ImageButton imageButton1DoorStatus;
    ImageButton imageButton2DoorStatus;
    String userID = "1001";
    String ipAddress = "192.168.1.114";
    String phpfile = "getGarageDoor.php";
    Thread updateDB;
    public FragGarage() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_frag_garage, container, false);
        userID = this.getArguments().getString("userID");
        ipAddress = this.getArguments().getString("ipAddress");
        textView1DoorStatus = (TextView) view.findViewById(R.id.textView1DoorStatus);
        textView2DoorStatus = (TextView) view.findViewById(R.id.textView2DoorStatus);

        imageButton1DoorStatus = (ImageButton) view.findViewById(R.id.imageButton1DoorStatus);
        imageButton1DoorStatus.setTag(R.drawable.ic_garage_door_closed);
        imageButton2DoorStatus = (ImageButton) view.findViewById(R.id.imageButton2DoorStatus);
        imageButton2DoorStatus.setTag(R.drawable.ic_garage_door_closed);
        imageButton1DoorStatus.setOnClickListener(this);
        imageButton2DoorStatus.setOnClickListener(this);

        String [] retG = GetSSMotionDoorData.
                getGarageDoorStatus(phpfile, ipAddress, userID);
        if(retG != null)
        {
            if(retG[0].equals("open")) {
                imageButton1DoorStatus.setImageDrawable(getResources().
                        getDrawable(R.drawable.ic_garage_door_open));
                imageButton1DoorStatus.setTag(R.drawable.ic_garage_door_open);
                textView1DoorStatus.setText("Opened");
                textView1DoorStatus.setTextColor(getResources().getColor(R.color.Disarmed));
            }
            else if(retG[0].equals("close")) {
                imageButton1DoorStatus.setImageDrawable(getResources().
                        getDrawable(R.drawable.ic_garage_door_closed));
                imageButton1DoorStatus.setTag(R.drawable.ic_garage_door_closed);
                textView1DoorStatus.setText("Closed");
                textView1DoorStatus.setTextColor(getResources().getColor(R.color.ArmedAway));
            }
            if(retG[1].equals("open")) {
                imageButton2DoorStatus.setImageDrawable(getResources().
                        getDrawable(R.drawable.ic_garage_door_open));
                imageButton2DoorStatus.setTag(R.drawable.ic_garage_door_open);
                textView2DoorStatus.setText("Opened");
                textView2DoorStatus.setTextColor(getResources().getColor(R.color.Disarmed));
            }
            else if(retG[1].equals("close")) {
                imageButton2DoorStatus.setImageDrawable(getResources().
                        getDrawable(R.drawable.ic_garage_door_closed));
                imageButton2DoorStatus.setTag(R.drawable.ic_garage_door_closed);
                textView2DoorStatus.setText("Closed");
                textView2DoorStatus.setTextColor(getResources().getColor(R.color.ArmedAway));
            }
        }

        return view;
    }



    @Override
    public void onClick(View v) {
        int tag;
        switch (v.getId())
        {
            case R.id.imageButton1DoorStatus:
                tag = (int)imageButton1DoorStatus.getTag();
                if(tag == R.drawable.ic_garage_door_closed){
                    imageButton1DoorStatus.setImageDrawable(getResources().
                            getDrawable(R.drawable.ic_garage_door_open));
                    imageButton1DoorStatus.setTag(R.drawable.ic_garage_door_open);
                    textView1DoorStatus.setText("Open");
                    textView1DoorStatus.setTextColor(getResources().getColor(R.color.Disarmed));
                    setGarageDoorStatus(userID, "1 car", "open");
                }else{
                    imageButton1DoorStatus.setImageDrawable(getResources().
                            getDrawable(R.drawable.ic_garage_door_closed));
                    imageButton1DoorStatus.setTag(R.drawable.ic_garage_door_closed);
                    textView1DoorStatus.setText("Closed");
                    textView1DoorStatus.setTextColor(getResources().getColor(R.color.ArmedAway));
                    setGarageDoorStatus(userID, "1 car", "close");
                }
                break;
            case R.id.imageButton2DoorStatus:
                tag = (int)imageButton2DoorStatus.getTag();
                if(tag == R.drawable.ic_garage_door_closed){
                    imageButton2DoorStatus.setImageDrawable(getResources().
                            getDrawable(R.drawable.ic_garage_door_open));
                    imageButton2DoorStatus.setTag(R.drawable.ic_garage_door_open);
                    textView2DoorStatus.setText("Open");
                    textView2DoorStatus.setTextColor(getResources().getColor(R.color.Disarmed));
                    setGarageDoorStatus(userID, "2 car", "open");
                }else{
                    imageButton2DoorStatus.setImageDrawable(getResources().
                            getDrawable(R.drawable.ic_garage_door_closed));
                    imageButton2DoorStatus.setTag(R.drawable.ic_garage_door_closed);
                    textView2DoorStatus.setText("Closed");
                    textView2DoorStatus.setTextColor(getResources().getColor(R.color.ArmedAway));
                    setGarageDoorStatus(userID, "2 car", "close");
                }
                break;
        }
    }

    public String setGarageDoorStatus(String userId, String door, String status){

        InputStream inputStream = null;
        String result = "";
        ArrayList<NameValuePair> nameValuePairs1 = new ArrayList<NameValuePair>();
        nameValuePairs1.add(new BasicNameValuePair("userID",userId));
        nameValuePairs1.add(new BasicNameValuePair("door_type",door));
        nameValuePairs1.add(new BasicNameValuePair("door_status",status));


        //http postappSpinners
        try{
            HttpClient httpclient = new DefaultHttpClient();

            // have to change the ip here to correct ip
            HttpPost httppost = new HttpPost(String.format("http://%s/%s",ipAddress,"setGarageDoor.php"));
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
        Log.d("set",result);
        return result;

    }

}
