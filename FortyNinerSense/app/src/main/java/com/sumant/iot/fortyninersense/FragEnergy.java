package com.sumant.iot.fortyninersense;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragEnergy extends Fragment implements View.OnClickListener,
        RadioGroup.OnCheckedChangeListener, Spinner.OnItemSelectedListener
{

    Spinner spinner;
    RadioGroup radioGroup;
    RadioButton radioButton;
    TextView consumption;
    String ipAddress = "192.168.1.114";
    String phpfile = "settempTime.php";
    String userID = "1001";
    String appID;
    public FragEnergy() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_frag_energy, container, false);
        userID = this.getArguments().getString("userID");
        ipAddress = this.getArguments().getString("ipAddress");
        spinner = (Spinner) v.findViewById(R.id.spinnerApp);
        radioGroup = (RadioGroup) v.findViewById(R.id.radioGroupTime);
        consumption = (TextView) v.findViewById(R.id.textViewConsumption);
        appID = "1";
        List<String> app = new ArrayList<>();
        app.add("Light");
        app.add("Thermostat");
        ArrayAdapter<String> appAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, app);
        appAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(appAdapter);
        radioGroup.setOnCheckedChangeListener(this);
        spinner.setOnItemSelectedListener(this);

        return v;
    }
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        String targetDateTimeString = null;//DateFormat.getDateTimeInstance().format(new Date());
        String currentDateTimeString = null;
        Date date = null;
        switch (checkedId){
            case R.id.radioButtonLastDay:
                 date = new Date(new Date().getTime()-86400000);
                targetDateTimeString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
                break;
            case R.id.radioButtonLastWeek:
                date = new Date(new Date().getTime()-604800000);
                targetDateTimeString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
                break;
            case R.id.radioButtonMonth:
                //long difmo = 2592000000;
                date = new Date(new Date().getTime()-2592000000L);
                targetDateTimeString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
                break;
        }
        Date date1 = new Date(new Date().getTime());
        currentDateTimeString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date1);
        Log.d("date",targetDateTimeString);
        currentDateTimeString = currentDateTimeString.replace(' ','+');
        targetDateTimeString = targetDateTimeString.replace(' ','+');
        GetSSMotionDoorData.setEnergyQuery(phpfile, ipAddress,  userID,
                targetDateTimeString, currentDateTimeString, "1", appID);
        String ret = GetSSMotionDoorData.
                queryEnergyFlag("gettempTime.php", ipAddress, userID,appID);
        int flag=0;
        if(ret != null) {
            while(ret.equals("1"))
            {
                ret = GetSSMotionDoorData.
                        queryEnergyFlag("gettempTime.php", ipAddress, userID,appID);
                if(ret == null) {
                    flag = 1;
                    break;
                }
            }
        }
        if(flag == 0)
        {
            ret = GetSSMotionDoorData.getEnergyData("getEnergyOutput.php", ipAddress);
            if(ret != null){
                consumption.setText(ret);
               // GetSSMotionDoorData.setEnergyQuery(phpfile, ipAddress,  userID,
                 //       targetDateTimeString, currentDateTimeString, "-1", appID);
            }

        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {

        }
    }
    public int getAppID(String val){
        if(val.equals("Light"))
            return 3;
        else if(val.equals("Thermostat"))
            return 1;
        else return 0;
    }

    public void getValues(String appID, String mode){
        return;
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(position == 0)
            appID = "3";
        else if(position == 1)
            appID = "1";
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
