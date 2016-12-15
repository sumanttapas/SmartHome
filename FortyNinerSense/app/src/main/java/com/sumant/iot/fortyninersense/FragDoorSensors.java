package com.sumant.iot.fortyninersense;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
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
 * Created by sumant on 10/25/16.
 */
public class FragDoorSensors extends Fragment implements CompoundButton.OnCheckedChangeListener {

    TextView textViewMainStatus;
    TextView textViewUpStatus;

    Switch switchMain;
    Switch switchUp;

    String ipAddress = "192.168.1.114";
    String phpfile = "getDoorSensor.php";
    String userID = "1001";

    public FragDoorSensors(){

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_frag_door_sensors, container, false);

        textViewMainStatus = (TextView) view.findViewById(R.id.textViewDoorSensorMainStatus);
        userID = this.getArguments().getString("userID");
        ipAddress = this.getArguments().getString("ipAddress");
        textViewUpStatus = (TextView) view.findViewById(R.id.textViewDoorSensorUpStatus);
        textViewMainStatus.setTextColor(getResources().getColor(R.color.ArmedAway));
        textViewUpStatus.setTextColor(getResources().getColor(R.color.ArmedAway));

        switchMain = (Switch) view.findViewById(R.id.switchDoorSensorMain);
        switchUp = (Switch) view.findViewById(R.id.switchDoorSensorUp);

        switchUp.setTextColor(getResources().getColor(R.color.ArmedAway));
        switchMain.setTextColor(getResources().getColor(R.color.ArmedAway));

        switchMain.setOnCheckedChangeListener(this);
        switchUp.setOnCheckedChangeListener(this);

        String[] retMD = GetSSMotionDoorData.getMotionSensorsStatus
                ("getDoorSensor.php", ipAddress, userID);
        if(retMD != null)
        {
            if(retMD[0].equals("engaged")){
                textViewMainStatus.setText("Engaged");
                textViewMainStatus.setTextColor(getResources().getColor(R.color.ArmedAway));
                switchMain.setChecked(true);
                switchMain.setText("Engage");
                switchMain.setTextColor(getResources().getColor(R.color.ArmedAway));
            }
            else if(retMD[0].equals("disengaged")) {
                textViewMainStatus.setText("Disengaged");
                textViewMainStatus.setTextColor(getResources().getColor(R.color.Disarmed));
                switchMain.setChecked(false);
                switchMain.setText("Disengage");
                switchMain.setTextColor(getResources().getColor(R.color.Disarmed));
            }

            if(retMD[1].equals("engaged")){
                textViewUpStatus.setText("Engaged");
                textViewUpStatus.setTextColor(getResources().getColor(R.color.ArmedAway));
                switchUp.setChecked(true);
                switchUp.setText("Engage");
                switchUp.setTextColor(getResources().getColor(R.color.ArmedAway));
            }
            else if(retMD[1].equals("disengaged")) {
                textViewUpStatus.setText("Disengaged");
                textViewUpStatus.setTextColor(getResources().getColor(R.color.Disarmed));
                switchUp.setChecked(false);
                switchUp.setText("Disengage");
                switchUp.setTextColor(getResources().getColor(R.color.Disarmed));
            }
        }



        return view;
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch(buttonView.getId())
        {
            case R.id.switchDoorSensorMain:
                if(isChecked) {
                    textViewMainStatus.setText(getResources().getString(R.string.Engaged));
                    textViewMainStatus.setTextColor(getResources().getColor(R.color.ArmedAway));
                    switchMain.setText("Engage");
                    switchMain.setTextColor(getResources().getColor(R.color.ArmedAway));
                    setDoorSensorStatus(userID, "main", "engaged");
                }
                else{
                    textViewMainStatus.setText(getResources().getString(R.string.Disengaged));
                    textViewMainStatus.setTextColor(getResources().getColor(R.color.Disarmed));
                    switchMain.setText("Disengage");
                    switchMain.setTextColor(getResources().getColor(R.color.Disarmed));
                    setDoorSensorStatus(userID, "main", "disengaged");
                }
                break;
            case R.id.switchDoorSensorUp:
                if(isChecked) {
                    textViewUpStatus.setText(getResources().getString(R.string.Engaged));
                    textViewUpStatus.setTextColor(getResources().getColor(R.color.ArmedAway));
                    switchUp.setText("Engage");
                    switchUp.setTextColor(getResources().getColor(R.color.ArmedAway));
                    setDoorSensorStatus(userID, "up", "engaged");
                }
                else{
                    textViewUpStatus.setText(getResources().getString(R.string.Disengaged));
                    textViewUpStatus.setTextColor(getResources().getColor(R.color.Disarmed));
                    switchUp.setText("Disengage");
                    switchUp.setTextColor(getResources().getColor(R.color.Disarmed));
                    setDoorSensorStatus(userID, "up", "disengaged");
                }
                break;
        }
    }

    public String setDoorSensorStatus(String userId, String floor, String status){

        InputStream inputStream = null;
        String result = "";
        ArrayList<NameValuePair> nameValuePairs1 = new ArrayList<NameValuePair>();
        nameValuePairs1.add(new BasicNameValuePair("userID",userId));
        nameValuePairs1.add(new BasicNameValuePair("floor",floor));
        nameValuePairs1.add(new BasicNameValuePair("status",status));


        //http postappSpinners
        try{
            HttpClient httpclient = new DefaultHttpClient();

            // have to change the ip here to correct ip
            HttpPost httppost = new HttpPost(String.
                    format("http://%s/%s",ipAddress,"setDoorSensors.php"));
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
        Log.d("resDoor",result);
        return result;

    }
}
