package com.sumant.iot.fortyninersense;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
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
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragLocks extends Fragment implements View.OnClickListener{

    View view;
    ImageButton buttonLock;
    ImageButton buttonUnlock;
    CheckBox checkBoxGarage;
    CheckBox checkBoxFrontDoor;
    CheckBox checkBoxBackDoor;
    ImageView imageViewGarage;
    ImageView imageViewFrontDoor;
    ImageView imageViewBackDoor;
    TextView textViewFrontDoor;
    TextView textViewBackDoor;
    TextView textViewGarageDoor;
    String ipAddress = "192.168.1.109";
    String phpfile = "getLock.php";
    String userID = "1001";
    public FragLocks() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d("OnCreate","In OnCreate View");
        view = inflater.inflate(R.layout.fragment_frag_locks, container, false);
        userID = this.getArguments().getString("userID");
        ipAddress = this.getArguments().getString("ipAddress");
        buttonLock = (ImageButton) view.findViewById(R.id.imageButtonLock);
        buttonUnlock = (ImageButton)  view.findViewById(R.id.imageButtonUnlock);
        checkBoxBackDoor = (CheckBox) view.findViewById(R.id.checkBoxBackDoor);
        checkBoxFrontDoor = (CheckBox) view.findViewById(R.id.checkBoxFrontDoor);
        checkBoxGarage = (CheckBox) view.findViewById(R.id.checkBoxGarageDoor);
        imageViewGarage = (ImageView) view.findViewById(R.id.imageViewGarageDoor);
        imageViewFrontDoor = (ImageView) view.findViewById(R.id.imageViewFrontDoors);
        imageViewBackDoor = (ImageView) view.findViewById(R.id.imageViewBackDoor);
        textViewGarageDoor = (TextView) view.findViewById(R.id.textViewGarageDoor);
        textViewFrontDoor = (TextView) view.findViewById(R.id.textViewFrontDoor);
        textViewBackDoor = (TextView) view.findViewById(R.id.textViewBackDoor);
        buttonUnlock.setOnClickListener(this);
        buttonLock.setOnClickListener(this);

        HashMap<String, String> ret = GetLockData.getLockData(phpfile, ipAddress, userID);
        if(ret != null)
        {
            if(ret.get("lock_status_garage").equals("locked")){
                imageViewGarage.setImageDrawable(getResources().getDrawable(R.drawable.ic_keylocked_s));
                textViewGarageDoor.setText("Locked");
                textViewGarageDoor.setTextColor(getResources().getColor(R.color.ArmedAway));
            }else if(ret.get("lock_status_garage").equals("unlocked")){
                imageViewGarage.setImageDrawable(getResources().getDrawable(R.drawable.ic_keyunlocked_s));
                textViewGarageDoor.setText("UnLocked");
                textViewGarageDoor.setTextColor(getResources().getColor(R.color.Disarmed));
            }

            if(ret.get("lock_status_front").equals("locked")){
                imageViewFrontDoor.setImageDrawable(getResources().getDrawable(R.drawable.ic_keylocked_s));
                textViewFrontDoor.setText("Locked");
                textViewFrontDoor.setTextColor(getResources().getColor(R.color.ArmedAway));
            }else if(ret.get("lock_status_front").equals("unlocked")){
                imageViewFrontDoor.setImageDrawable(getResources().getDrawable(R.drawable.ic_keyunlocked_s));
                textViewFrontDoor.setText("UnLocked");
                textViewFrontDoor.setTextColor(getResources().getColor(R.color.Disarmed));
            }

            if(ret.get("lock_status_back").equals("locked")){
                imageViewBackDoor.setImageDrawable(getResources().getDrawable(R.drawable.ic_keylocked_s));
                textViewBackDoor.setText("Locked");
                textViewBackDoor.setTextColor(getResources().getColor(R.color.ArmedAway));
            }else if(ret.get("lock_status_back").equals("unlocked")){
                imageViewBackDoor.setImageDrawable(getResources().getDrawable(R.drawable.ic_keyunlocked_s));
                textViewBackDoor.setText("UnLocked");
                textViewBackDoor.setTextColor(getResources().getColor(R.color.Disarmed));
            }
        }


        return view;
    }

    @Override
    public void onClick(View v) {
        Log.d("OnClick","In the Function");
        switch (v.getId())
        {
            case R.id.imageButtonUnlock:
                if(checkBoxGarage.isChecked())
                {
                    imageViewGarage.setImageDrawable(getResources().getDrawable(R.drawable.ic_keyunlocked_s));
                    textViewGarageDoor.setText("Unlocked");
                    textViewGarageDoor.setTextColor(getResources().getColor(R.color.Disarmed));
                    setLock(userID,4,"unlocked");
                }
                if(checkBoxFrontDoor.isChecked())
                {
                    imageViewFrontDoor.setImageDrawable(getResources().getDrawable(R.drawable.ic_keyunlocked_s));
                    textViewFrontDoor.setText("Unlocked");
                    textViewFrontDoor.setTextColor(getResources().getColor(R.color.Disarmed));
                    setLock(userID,5,"unlocked");
                }
                if(checkBoxBackDoor.isChecked())
                {
                    imageViewBackDoor.setImageDrawable(getResources().getDrawable(R.drawable.ic_keyunlocked_s));
                    textViewBackDoor.setText("Unlocked");
                    textViewBackDoor.setTextColor(getResources().getColor(R.color.Disarmed));
                    setLock(userID,6,"unlocked");
                }
                break;
            case R.id.imageButtonLock:
                if(checkBoxGarage.isChecked())
                {
                    imageViewGarage.setImageDrawable(getResources().getDrawable(R.drawable.ic_keylocked_s));
                    textViewGarageDoor.setText("Locked");
                    textViewGarageDoor.setTextColor(getResources().getColor(R.color.ArmedAway));
                    setLock(userID,4,"locked");
                }
                if(checkBoxFrontDoor.isChecked())
                {
                    imageViewFrontDoor.setImageDrawable(getResources().getDrawable(R.drawable.ic_keylocked_s));
                    textViewFrontDoor.setText("Locked");
                    textViewFrontDoor.setTextColor(getResources().getColor(R.color.ArmedAway));
                    setLock(userID,5,"locked");
                }
                if(checkBoxBackDoor.isChecked())
                {
                    imageViewBackDoor.setImageDrawable(getResources().getDrawable(R.drawable.ic_keylocked_s));
                    textViewBackDoor.setText("Locked");
                    textViewBackDoor.setTextColor(getResources().getColor(R.color.ArmedAway));
                    setLock(userID,6,"locked");
                }
                break;
        }
    }

    public String setLock(String userId, int room, String lockStatus){

        InputStream inputStream = null;
        String result = "";
        ArrayList<NameValuePair> nameValuePairs1 = new ArrayList<NameValuePair>();
        nameValuePairs1.add(new BasicNameValuePair("userID",userId));
        nameValuePairs1.add(new BasicNameValuePair("room",Integer.toString(room)));
        nameValuePairs1.add(new BasicNameValuePair("lock_status",lockStatus));


        //http postappSpinners
        try{
            HttpClient httpclient = new DefaultHttpClient();

            // have to change the ip here to correct ip
            HttpPost httppost = new HttpPost(String.format("http://%s/%s",ipAddress,"setLock.php"));
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
        Log.d("resLock",result);
        return result;

    }
}
