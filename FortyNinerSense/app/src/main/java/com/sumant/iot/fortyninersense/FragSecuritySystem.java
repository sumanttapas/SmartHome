package com.sumant.iot.fortyninersense;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
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


public class FragSecuritySystem extends Fragment implements View.OnClickListener{

    ImageButton imageButtonArmAway;
    ImageButton imageButtonArmStay;
    ImageButton imageButtonDisArm;

    ImageView imageViewStatus;
    TextView textViewStatus;

    String ipAddress = "192.168.1.109";
    String phpfile = "getSecurityStatus.php";
    String userID = "1001";


    public FragSecuritySystem() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_frag_security_system, container, false);
        userID = this.getArguments().getString("userID");
        ipAddress = this.getArguments().getString("ipAddress");
        imageButtonArmAway = (ImageButton) view.findViewById(R.id.imageButtonSecuritySystemArmedAway);
        imageButtonArmStay = (ImageButton) view.findViewById(R.id.imageButtonSecuritySystemArmedStay);
        imageButtonDisArm = (ImageButton) view.findViewById(R.id.imageButtonSecuritySystemDisArmed);

        textViewStatus = (TextView) view.findViewById(R.id.textViewSecuritySystemStatus);

        imageViewStatus = (ImageView) view.findViewById(R.id.imageViewSecuritySystemStatus);

        imageButtonArmStay.setOnClickListener(this);
        imageButtonDisArm.setOnClickListener(this);
        imageButtonArmAway.setOnClickListener(this);
        String rep = GetSSMotionDoorData.getSecuritySystemData
                ("getSecurityStatus.php", ipAddress, userID);

        if(rep.equals("armed stay")) {
            textViewStatus.setText("Armed Stay");
            imageViewStatus.setImageDrawable(getResources().
                    getDrawable(R.drawable.ic_armed_orange_b));
        }else if(rep.equals("armed away")){
            textViewStatus.setText("Armed Away");
            imageViewStatus.setImageDrawable(getResources().
                    getDrawable(R.drawable.ic_armed_red_b));
        }else if(rep.equals("disarmed")){
            textViewStatus.setText("Disarmed");
            imageViewStatus.setImageDrawable(getResources().
                    getDrawable(R.drawable.ic_armed_green_b));
        }


        return view;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.imageButtonSecuritySystemArmedAway:
                imageViewStatus.setImageDrawable(getResources().getDrawable(R.drawable.ic_armed_red_b));
                textViewStatus.setText(getResources().getString(R.string.ArmedAway));
                textViewStatus.setTextColor(getResources().getColor(R.color.ArmedAway));
                setSecuritySysttem(ipAddress, "armed away");
                break;
            case R.id.imageButtonSecuritySystemArmedStay:
                imageViewStatus.setImageDrawable(getResources().getDrawable(R.drawable.ic_armed_orange_b));
                textViewStatus.setText(getResources().getString(R.string.ArmedStay));
                textViewStatus.setTextColor(getResources().getColor(R.color.ArmedStay));
                setSecuritySysttem(ipAddress, "armed stay");
                break;
            case R.id.imageButtonSecuritySystemDisArmed:
                imageViewStatus.setImageDrawable(getResources().getDrawable(R.drawable.ic_armed_green_b));
                textViewStatus.setText(getResources().getString(R.string.Disarmed));
                textViewStatus.setTextColor(getResources().getColor(R.color.Disarmed));
                setSecuritySysttem(ipAddress, "disarmed");
                break;

        }
    }

    public String setSecuritySysttem(String ipAddress, String status) {
        InputStream inputStream = null;
        String result = "";
        ArrayList<NameValuePair> nameValuePairs1 = new ArrayList<NameValuePair>();
        nameValuePairs1.add(new BasicNameValuePair("userID", userID));
        nameValuePairs1.add(new BasicNameValuePair("security_status",status));



        //http postappSpinners
        try{
            HttpClient httpclient = new DefaultHttpClient();

            // have to change the ip here to correct ip
            HttpPost httppost = new HttpPost(String.
                    format("http://%s/%s",ipAddress,"setSecurityStatus.php"));
            //"http://192.168.1.109/setLightStatus.php");
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs1));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            inputStream = entity.getContent();
            Log.d("POST", "Posted");
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
        Log.d("POST",result);
        return result;
    }
}

